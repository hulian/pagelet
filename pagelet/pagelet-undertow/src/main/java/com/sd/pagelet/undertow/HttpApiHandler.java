package com.sd.pagelet.undertow;

import java.io.ByteArrayOutputStream;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map.Entry;
import org.jboss.logging.Logger;

import com.sd.pagelet.core.server.Dispatcher;
import com.sd.pagelet.core.server.ServerLiteExchange;
import com.sd.pagelet.undertow.utils.TokenUtil;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.HttpString;

public class HttpApiHandler  implements HttpHandler {

	private static final Logger logger = Logger.getLogger(HttpApiHandler.class);
	
	private final FormParserFactory formParserFactory;

	private Dispatcher dispatcher;
	
	public HttpApiHandler(Dispatcher dispatcher) {
		 this.dispatcher = dispatcher;
		 this.formParserFactory = FormParserFactory.builder().build();
	}

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {

		if (exchange.isInIoThread()) {
			exchange.dispatch(this);
			return;
		}
		exchange.getResponseHeaders().add(new HttpString("Access-Control-Allow-Headers"),
				"origin, authorization, content-type, accept, referer, user-agent");
		exchange.getResponseHeaders().add(new HttpString("Access-Control-Allow-Method"), "GET, POST, OPTIONS");
		exchange.getResponseHeaders().add(new HttpString("Access-Control-Allow-Origin"), "*");

		exchange.startBlocking();
		try {
			// 获取token
			String token = null;
			if (exchange.getRequestCookies().get(TokenUtil.TOKEN_COOKIE_NMAE) != null) {
				token = exchange.getRequestCookies().get(TokenUtil.TOKEN_COOKIE_NMAE).getValue();
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ServerLiteExchange serverLiteExchange = new ServerLiteExchange();
			
			//检查并解析FORM数据
			if(exchange.getRequestHeaders().get("Content-Type").getFirst().contains("form")){
				if(exchange.isBlocking()){
					FormDataParser parser = formParserFactory.createParser(exchange);
					FormData formData = parser.parseBlocking();
					Iterator<String> iterator = formData.iterator(); 
					while(iterator.hasNext()){
						String key = iterator.next();
						serverLiteExchange.getParams().put(key,formData.get(key).getLast().getValue());
					}
				}
			}else{
				serverLiteExchange.setInputStream(exchange.getInputStream());
			}
			
			//获取URL参数
			Iterator<Entry<String, Deque<String>>>  iterator = exchange.getQueryParameters().entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, Deque<String>> entry = iterator.next();
				serverLiteExchange.getParams().put(entry.getKey(),entry.getValue().getLast());
			}
			
			serverLiteExchange.setOutputStream(outputStream);
			serverLiteExchange.setToken(token);
			serverLiteExchange.setUrl(exchange.getRelativePath());
			
			dispatcher.dispatchRequest(serverLiteExchange);

			if (serverLiteExchange.getToken() != null) {
				exchange.getResponseCookies().put(TokenUtil.TOKEN_COOKIE_NMAE,
						new CookieImpl(TokenUtil.TOKEN_COOKIE_NMAE, serverLiteExchange.getToken()));
			}
			exchange.getOutputStream().write(outputStream.toByteArray());
		} catch (Exception e) {
			logger.error("SERVER ERROR", e);
		}
		exchange.endExchange();

	}

}
