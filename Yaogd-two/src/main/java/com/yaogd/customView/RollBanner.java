package com.yaogd.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 目标效果：等比例缩放，适应屏幕最大宽度，同时又包裹图片
 * 用于混动展示广告的RollBanner
 * @author yaoguangdong 2015-1-28
 */
public class RollBanner extends ImageView {

	private int resizeHeight ;
	
	public RollBanner(Context context) {
		super(context);
	}

	public RollBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		
		int canvasWidth = 720 ;
		
		int intrinsicWidth = bitmap.getWidth();
		int intrinsicHeight = bitmap.getHeight();
		
		float h = canvasWidth * intrinsicHeight / (float)intrinsicWidth + 0.5f;
		resizeHeight = (int)h ;
		
		super.setImageBitmap(bitmap) ;
		
		//强制使用这种模式，以配合计算出来的高度，实现目标效果
		super.setScaleType(ScaleType.CENTER_CROP) ;
		
		invalidate() ;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas) ;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), resizeHeight);
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
	 
}