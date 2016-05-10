package cn.farcanton.floatButton;

import android.app.Application;
import android.view.WindowManager;

public class FloatApplication extends Application {
	/**
	 * 做全局量使用
	 */
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams(); 
	public WindowManager.LayoutParams getWindowParams() { 
		return windowParams; 
	} 

}
