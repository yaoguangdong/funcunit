package cn.farcanton.customView;

import cn.farcanton.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomBtnView extends View {
	
	private final static String TAG = "CustomBtnView" ;
    private Paint mTextPaint, mSecantPaint;
    private String mText;
    private int mAscent;
    private int textLength;
    private Canvas mCanvas ;
    private int mscentColor ;
    
    public CustomBtnView(Context context) {
        super(context);
        initLabelView();
    }

    public CustomBtnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLabelView();

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomBtnView);

        CharSequence s = a.getString(R.styleable.CustomBtnView_text);
        if (s != null) {
            setText(s.toString());
        }

        setTextColor(a.getColor(R.styleable.CustomBtnView_textColor, 0xFF000000));
        mscentColor = a.getColor(R.styleable.CustomBtnView_secantColor, 0xFF000000);
        setSecantColor(mscentColor);

        int textSize = a.getDimensionPixelOffset(R.styleable.CustomBtnView_textSize, 0);
        if (textSize > 0) {
            setTextSize(textSize);
        }
        //Give back a previously retrieved StyledAttributes, for later re-use.
        a.recycle();
    }

    private final void initLabelView() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(16);
        mTextPaint.setColor(0xFF000000);
        mSecantPaint = new Paint();
        mSecantPaint.setColor(0xFF9900);
        setPadding(6, 6, 6, 6);
    }

    public void setText(String text) {
        mText = text;
        requestLayout();
        invalidate();
    }

    public void setTextSize(int size) {
        mTextPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    public void setSecantColor(int color){
    	mSecantPaint.setColor(color);
    	invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
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
            result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
                    + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        textLength = result;
        return result;
    }
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //ascent()返回基线之上的距离。
        mAscent = (int) mTextPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        drawDoubleRect(mCanvas) ;
    }
    
    //自定义事件1
  	interface MyOnFocusListener {
  		public void onKeyFous();
  	}
  	private MyOnFocusListener myOnFocusListener ;
  	public void setMyOnFocusListener(MyOnFocusListener myOnFocusListener) {
  		this.myOnFocusListener = myOnFocusListener;
  	}
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	if(event.getAction() == MotionEvent.ACTION_DOWN){
    		
    		//回调自定义事件1
    		if(myOnFocusListener != null){
    			myOnFocusListener.onKeyFous();
    		}
    		
    		Log.i(TAG, "motionEvent excute1") ;
    		setSecantColor(Color.GREEN);
    	}else if(event.getAction() == MotionEvent.ACTION_UP){
    		Log.i(TAG, "motionEvent excute2") ;
    		setSecantColor(mscentColor);
    	}
		return super.onTouchEvent(event);
	}
    
    //自定义事件2
	abstract static public class myOnClickListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
			Log.i(TAG, "customBtnView clicked native");
			//内部执行的业务
			
			//回调自定义事件2
			myClick() ;
		}
		abstract public void myClick();
    }

	/*
	 * 画带双边框的按钮
	 */
	private void drawDoubleRect(Canvas canvas)
	{
		int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //这里的paddingTop == paddingBottom == 4,在初始化时设定的。
        float stopY = paddingTop - mAscent + paddingBottom;
        float ptsLeftOuter [] = {2f,2f,2f,stopY};
        float ptsLeftInner [] = {4f,4f,4f,stopY - 2 };
        float ptsLeftOuterInnerLine [] = {2f,2f,4f,4f};
        float ptsUpOuter [] = {2f,2f,textLength,2f};
        float ptsUpInner [] = {4f,4f,textLength - 2,4f};
        float ptsUpOuterInnerLine [] = {textLength,2f,textLength - 2, 4f} ;
        float ptsRightOuter [] = {textLength - 1, 2, textLength - 1, stopY} ;
        float ptsRightInner [] = {textLength - 3, 2, textLength - 3, stopY -2} ;
        float ptsRightOuterInnerLine [] = {textLength - 3, stopY -2, textLength - 1, stopY} ;
        float ptsDownInner [] = {4,stopY - 2 ,textLength - 2 , stopY - 2} ;
        float ptsDownOuter [] = {2,stopY,textLength , stopY} ;
        float ptsDownOuterInnerLine [] = {4,stopY - 2 ,2,stopY} ;
        
        canvas.drawText(mText, paddingLeft, paddingTop - mAscent, mTextPaint);
        //画字左外边的竖线
        canvas.drawLines(ptsLeftOuter, mSecantPaint);
        //画字左内边的竖线
        canvas.drawLines(ptsLeftInner,mSecantPaint);
        //画连线
        canvas.drawLines(ptsLeftOuterInnerLine, mSecantPaint) ;
        //上外边
        canvas.drawLines(ptsUpOuter,mSecantPaint);
        //上内边
        canvas.drawLines(ptsUpInner, mSecantPaint) ;
        //画连线
        canvas.drawLines(ptsUpOuterInnerLine, mSecantPaint) ;
        //右外边的竖线
        canvas.drawLines(ptsRightOuter ,mSecantPaint);
        //右内边的竖线
        canvas.drawLines(ptsRightInner ,mSecantPaint);
        //画连线
        canvas.drawLines(ptsRightOuterInnerLine, mSecantPaint) ;
        //下外边
        canvas.drawLines(ptsDownOuter,mSecantPaint);
        //下内边
        canvas.drawLines(ptsDownInner,mSecantPaint);
        //画连线
        canvas.drawLines(ptsDownOuterInnerLine, mSecantPaint) ;
	}
}
