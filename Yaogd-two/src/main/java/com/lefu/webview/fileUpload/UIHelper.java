package com.lefu.webview.fileUpload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 应用程序UI工具包
 * @author yaoguangdong
 * 2014-8-25
 */
public class UIHelper {
	
	
	/**
	 * 隐藏软键盘
	 */
	public static void hideInputMethod(Activity act){
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	}
	
	/**
	 * 显示软键盘
	 */
	public static void showInputMethod(Activity act){
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	}
	
	/**
	 * 简单发送消息
	 * handler在Activity销毁时，置null，否则可能导致Activity内存泄露
	 */
	public static void sendMsgToHandler(Handler handler, int what, Object obj){
		if(handler != null){
			Message msg = new Message();
			msg.what = what ;
			msg.obj = obj ;
			handler.sendMessage(msg) ;
		}
	}
	
	/**
	 * 简单发送消息
	 * handler在Activity销毁时，置null，否则可能导致Activity内存泄露
	 */
	public static void sendMsgToHandler(Handler handler,int what){
		if(handler != null){
			handler.sendEmptyMessage(what) ;
		}
	}
	
	/**
	 * 简单发送消息
	 * handler在Activity销毁时，置null，否则可能导致Activity内存泄露
	 */
	public static void sendMsgToHandler(Handler handler,int what, int arg1){
		if(handler != null){
			Message msg = new Message();
			msg.what = what ;
			msg.arg1 = arg1 ;
			handler.sendMessage(msg) ;
		}
	}
	
	/**
	 * 简单发送消息
	 * handler在Activity销毁时，置null，否则可能导致Activity内存泄露
	 */
	public static void sendMsgToHandler(Handler handler,int what, int arg1, long delayMillis){
		if(handler != null){
			Message msg = new Message();
			msg.what = what ;
			msg.arg1 = arg1 ;
			handler.sendMessageDelayed(msg, delayMillis) ;
		}
	}
	
	/**
	 * 简单发送消息
	 * handler在Activity销毁时，置null，否则可能导致Activity内存泄露
	 */
	public static void sendMsgToHandler(Handler handler,int what, int arg1 ,Object obj){
		if(handler != null){
			Message msg = new Message();
			msg.what = what ;
			msg.arg1 = arg1 ;
			msg.obj = obj ;
			handler.sendMessage(msg) ;
		}
	}
	
	/**
	 * 显示错误页面
	 */
	public static void showErrorPage(Activity act , WebView webView, int errorCode){
		String errorHtml = null;
		try {
			errorHtml = readData(act.getAssets().open("error.html"), "UTF-8");
		} catch (IOException e) { 
		}
		//此日志暂时开放
		if(isNetError(errorCode)){
			errorHtml = errorHtml.replace("#Error-Pic#", "net_error.png");
		}else{
			errorHtml = errorHtml.replace("#Error-Pic#", "server_error.png");
		}
		webView.loadDataWithBaseURL("file:///android_asset/", errorHtml, "text/html", "utf-8", null);
	}
	
	/**暂时定义两种错误*/
	public static final int NET_ERROR = -2 ;
	public static final int SERVER_ERROR = -7 ;
	
	/**
	 * 判断是否网络连接异常
	 */
	private static boolean isNetError(int errorCode){
		int index = 0 ;
		boolean result = false ;
	
		do{
			if(netErrorCode[index] == errorCode){
				result = true ;
				break ;
			}
			index++ ;
		} while(index < netErrorCode.length);
		
		return result ;
	}
	
	private static int [] netErrorCode = new int [5] ;
	static {
		//无法连接到服务器
		netErrorCode[0] = WebViewClient.ERROR_CONNECT ;
		//服务器或代理主机名查找失败
		netErrorCode[1] = WebViewClient.ERROR_HOST_LOOKUP ;
		//连接超时 
		netErrorCode[2] = WebViewClient.ERROR_TIMEOUT ;
		//通用错误
		netErrorCode[3] = WebViewClient.ERROR_UNKNOWN ;
		//不支持的URI格式 
		netErrorCode[4] = WebViewClient.ERROR_UNSUPPORTED_SCHEME ;
	} ;
	

	/**
	 * stream to string
	 */
	public static String readData(InputStream inputStream, String encodeType) {
		String result = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] byteBuffer = new byte[1024];
		int size = 0;
		byte[] partByteArray;
		try {
			while ((size = inputStream.read(byteBuffer)) != -1) {
				outputStream.write(byteBuffer, 0, size);
			}
			partByteArray = outputStream.toByteArray();
			outputStream.close();
			inputStream.close();
			result = new String(partByteArray, encodeType);
		} catch (IOException e) {
		}
		return result;
	}
}
