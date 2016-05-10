package com.yaogd.lib;

import android.util.Log;

/**
 * 应用程序快捷引用类
 * @date 2013-12-2 下午8:40:37
 * @author Administrator guangdong.yao
 */
public class A {
	
	//TAG
	private static final String T = "yaogd" ;
	
	@SuppressWarnings("rawtypes")
	public static void e(Class clazz, String msg) {
		Log.e(T, clazz.getSimpleName() + "," + msg);
	}
	
	public static void e(String msg) {
		Log.e(T, msg);
	}
	
	public static void e(String msg,Throwable tr) {
		Log.e(T, msg,tr);
	}

	public static void i(String msg) {
		Log.i(T, msg);
	}
	
	@SuppressWarnings("rawtypes") 
	public static void i(Class clazz, String msg) {
		Log.i(T, clazz.getSimpleName() + "," + msg);
	}
	
	public static void d(String msg) {
		Log.i(T, msg);
	}
	
	@SuppressWarnings("rawtypes") 
	public static void d(Class clazz, String msg) {
		Log.i(T, clazz.getSimpleName() + "," + msg);
	}
	
	public static void v(String msg) {
		Log.i(T, msg);
	}
	
	@SuppressWarnings("rawtypes") 
	public static void v(Class clazz, String msg) {
		Log.i(T, clazz.getSimpleName() + "," + msg);
	}
	
	public static void w(String msg) {
		Log.i(T, msg);
	}
	
	@SuppressWarnings("rawtypes") 
	public static void w(Class clazz, String msg) {
		Log.i(T, clazz.getSimpleName() + "," + msg);
	}
	
}
