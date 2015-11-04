package com.sd.pagelet.core.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

public class ServerProviderManager implements ServerProvider{
	
	private static List<ServerProvider> serverProviders = new ArrayList<>();
	private static ServiceLoader<ServerProvider> serverLoader = ServiceLoader.load(ServerProvider.class); 
	static{
		for( ServerProvider provider : serverLoader ){
			serverProviders.add(provider);
		}
	}
	
	private static ServerProviderManager serverProviderManager = new ServerProviderManager();
	
	private ServerProviderManager() {
	}
	
	public static ServerProviderManager getInstance(){
		return serverProviderManager;
	}
	
	public void addServerProvider( ServerProvider serverProvider ){
		serverProviders.add(serverProvider);
	}
	

	@Override
	public void init(Properties configuration){
		for( ServerProvider provider : serverProviders ){
			provider.init(configuration);
		}
	}
	
	@Override
	public void addDispatcher(Dispatcher dispatcher){
		for( ServerProvider provider : serverProviders ){
			provider.addDispatcher(dispatcher);
		}
	}
	
	@Override
	public void start() {
		for( ServerProvider provider : serverProviders ){
			provider.start();
		}
	}
	
	@Override
	public void stop() {
		for( ServerProvider provider : serverProviders ){
			provider.stop();
		}
	}

}
