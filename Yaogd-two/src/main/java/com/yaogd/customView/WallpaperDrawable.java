package com.yaogd.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lefu.A;

/**
 * 按照整个屏幕的比例进行放大并剪裁。
 * 这种效果还不知道用在什么地方。
 * @author yaoguangdong 2015-1-28
 */
public class WallpaperDrawable extends ImageView {

	public WallpaperDrawable(Context context) {
		super(context);
	}

	public WallpaperDrawable(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	Bitmap mBitmap;
	int mIntrinsicWidth;
	int mIntrinsicHeight;

	public void setBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
		if (mBitmap == null) {
			return;
		}
		mIntrinsicWidth = mBitmap.getWidth();
		mIntrinsicHeight = mBitmap.getHeight();
	}

	@Override
	public void draw(Canvas canvas) {
		if (mBitmap == null) {
			return;
		}
//		int width = canvas.getWidth();//屏幕的宽高
//		int height = canvas.getHeight();
		//测试
		int width = 720;
		int height = 100;

		A.i("Bitmap width is " + mIntrinsicWidth + ", height is "
				+ mIntrinsicHeight + ". Canvas width is " + width
				+ ", height is " + height);

		// / M: scale up the bitmap to make it cover the entire area
		float scalew = width / (float) mIntrinsicWidth;
		float scaleh = height / (float) mIntrinsicHeight;

		if (scalew > 1.0 || scaleh > 1.0) {
			A.i("Draw by scale size");
			float scale = scalew > scaleh ? scalew : scaleh;
			int scaledWidth = (int) (mIntrinsicWidth * scale);
			int scaledHeight = (int) (mIntrinsicHeight * scale);
			int x = (width - scaledWidth) / 2;
			int y = (height - scaledHeight) / 2;

			Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmap,
					scaledWidth, scaledHeight, true);
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
					Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
			canvas.drawBitmap(scaledBitmap, x, y, null);
			scaledBitmap.recycle();
			scaledBitmap = null;
		} else {
			A.i("Draw by original size");
			int x = (width - mIntrinsicWidth) / 2;
			int y = (height - mIntrinsicHeight) / 2;
			canvas.drawBitmap(mBitmap, x, y, null);
		}

	}

}