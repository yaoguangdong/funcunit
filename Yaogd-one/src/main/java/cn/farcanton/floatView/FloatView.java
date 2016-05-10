package cn.farcanton.floatView;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

public class FloatView extends TableLayout{ 
	//用来计算位移
	private float mTouchX; 
	private float mTouchY; 
	private float x; 
	private float y; 
	private int statusBarHeight ;
	//用来判断点击事件
	private float mStartX; 
	private float mStartY; 
	private OnClickListener mClickListener; 
	
	private WindowManager windowManager = (WindowManager) getContext() 
	.getApplicationContext().getSystemService(Context.WINDOW_SERVICE); 
	// 此windowManagerParams变量为获取的全局变量，用以保存悬浮窗口的属性 
	private WindowManager.LayoutParams windowManagerParams = ((FloatApplication) getContext() 
	.getApplicationContext()).getWindowParams(); 
	public FloatView(Context context) { 
		super(context); 
		//获取到状态栏的高度 
		Rect frame = new Rect(); 
		getWindowVisibleDisplayFrame(frame); 
		this.statusBarHeight = frame.top; 
	} 
	@Override 
	public boolean onTouchEvent(MotionEvent event) { 

		// 获取相对屏幕的坐标，即以屏幕左上角为原点 
		x = event.getRawX(); 
		// statusBarHeight是系统状态栏的高度 ,悬浮图标不能拖到装填栏上去，所以要减掉。
		y = event.getRawY() - statusBarHeight; 
		//在位移中，一直不断的更新
		Log.i("tag", "currRawX" + x + ",currRawY" + y); 
		switch (event.getAction()) { 
			case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作 
				// 获取相对View的坐标，即以此View左上角为原点 
				mTouchX = event.getX(); 
				mTouchY = event.getY(); 
				mStartX = x; 
				mStartY = y; 
				Log.i("tag", "touchDownX" + mTouchX + ",touchDownY"  + mTouchY); 
				break; 
			case MotionEvent.ACTION_MOVE: 
				//View控件自动识别移动动作，并循环调用onTouch来更新当前的坐标(x,y)
				updateViewPosition(); 
				break; 
			case MotionEvent.ACTION_UP: 
				updateViewPosition(); 
				mTouchX = mTouchY = 0; 
				if ((x - mStartX) < 5 && (y - mStartY) < 5) { 
					if(mClickListener!=null) { 
						mClickListener.onClick(this); 
					} 
				} 
				break; 
		} 
		return true; 
	} 
	@Override 
	public void setOnClickListener(OnClickListener l) { 
		this.mClickListener = l; 
	} 
	/**
	 * 在位移中，一直不断的调用。
	 */
	private void updateViewPosition() { 
		// 更新浮动窗口位置参数 ，screen纵坐标向下为正，横坐标向右为正。
		windowManagerParams.x = (int) (x - mTouchX); 
		windowManagerParams.y = (int) (y - mTouchY); 
		//使用位移差做增量计算，而不是使用绝对坐标重新定位。
		windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示 
	} 
} 



