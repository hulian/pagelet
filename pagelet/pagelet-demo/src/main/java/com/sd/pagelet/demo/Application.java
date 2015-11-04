package com.sd.pagelet.demo;

import com.sd.pagelet.core.PageLiteApplication;
import com.sd.pagelet.demo.page.home.HomeDispatcher;
import com.sd.pagelet.demo.page.lib.JsLibDispatcher;
import com.sd.pagelet.demo.page.registration.RegistrationDispatcher;

public class Application {

	public static void main(String[] args) {
		PageLiteApplication.builder()
			.withDispatcher(new HomeDispatcher())
			.withDispatcher(new JsLibDispatcher())
			.withDispatcher(new RegistrationDispatcher())
			.build().run();
	}
}
