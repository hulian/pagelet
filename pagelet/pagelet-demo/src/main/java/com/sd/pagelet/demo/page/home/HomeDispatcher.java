package com.sd.pagelet.demo.page.home;

import java.io.IOException;

import com.sd.pagelet.core.server.AbstractDispatcher;
import com.sd.pagelet.core.server.ServerLiteExchange;
import com.sd.pagelet.undertow.utils.TokenUtil;

public class HomeDispatcher extends AbstractDispatcher{
	
	@Override
	public String getResourcePath() {
		return "/";
	}

	@Override
	public void dispatchRequest(ServerLiteExchange serverLiteExchange) {
		System.out.println(serverLiteExchange.getParams());
		System.out.println(serverLiteExchange.getToken());
		serverLiteExchange.setToken(TokenUtil.createRandomToken());
		try {
			serverLiteExchange.getOutputStream().write("success".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
