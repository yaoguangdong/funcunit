package cn.farcanton.floatButton;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
/**
 * 天天动听的悬浮歌词，带有进度效果
 * @author yaoguangdong
 * 2014-5-14
 */
public class TopFrame extends Activity {
	
	private MyTextView tv = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(tv != null && tv.isShown()){
			WindowManager wm = (WindowManager)getApplicationContext().getSystemService(TopFrame.this.WINDOW_SERVICE);
			wm.removeView(tv);
		}
		show();
	}
	
	private void show(){
		Rect frame = new Rect();  
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
		MyTextView.TOOL_BAR_HIGH = frame.top;  
		
		WindowManager wm = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
		WindowManager.LayoutParams params = MyTextView.params;
		
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		
		params.width = WindowManager.LayoutParams.FILL_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.alpha = 80;
		
		params.gravity=Gravity.LEFT|Gravity.TOP;
	    //����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
		params.x = 0;
		params.y = 0;
	        
		tv = new MyTextView(TopFrame.this);
		wm.addView(tv, params);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
		
//		if(tv != null && tv.isShown()){
//			wm.removeView(tv);
//		}
		super.onDestroy();
	}
	
	
	
}