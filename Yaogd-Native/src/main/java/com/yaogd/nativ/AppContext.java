package com.yaogd.nativ;

import android.app.Application;

/**
 * 
 * author yaoguangdong
 * 2015-8-16
 */
public class AppContext extends Application {
	
	/**Application实例*/
	private static AppContext appContext = null ;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		appContext = this ;
		
	}
	
	
	
}
