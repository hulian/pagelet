package com.sd.pagelet.core.server;

public interface Dispatcher {

	String getResourcePath();
	String getResourceFilePath();
	String getHttpApiPath();
	String getWebSocketApiPath();
	void dispatchRequest(ServerLiteExchange serverLiteExchange);
	
}
