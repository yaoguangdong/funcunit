package com.yaogd.lib;

import android.util.Log;

public class L {

	private static String TAG = "ADPV";
	private static boolean isDebug;

	static {
		L.init(true);
	}

	public static final void init(boolean isDebug){
		L.isDebug = isDebug;
	}

	/**
	 * 打印Verbose级别日志，使用默认 TAG 
	 * 
	 * @param msg
	 */
	public static void v(String msg) {
		if (isDebug) {
			Log.v(TAG, msg);
		}
	}

	/**
	 * 打印Verbose级别日志
	 * 
	 * @param msg
	 */
	public static void v(String tag, String msg) {
		if (isDebug) {
			Log.v(tag, msg);
		}
	}
	
	/**
	 * 打印Info级别日志，使用默认 TAG 
	 * 
	 * @param msg
	 */
	public static void i(String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
		}
	}
	
	/**
	 * 打印Info级别日志
	 * 
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	/**
	 * 默认 TAG 打印日志
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	/**
	 * 日志是否打印可控 d
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	/**
	 * 默认 TAG 打印日志
	 * 
	 * @param msg
	 */
	public static void w(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	/**
	 * 日志是否打印可控 w
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}
	
	/**
     * 日志是否打印可控 w
     * 
     * @param tag
     */
    public static void w(String tag, Throwable tr) {
        if (isDebug) {
            Log.w(tag, tr);
        }
    }

	/**
	 * 日志是否打印可控 e
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}

	/**
	 * 日志是否打印e
	 * 
	 * @param msg
	 */
	public static void e(String msg) {
		if (isDebug) {
			Log.e(TAG, msg);
		}
	}
	
	/**
	 * 日志是否打印e
	 * 
	 * @param tag
	 * @param msg
	 * @param tr
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if (isDebug) {
			Log.e(tag, msg, tr);
		}
	}

	/**
	 * 是否是调试模式
	 * @return
     */
	public static boolean isDebug(){
		return isDebug;
	}
}