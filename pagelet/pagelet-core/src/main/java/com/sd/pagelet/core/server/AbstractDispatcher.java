package com.sd.pagelet.core.server;

public abstract class AbstractDispatcher implements Dispatcher {
	
	

	@Override
	public String getHttpApiPath() {
		return "api/"+getPackageName();
	}
	
	@Override
	public String getResourceFilePath() {
		return getClass().getResource("./").getPath();
	}
	
	@Override
	public String getResourcePath() {
		return "resources/"+getPackageName();
	}
	
	@Override
	public String getWebSocketApiPath() {
		return null;
	}
	
	@Override
	public void dispatchRequest(ServerLiteExchange serverLiteExchange) {
	}
	
	private String getPackageName(){
		String packageName = getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf(".")+1);
	}
}
