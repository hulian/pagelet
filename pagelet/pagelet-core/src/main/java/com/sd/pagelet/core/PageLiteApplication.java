package com.sd.pagelet.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sd.pagelet.core.server.Dispatcher;
import com.sd.pagelet.core.server.ServerProviderManager;

public class PageLiteApplication {
	
	

	public void run(){
		ServerProviderManager.getInstance().start();
	}
	
	
	public static class Builder{
		
		private Properties configuration = new Properties();
		
		private List<Dispatcher> dispatchers = new ArrayList<>();
		
		public Builder withConfiguration( Properties configuration ){
			this.configuration=configuration;
			return this;
		}
		
		public Builder withDispatcher( Dispatcher dispatcher ){
			dispatchers.add(dispatcher);
			return this;
		}
		
		public PageLiteApplication build(){
			
			ServerProviderManager spm = ServerProviderManager.getInstance();
			for( Dispatcher dispatcher : dispatchers ){
				spm.addDispatcher(dispatcher);
			}
			spm.init(configuration);
			
			return new PageLiteApplication();
		}
		
	}
	

	public static Builder builder(){
		return new Builder();
	}

}
