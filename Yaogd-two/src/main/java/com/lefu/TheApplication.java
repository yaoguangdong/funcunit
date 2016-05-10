package com.lefu;

import android.app.Application;

/**
 * 整个(app)程序初始化之前被调用
 */
public class TheApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		MyCrashHandler handler = MyCrashHandler.getInstance();
		handler.init(getApplicationContext());
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}
}
