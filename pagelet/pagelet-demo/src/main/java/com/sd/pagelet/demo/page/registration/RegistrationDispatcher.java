package com.sd.pagelet.demo.page.registration;

import java.io.IOException;

import com.sd.pagelet.core.server.AbstractDispatcher;
import com.sd.pagelet.core.server.ServerLiteExchange;
import com.sd.pagelet.undertow.utils.TokenUtil;

public class RegistrationDispatcher extends AbstractDispatcher{

	@Override
	public void dispatchRequest(ServerLiteExchange serverLiteExchange) {
		System.out.println(serverLiteExchange.getParams());
		serverLiteExchange.setToken(TokenUtil.createRandomToken());
		try {
			serverLiteExchange.getOutputStream().write("success".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
