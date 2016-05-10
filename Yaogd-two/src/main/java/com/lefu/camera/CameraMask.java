package com.lefu.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.lefu.A;
import com.yaogd.R;

/**
 * 矩形框
 * @author: yaoguangdong
 * @data: 2014-2-1
 */
public class CameraMask extends View {
	public Rect mFrame;
	public boolean visiable = true ;
	private Context mCxt;
	//边框的最小范围
	private int frameLeftMin,frameTopMin,frameRightMin,frameBottomMin ;
	private float startX, startY ;
	private int frameLeftMove, frameRightMove, frameTopMove, frameBottomMove ;
	//判断当前触摸的是左边还是右边，0：没有触摸，1：左边，2：右边，3：上边，4：下边
	private int touchPosition = 0 ;
	private final int mFrameColor;
	private final int mMaskColor;
	private Rect m_Box;
	private Point screenResolution;
	private int screen_height;
	private int screen_width;
	private long doubleClickTime = 0L;
	private Paint maskPaint = null ;
	private Paint choosedPaint = null ;
	private Paint textPaint = null ;
	private String tips = null ;
	private int textX;
	private int textY;
	private static final int textSize = 13 ;
	
	public CameraMask(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mCxt = paramContext;
		this.m_Box = new Rect();
		this.mMaskColor = getResources().getColor(
				R.color.transparent_background);
		this.mFrameColor = getResources().getColor(R.color.azure);
		Display localDisplay = ((WindowManager) mCxt.getSystemService("window")).getDefaultDisplay();
		this.screenResolution = new Point(localDisplay.getWidth(), localDisplay.getHeight()) ;
		
		this.screen_width = this.screenResolution.x;
		this.screen_height = this.screenResolution.y;
		
		this.frameLeftMin = this.screenResolution.x >> 2 ;
		this.frameTopMin = -18 + (this.screenResolution.y >> 1) ;
		this.frameRightMin = 3 * (this.screenResolution.x >> 2) ;
		this.frameBottomMin = 18 + (this.screenResolution.y >> 1) ;
		
		if( ! readCameraParams()){
			this.frameLeftMove = this.frameLeftMin ;
			this.frameRightMove = this.frameRightMin ;
			this.frameTopMove = this.frameTopMin ;
			this.frameBottomMove = this.frameBottomMin;
		}
		
		maskPaint = new Paint();
		maskPaint.setColor(this.mMaskColor);
		choosedPaint = new Paint();
		choosedPaint.setColor(Color.BLUE) ;
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		// 设置画笔0xAF透明红色
		textPaint.setARGB(0xAF, 0xFF, 0x45, 0);
		textPaint.setTextSize(textSize);
		
		tips = mCxt.getResources().getString(R.string.double_click_remove) ; 
		textX = (this.screen_width - textSize * tips.length()) / 2 ;
		textY = (this.screen_height - textSize * tips.length()) / 2 ;
				
		this.mFrame = getFrameRect();
		
 	}

	protected void onDraw(Canvas paramCanvas) {
		super.onDraw(paramCanvas);
		if (this.mFrame != null && visiable){
			drawFrame(this.mFrame, paramCanvas);
		} 
	}
	
	//绘制矩形框，周围4个区域作为蒙版
	public void drawFrame(Rect paramRect, Canvas paramCanvas ) {
		
		//landscape方式下，矩形框的上方的矩形蒙版
		this.m_Box.set(0, 0, this.screen_width, paramRect.top);
		paramCanvas.drawRect(this.m_Box, maskPaint);
		//landscape方式下，矩形框的左方的矩形蒙版
		this.m_Box.set(0, paramRect.top, paramRect.left, 1 + paramRect.bottom);
		paramCanvas.drawRect(this.m_Box, maskPaint);
		//landscape方式下，矩形框的右方的矩形蒙版
		this.m_Box.set(1 + paramRect.right, paramRect.top, this.screen_width, 1 + paramRect.bottom);
		paramCanvas.drawRect(this.m_Box, maskPaint);
		//landscape方式下，矩形框的下方的矩形蒙版
		this.m_Box.set(0, 1 + paramRect.bottom, this.screen_width, this.screen_height);
		paramCanvas.drawRect(this.m_Box, maskPaint);
		
		maskPaint.setColor(this.mFrameColor);
		//landscape方式下，矩形框上边框
		this.m_Box.set(paramRect.left, paramRect.top, 1 + paramRect.right, 2 + paramRect.top);
		if(touchPosition == 3){
			paramCanvas.drawRect(this.m_Box, choosedPaint);
		}else {
			paramCanvas.drawRect(this.m_Box, maskPaint);
		}
		
		//landscape方式下，矩形框下边框
		this.m_Box.set(paramRect.left, -1 + paramRect.bottom, 1 + paramRect.right, 1 + paramRect.bottom);
		if(touchPosition == 4){
			paramCanvas.drawRect(this.m_Box, choosedPaint);
		}else {
			paramCanvas.drawRect(this.m_Box, maskPaint);
		}
		
		//landscape方式下，矩形框左边框
		this.m_Box.set(paramRect.left, 2 + paramRect.top, 2 + paramRect.left, -1 + paramRect.bottom);
		if(touchPosition == 1){
			paramCanvas.drawRect(this.m_Box, choosedPaint);
		}else {
			paramCanvas.drawRect(this.m_Box, maskPaint);
		}
		
		//landscape方式下，矩形框右边框
		this.m_Box.set(-1 + paramRect.right, paramRect.top, 1 + paramRect.right, -1 + paramRect.bottom);
		if(touchPosition == 2){
			paramCanvas.drawRect(this.m_Box, choosedPaint);
		}else {
			paramCanvas.drawRect(this.m_Box, maskPaint);
		}
		//恢复画笔颜色
		maskPaint.setColor(this.mMaskColor);
		
		paramCanvas.drawText(tips, textX , textY, textPaint);
	}

