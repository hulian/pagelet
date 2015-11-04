package com.sd.pagelet.core.server;

import java.util.Properties;


public interface ServerProvider {

	void init(Properties configuration);
	void addDispatcher(Dispatcher dispatcher);
	void start();
	void stop();
}
