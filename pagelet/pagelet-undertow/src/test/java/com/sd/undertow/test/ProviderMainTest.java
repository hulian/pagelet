package com.sd.undertow.test;

import com.sd.pagelet.core.PageLiteApplication;
import com.sd.undertow.test.dispatcher.lib.JsLibDispatcher;
import com.sd.undertow.test.dispatcher.test.TestDispatcher;

public class ProviderMainTest {

	public static void main(String[] args) {
		PageLiteApplication.builder()
			.withDispatcher(new TestDispatcher())
			.withDispatcher(new JsLibDispatcher())
			.build().run();
	}
}
