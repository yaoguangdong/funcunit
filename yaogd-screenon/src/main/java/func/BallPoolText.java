package func;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 球形蓄水池，根据文字大小自适应
 * @author yaoguangdong
 * 2015-1-12
 */
public class BallPoolText extends View {
	
	private int diameter ;//直径
	private int padding = 30 ;//内边距
    private Paint mPaint;
    private Path mPath;
    private TextPaint textPaint;
    private String rateText = "0%" ;
    private int textSize ;
    private int textHeight; 
    private int textWidth ;
    private float rate = 0.0f ;
    
    public BallPoolText(Context context) {
        super(context);
    }
    
    public BallPoolText(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        //获取属性集合
  		textPaint = new TextPaint();
  		// 设置画笔无锯齿
  		textPaint.setAntiAlias(true);
  		// 初始化文字颜色
  		textPaint.setColor(Color.parseColor("0xFFFEFEFE")) ;
  		// 初始化尺寸
  		textSize = 40;
      	if (textSize > 0) {
      		textPaint.setTextSize(textSize);
      	} else{
      		textPaint.setTextSize(20);
      	}
          	
        // 初始化球的大小
        Rect textBounds = new Rect();
        textPaint.getTextBounds(rateText, 0, rateText.length(), textBounds);

        textHeight = textBounds.height() ; 
        textWidth = textBounds.width() ; 
        diameter = (textWidth > textHeight ? textWidth : textHeight) + padding ;
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        
        mPath = new Path();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {

        //取代
        canvas.save();
        mPath.reset();
        canvas.clipPath(mPath); // makes the clip empty
        
        mPath.addCircle(diameter / 2, diameter / 2, diameter / 2, Path.Direction.CCW);
        canvas.clipPath(mPath, Region.Op.REPLACE);

        canvas.clipRect(0, 0, diameter, diameter);
        
        canvas.drawColor(Color.argb(255, 0x63, 0x6f, 0x8e));//dark blue
        
        mPaint.setColor(Color.argb(255, 0x33, 0x99, 0xcc));//light blue
        canvas.drawRect(0, (int)(diameter * (1 - rate)), diameter, diameter, mPaint) ;
        
        canvas.drawText(rateText, (diameter - textWidth) / 2, (diameter + textHeight -10) / 2, textPaint);
        //
        canvas.restore();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(diameter, diameter);
    }
    
	public void setRateForShow(float rate, String rateForShow) {
		this.rate = rate;
		this.rateText = rateForShow ;
		invalidate() ;
	}

}