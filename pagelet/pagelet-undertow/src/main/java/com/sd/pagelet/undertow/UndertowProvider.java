package com.sd.pagelet.undertow;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.jboss.logging.Logger;

import com.sd.pagelet.core.server.Dispatcher;
import com.sd.pagelet.core.server.ServerProvider;

import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.encoding.ContentEncodedResourceManager;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.server.handlers.resource.CachingResourceManager;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;

public class UndertowProvider implements ServerProvider {

	private static final Logger logger = Logger.getLogger(UndertowProvider.class);
	
	private List<Dispatcher> dispatchers = new ArrayList<>();

	private Undertow undertow;

	@Override
	public void init(Properties configuration) {

		PathHandler pathHandler = new PathHandler();
		
		for( Dispatcher dispatcher : dispatchers ){
			
			 //如果设置了静态资源路径，添加静态资源HANDLER
			 if( dispatcher.getResourcePath()!=null ){
				 ResourceManager resourceManager = new FileResourceManager(new File(dispatcher.getResourceFilePath()), 10240);
				 ResourceHandler resourceHandler = new ResourceHandler(resourceManager);
				//静态资源，压缩
				 if(configuration.getProperty("http.gzip", "false").equals("true")){
			        ContentEncodedResourceManager encoder =  new ContentEncodedResourceManager(Paths.get(dispatcher.getResourcePath()), new CachingResourceManager(100, 10000, null, resourceManager, -1), new ContentEncodingRepository()
			                .addEncodingHandler("gzip", new GzipEncodingProvider(), 50, null),1024, 1024*500, null);
		        	resourceHandler.setContentEncodedResourceManager(encoder);
		         }
				 pathHandler.addPrefixPath(
						dispatcher.getResourcePath(), 
						resourceHandler);
				 logger.info("add resource handler for path:"+dispatcher.getResourcePath()+" file path:"+dispatcher.getResourceFilePath());
			 }
			 
			 if( dispatcher.getHttpApiPath()!=null ){
				 pathHandler.addPrefixPath(dispatcher.getHttpApiPath(), new HttpApiHandler(dispatcher));
				 logger.info("add http api handler for path:"+dispatcher.getHttpApiPath());
			 }
			 
			 
		}
		
		
		int processes = Runtime.getRuntime().availableProcessors();
		
		//IO处理线程数,默认CPU核数
		int ioThread = Integer.parseInt(
				configuration.getProperty("io_thread_number", 
				String.valueOf(processes + 1))
				);
		//WORKER线程数,默认CPU核数*200
		int workerThread = Integer.parseInt(
				configuration.getProperty("work_thread_number",
				String.valueOf(processes * 200))
				);
		//SERVER绑定地址，默认0.0.0.0
		String httpHost = configuration.getProperty("http.host", "0.0.0.0");
		
		//Server端口，默认8080
		int httpPort = Integer.parseInt(configuration.getProperty("http.port", "8080"));

		
		undertow = Undertow.builder().addHttpListener(httpPort, httpHost).setIoThreads(ioThread)
				.setWorkerThreads(workerThread).setHandler(pathHandler).build();
	}

	@Override
	public void addDispatcher(Dispatcher dispatcher) {
		dispatchers.add(dispatcher);
	}

	@Override
	public void start() {
		if (undertow != null) {
			logger.info("Start Undertow server......");
			undertow.start();
			logger.info("Start Undertow OK!");
			// kill消息钩子函数，关闭系统使用的资源
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					logger.info("Stop Undertow......");
					undertow.stop();
					logger.info("Stop Undertow OK!");
				}
			}));
		}
	}

	@Override
	public void stop() {
		if (undertow != null) {
			logger.info("Stop Undertow......");
			undertow.stop();
			logger.info("Stop Undertow OK!");
		}
	}

}
