package com.lefu;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 应用程序配置类
 * @date 2013-12-2 下午8:40:37
 * @author Administrator guangdong.yao
 */
public class A {
	//开发模式
	public static final boolean DEBUG = true;
	//超时时间
	public static final int TIMEOUT = 30000;
	//TAG
	public static final String T = "yaogd" ;
	
	/**
	 * 数据下载地址
	 */
	public static final String url= "http://www.lefu8.com/netcheck/ips.xml" ;
	
	public static void e(String tag, String msg) {
		if (DEBUG)
			Log.e(tag, msg);
	}
	public static void e(String msg) {
		if (DEBUG)
			Log.e(T, msg);
	}

	public static void e(String msg,Throwable tr) {
		if (DEBUG)
			Log.e(T, msg,tr);
	}
	
	public static void i(String msg) {
		if (DEBUG)
			Log.i(T, msg);
	}
	
	public static void i(String tag, String msg) {
		if (DEBUG)
			Log.i(tag, msg);
	}
	
	public static int readIntPreferences(Context cxt, String paramString)
    {
      return cxt.getSharedPreferences("camera-parameter", 0).getInt(paramString, 0);
    }
    
	public static void writeIntPreferences(Context cxt, String paramString, int paramInt)
    {
      SharedPreferences.Editor localEditor = cxt.getSharedPreferences("camera-parameter", 0).edit();
      localEditor.putInt(paramString, paramInt);
      localEditor.commit();
    }
    
	public static String readPreferences(Context cxt, String paramString)
    {
      return cxt.getSharedPreferences("camera-parameter", 0).getString(paramString, null);
    }
    
	public static void writePreferences(Context cxt, String paramString1, String paramString2)
    {
      SharedPreferences.Editor localEditor = cxt.getSharedPreferences("camera-parameter", 0).edit();
      localEditor.putString(paramString1, paramString2);
      localEditor.commit();
    }
    
}
