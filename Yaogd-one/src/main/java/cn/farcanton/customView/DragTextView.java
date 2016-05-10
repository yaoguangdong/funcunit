package cn.farcanton.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;
import android.widget.TextView;
/**
 * 自定义可以拖动的视图
 * @author yaoguangdong
 * 2014-5-1
 */
public class DragTextView extends TextView{

	private float firstX, firstY;
	
	public DragTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			firstX = event.getX();
			firstY = event.getY();
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			AbsoluteLayout.LayoutParams p = (AbsoluteLayout.LayoutParams)getLayoutParams();
			p.x += (event.getX() - firstX) ;
			p.y += (event.getY() - firstY);
			this.setLayoutParams(p) ;
		}
		return true ;
	}
}