	public Rect getFrameRect() {
		this.mFrame = new Rect(frameLeftMove, frameTopMove, frameRightMove, frameBottomMove);
		return this.mFrame;
	}

	@Override 
	public boolean onTouchEvent(MotionEvent event) { 
		float x = event.getRawX(); 
		float y = event.getRawY() ; 
//		Log.i("yaogd", "currRawX" + x + ",currRawY" + y); 
		// 除了右上角的拍照按钮外才是可触摸区域,暂时的尺寸写死。
		switch (event.getAction()) { 
			case MotionEvent.ACTION_DOWN:  
				//根据触摸点的位置，让左边或右边的框改变颜色，提示用户可以水平拖动。
				if(x < frameLeftMove + 10  && x >= frameLeftMove - 10 && y > frameTopMove && y <= frameBottomMove)
					touchPosition = 1 ;
				else if(x < frameRightMove + 10  && x >= frameRightMove - 10 && y > frameTopMove && y <= frameBottomMove)
					touchPosition = 2 ;
				else if(x > frameLeftMove && x <= frameRightMove && y <= frameTopMove + 10 && y > frameTopMove - 10)
					touchPosition = 3 ;
				else if(x > frameLeftMove && x <= frameRightMove && y <= frameBottomMove + 10 && y > frameBottomMove - 10)
					touchPosition = 4 ;
				else 
					touchPosition = 0 ;
				this.invalidate() ;
				startX = x ;
				startY = y ;
				break; 
			case MotionEvent.ACTION_MOVE: 
				//标志是move
				doubleClickTime = -1 ;
				//在左侧
				if(touchPosition == 1){
					if(x > startX){
						frameLeftMove += 1 ;
					}else{
						frameLeftMove -= 1 ;
					}
					if (frameLeftMove <= 0){
						frameLeftMove = 0 ;
					} else if(frameLeftMove >= frameLeftMin){
						frameLeftMove = frameLeftMin ;
					}
				}
				//在右侧
				else if(touchPosition == 2){
					if(x > startX){
						frameRightMove += 1 ;
					}else{
						frameRightMove -= 1 ;
					}
					if (frameRightMove <= frameRightMin){
						frameRightMove = frameRightMin ;
					} else if(frameRightMove >= screenResolution.x){
						frameRightMove = screenResolution.x ;
					}
				}
				
				//在上边
				else if(touchPosition == 3){
					if(y > startY){
						frameTopMove += 1 ;
					}else{
						frameTopMove -= 1 ;
					}
					if (frameTopMove >= frameTopMin){
						frameTopMove = frameTopMin ;
					} else if(frameTopMove <= 0){
						frameTopMove = 0 ;
					}
				}
				//在下边
				else if(touchPosition == 4){
					if(y > startY){
						frameBottomMove += 1 ;
					}else{
						frameBottomMove -= 1 ;
					}
					if (frameBottomMove <= frameBottomMin){
						frameBottomMove = frameBottomMin ;
					} else if(frameBottomMove >= screenResolution.y){
						frameBottomMove = screenResolution.y ;
					}
				}
				this.mFrame = new Rect(frameLeftMove, frameTopMove, frameRightMove, frameBottomMove);
				this.invalidate() ;
				startY = y ;
				startX = x ;
				break; 
			case MotionEvent.ACTION_UP: 
//				Log.i("yaogd", "ACTION_UP"); 
				
				if(doubleClickTime == 0){
					doubleClickTime = System.currentTimeMillis() ;
				}else if(doubleClickTime == -1){
					doubleClickTime = 0 ;
					//恢复颜色
					touchPosition = 0 ;
					this.invalidate() ;
					startX = 0 ;
					startY = 0 ;
					//写入
					writeCameraParams() ;
				} else if(doubleClickTime != 0 && doubleClickTime != -1){
					doubleClickTime = System.currentTimeMillis() - doubleClickTime ;
//					Log.i("yaogd", "doubleClickTime=" + doubleClickTime); 
					//is double click
					if( doubleClickTime < 800){
						if(visiable){
							visiable = false;
						}else{
							visiable = true ;
						}
						this.invalidate() ;
					}
					//返回初始化
					doubleClickTime = 0 ;
				}
				break; 
		} 
		
		return true; 
	} 
	
	//写入相机参数
	private void writeCameraParams() {
		A.writeIntPreferences(mCxt,"frameLeftMove", frameLeftMove) ;
		A.writeIntPreferences(mCxt,"frameRightMove", frameRightMove) ;
		A.writeIntPreferences(mCxt,"frameTopMove", frameTopMove) ;
		A.writeIntPreferences(mCxt,"frameBottomMove", frameBottomMove) ;
	}
    //读取相机参数
	private boolean readCameraParams() {
		frameLeftMove = A.readIntPreferences(mCxt,"frameLeftMove") ;
		frameRightMove = A.readIntPreferences(mCxt,"frameRightMove") ;
		frameTopMove = A.readIntPreferences(mCxt,"frameTopMove") ;
		frameBottomMove = A.readIntPreferences(mCxt,"frameBottomMove") ;
		
		if(frameLeftMove != 0 && frameRightMove != 0 
				&& frameTopMove != 0 && frameBottomMove != 0){
			return true ;
		}else{
			return false ;
		}
	}

}