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
import android.widget.ImageView;

/**
 * 球形蓄水池，长宽固定，文字自适应居中
 * @author yaoguangdong
 * 2015-1-12
 */
public class BallPoolImage extends ImageView {
	
	private int diameter ;//直径
	private int padding = 10 ;//内边距
	private Canvas mCanvas ;
    private Paint waterPaint;
    private Path mPath;
    private TextPaint textPaint;
    private String rateText = "0%" ;
    private float rate = 0.0f ;
    private int backgroundColor ;
    private int imageId ;//如果不为-1，则显示图片
    public static final int NO_IMAG = -0x590 ;
    
    public BallPoolImage(Context context) {
        super(context);
    }
    
    public BallPoolImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        
  		textPaint = new TextPaint();
  		// 设置画笔无锯齿
  		textPaint.setAntiAlias(true);
  		// 初始化文字颜色
  		textPaint.setColor(Color.parseColor("0xFFFEFEFE")) ;
  		
  		// 读取背景颜色
  		backgroundColor = Color.parseColor("0xFF22AA55") ;
  		
        waterPaint = new Paint();
        waterPaint.setAntiAlias(true);
        // 初始化水深颜色
  		waterPaint.setColor(Color.parseColor("0xFFCC4499")) ;
  		
        mPath = new Path();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	this.mCanvas = canvas ;
        
    	if(imageId == NO_IMAG){
    		pushWater() ;
    	}else{
    		super.onDraw(canvas) ;
    	}
    }
    
    /**
     * 蓄水
     */
    private void pushWater(){
    	
    	int saveCount = mCanvas.getSaveCount();
    	//锁定画布
    	mCanvas.save();
    	
    	mPath.reset();
        // makes the clip empty
    	mCanvas.clipPath(mPath); 
        // canvas circle clip region 
        mPath.addCircle(diameter / 2, diameter / 2, diameter / 2, Path.Direction.CCW);
        mCanvas.clipPath(mPath, Region.Op.REPLACE);
        // canvas background region 
        mCanvas.clipRect(0, 0, diameter, diameter);
        mCanvas.drawColor(backgroundColor);
        
    	// canvas Draft(吃水深度) region 
        mCanvas.drawRect(0, (int)(diameter * (1 - rate)), diameter, diameter, waterPaint) ;
        
     	// calculate text size
        float textSize = (diameter - padding) / rateText.length() ;
        textPaint.setTextSize(textSize) ;
        
        Rect textBounds = new Rect();
        textPaint.getTextBounds(rateText, 0, rateText.length(), textBounds);

        int textWidth = textBounds.width() ; 
        
        mCanvas.drawText(rateText, (diameter - textWidth) / 2, (diameter + textSize) / 2, textPaint);
     	//解锁画布
//    	mCanvas.restore();
    	mCanvas.restoreToCount(saveCount);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	int viewWidth = measureWidth(widthMeasureSpec) ;
    	int viewHeight = measureHeight(heightMeasureSpec) ;
    	
    	diameter = viewWidth < viewHeight ? viewHeight: viewWidth ;
    	
        setMeasuredDimension(diameter, diameter);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = super.getWidth() ;
        }
        return result;
    }
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = super.getHeight() ;
        }
        return result;
    }
    
	public void setRateForShow(float rate, String rateForShow ) {
		imageId = NO_IMAG ;
		this.rate = rate;
		this.rateText = rateForShow ;
		invalidate() ;
	}
	
	@Override
	public void setImageResource(int resId) {
		imageId = resId ;
		super.setImageResource(resId);
	}

}