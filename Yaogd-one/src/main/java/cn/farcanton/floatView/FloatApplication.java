package cn.farcanton.floatView;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

public class FloatApplication extends Application {
	/**
	 * 做全局量使用
	 */
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams(); 
	public WindowManager.LayoutParams getWindowParams() { 
		return windowParams; 
	} 
	private WindowManager.LayoutParams windowParams_dld = new WindowManager.LayoutParams(); 
	public WindowManager.LayoutParams getFloatDldWindowParams() { 
		return windowParams_dld; 
	} 
	private WindowManager windowManager = null;
	public WindowManager getWinManager(){
		if(windowManager == null){
			windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); 
		}
		return windowManager;
	}
	private static FloatApplication tcmApp ;
	//单例模式
	public static FloatApplication getInstence(){
		return tcmApp;
	}
}
