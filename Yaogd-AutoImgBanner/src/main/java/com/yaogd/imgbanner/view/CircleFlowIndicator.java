package com.yaogd.imgbanner.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yaogd.imgbanner.R;

public class CircleFlowIndicator extends View {

    private static final int STYLE_STROKE = 0;
	private static final int STYLE_FILL = 1;

	private float radius;
	private final Paint mPaintInactive = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint mPaintActive = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int count = 4 ;//默认4个
	private int currentIndex = 0;
	
	/**
	 * Default constructor
	 * @param context
	 */
    public CircleFlowIndicator(Context context) {
        super(context);
        initColors(0xFFFFFFFF, 0xFFFFFFFF, STYLE_FILL, STYLE_STROKE);
        this.radius = setRadius(TypedValue.COMPLEX_UNIT_DIP, 8f);
    }

	/**
	 * The contructor used with an inflater
	 * @param context
	 * @param attrs
	 */
	public CircleFlowIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Retrieve styles attributs
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleFlowIndicator);

		// Gets the inactive circle type, defaulting to "fill"
		int activeType = a.getInt(R.styleable.CircleFlowIndicator_activeType,
				STYLE_FILL);
		
		int activeDefaultColor = 0xFFFFFFFF;
		
		// Get a custom inactive color if there is one
		int activeColor = a
				.getColor(R.styleable.CircleFlowIndicator_activeColor,
						activeDefaultColor);

		int inactiveDefaultColor = 0x44FFFFFF;
		// Get a custom inactive color if there is one
		int inactiveColor = a.getColor(
				R.styleable.CircleFlowIndicator_inactiveColor,
				inactiveDefaultColor);

		// Retrieve the radius
		radius = a.getDimension(R.styleable.CircleFlowIndicator_radius, 8f);
		
		a.recycle();
		initColors(activeColor, inactiveColor, activeType, activeType);
		 
	}

	private void initColors(int activeColor, int inactiveColor, int activeType,
			int inactiveType) {
		// Select the paint type given the type attr
		switch (inactiveType) {
		case STYLE_FILL:
			mPaintInactive.setStyle(Style.FILL);
			break;
		default:
			mPaintInactive.setStyle(Style.STROKE);
		}
		mPaintInactive.setColor(inactiveColor);

		// Select the paint type given the type attr
		switch (activeType) {
		case STYLE_STROKE:
			mPaintActive.setStyle(Style.STROKE);
			break;
		default:
			mPaintActive.setStyle(Style.FILL);
		}
		mPaintActive.setColor(activeColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float circleSeparation = 4*radius;
		//this is the amount the first circle should be offset to make the entire thing centered
		float centeringOffset = 0;
		int leftPadding = getPaddingLeft();
		// Draw stroked circles
		for (int iLoop = 0; iLoop < count; iLoop++) {
			canvas.drawCircle(leftPadding + radius
					+ (iLoop * circleSeparation) + centeringOffset,
					getPaddingTop() + radius, radius, mPaintInactive);
		}
		float separation=circleSeparation*currentIndex;
		// The flow width has been upadated yet. Draw the default position
		canvas.drawCircle(leftPadding + radius +separation, getPaddingTop()
				+ radius, radius, mPaintActive);
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
		invalidate();
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		float circleSeparation = 4*radius;
		// We were told how big to be
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		// Calculate the width according the views count
		else {
			
			result = (int) (getPaddingLeft() + getPaddingRight()
					+ (count * 2 * radius+circleSeparation) + (count ) * radius + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// We were told how big to be
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		// Measure the height
		else {
			result = (int) (2 * radius + getPaddingTop() + getPaddingBottom() + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Sets the fill color
	 * 
	 * @param color
	 *            ARGB value for the text
	 */
	public void setFillColor(int color) {
		mPaintActive.setColor(color);
		invalidate();
	}

	/**
	 * Sets the stroke color
	 * 
	 * @param color
	 *            ARGB value for the text
	 */
	public void setStrokeColor(int color) {
		mPaintInactive.setColor(color);
		invalidate();
	}
	
	/**
	 * @param unit TypedValue.COMPLEX_UNIT_PX
	 * @param radius
	 */
	public float setRadius(int unit, float radius) {
	    Resources r = getContext().getResources();
        return TypedValue.applyDimension(unit, radius, r.getDisplayMetrics());
	}

    public void setCount(int count) {
        this.count = count;
    }

}