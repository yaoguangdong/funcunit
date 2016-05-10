package cn.farcanton.horizontalScroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {
	private OnHorizontalScrollViewScrollfinish event;

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	protected void onFinishInflate(){
		super.onFinishInflate();
	}
	
	public void onFinishTemporaryDetach(){
		super.onFinishTemporaryDetach();
	}
	
	protected void onScrollChanged(int l,int t,int oldl,int oldt){
		super.onScrollChanged(l, t, oldl, oldt);
		AppUtils.writeLog("mxt","onScrollChanged l " + l + "   t = " + t  + "    oldl = " + oldl + "    oldt = " + oldt + "  RIGHT = " + this.getRight());
		event.onLeftChange(l);
		event.onRightChange(l, this.computeHorizontalScrollRange()-this.getRight());
		
	}
	public void setOnHorizontalScrollViewEvent(OnHorizontalScrollViewScrollfinish event){
		this.event=event;
	}
	
	public void onStartTemporaryDetach(){
		super.onStartTemporaryDetach();
	}

}
