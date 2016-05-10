package cn.farcanton.customView;

import cn.farcanton.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
/**
 * 自定义布局，在CustomViewActivity中使用
 * @author yaoguangdong
 * 2014-5-1
 */
public class CustomLayout extends LinearLayout{

	private Paint basePaint ,framePaint;
	
	private int baseColor, frameColor;
	
	public CustomLayout(Context context, AttributeSet attrs) {
		super(context,attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.customView);
		baseColor = a.getColor(R.styleable.customView_baseColor, 0xC84B4B9B) ;
		frameColor = a.getColor(R.styleable.customView_frameColor, 0xC8373787) ;
		
		init();
	}

	private void init() {
		// paint在init中初始化，在dispatchDraw中会降低效率
		basePaint = new Paint() ;
		//basePaint.setARGB(200, 75, 75, 135) ;
		basePaint.setColor(baseColor) ;
		
		framePaint = new Paint();
		//framePaint.setARGB(200, 55, 55, 135) ;
		framePaint.setColor(frameColor);
		framePaint.setAntiAlias(true) ;
		framePaint.setStyle(Style.STROKE) ;
		framePaint.setStrokeWidth(2) ;
		
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		RectF drawRect = new RectF();
		setPadding(8, 8, 8, 8) ;
		drawRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight()) ;
		canvas.drawRoundRect(drawRect, 15, 15, basePaint) ;
		canvas.drawRoundRect(drawRect, 15, 15, framePaint) ;
		
		Log.i("yaogd", "dispatchDraw" ) ;
		
		super.dispatchDraw(canvas);
	}
	

}
