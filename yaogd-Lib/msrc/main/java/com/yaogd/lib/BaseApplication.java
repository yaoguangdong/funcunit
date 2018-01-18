package com.yaogd.lib;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application{

	private static Context context ;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = getApplicationContext();

		//L.d("Application " + getResources().getString(R.string.app_name));
	}

	public static Context getContext() {
		return context;
	}

}
