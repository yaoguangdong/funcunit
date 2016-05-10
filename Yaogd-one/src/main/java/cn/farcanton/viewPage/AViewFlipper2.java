package cn.farcanton.viewPage;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;
import cn.farcanton.R;
/**
 * 
 * 另外一种思路：构建一个Overlay,这个Overlay实现 OnGestureListener接口，
 * 使其维护自己的GestureDetector。在主视图上添加这个Overlay,并传入相应的 listener，
 * 即可实现捕捉手势的功能。
 *
 */
public class AViewFlipper2 extends Activity implements GestureDetector.OnGestureListener{
	private ViewFlipper mViewFlipper;
	private GestureDetector gesture ;
	private float endX ;
	private AnimationSet rightOut;
	private AnimationSet leftIn;
	private AnimationSet leftOut;
	private AnimationSet rightIn;
	
	private int count = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.view_flipper_main);
		super.onCreate(savedInstanceState) ;
		getWindow().setBackgroundDrawableResource(R.drawable.faya);
		mViewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper1);
		
		//步骤一，建立GestureDetector对象，并将监听器对象（this）传入
		gesture = new GestureDetector(this);
		
		madeInXML();
	}
	private void madeInXML(){
		leftIn = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.left_in);
		rightOut = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.right_out);
		rightIn = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.right_in);
		leftOut = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.left_out);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//步骤二，触发屏幕的Touch Event时，调用gesture的Touch Event
		if(event.getAction() == MotionEvent.ACTION_UP){
			endX = event.getRawX();
		}
		return gesture.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		
		return false;
	}
	/**
	 * 滑动事件结束后才会触发
	 * onFling() 甩，这个甩的动作是在一个MotionEvent.ACTION_UP(手指抬起)发生时执行，
	 * 而onScroll()，只要手指移动就会执行。onScroll不会 执行MotionEvent.ACTION_UP。
	 * onFling通常用来实现翻页效果，而onScroll通常用来实现放大缩小和移动。
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float x = e1.getRawX() ;
		
		//滑动位移>70 且 滑动速度 > 300
		if(Math.abs(x - endX) > 70 && Math.abs(velocityX) > 300){
			Log.i("yaogd", "rawStartX:"+x);
			Log.i("yaogd", "velocityX:"+velocityX);
			Log.i("yaogd", "endX:"+endX);
			
			//判断向左滑动
			if(x > endX){
				if(count > 1){
					count-- ;
					
					mViewFlipper.clearAnimation();
					mViewFlipper.setInAnimation(rightIn );
					mViewFlipper.setOutAnimation(leftOut );
					
					mViewFlipper.setDisplayedChild(count);
				}
			}else{
				if(count < 4){
					count++;
					
					mViewFlipper.clearAnimation();
					mViewFlipper.setInAnimation(leftIn );
					mViewFlipper.setOutAnimation(rightOut );
					
					mViewFlipper.setDisplayedChild(count);
				}
			}
		}
		
		endX = 0;
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
