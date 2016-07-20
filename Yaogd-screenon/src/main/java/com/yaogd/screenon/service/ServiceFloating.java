package com.yaogd.screenon.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yaogd.screenon.R;

public class ServiceFloating extends Service {

	private static final int STATE_KEEP_SCREEN_ON = 8;
	private static final int STATE_CLOSED = 9;
	private static final int STATE_EXIT = 10;

	private WindowManager windowManager;
	private TextView chatHead;

	boolean mHasDoubleClicked = false;
	long lastPressTime;

	private PowerManager.WakeLock wl;

	private int currentState = STATE_KEEP_SCREEN_ON;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "screenon");
		wl.acquire();
		currentState = STATE_KEEP_SCREEN_ON;

		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		chatHead = new TextView(this);

		chatHead.setBackgroundResource(R.drawable.ic_launcher);

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		windowManager.addView(chatHead, params);

		try {
			chatHead.setOnTouchListener(new View.OnTouchListener() {
				private WindowManager.LayoutParams paramsF = params;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						long pressTime = System.currentTimeMillis();
						// If double click...
						if (pressTime - lastPressTime <= 300) {
							ServiceFloating.this.stopSelf();
							mHasDoubleClicked = true;
							return true;
						} else { // If not double click....
							mHasDoubleClicked = false;
						}
						lastPressTime = pressTime;
						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						long now = System.currentTimeMillis();
						if (now - lastPressTime > 200) {
							return true;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						paramsF.x = initialX
								+ (int) (event.getRawX() - initialTouchX);
						paramsF.y = initialY
								+ (int) (event.getRawY() - initialTouchY);
						if(null!=windowManager){
							windowManager.updateViewLayout(chatHead, paramsF);
						}
						break;
					}
					return false;
				}
			});
		} catch (Exception e) {
		}

		chatHead.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(currentState == STATE_KEEP_SCREEN_ON){
					currentState = STATE_CLOSED;
					wl.release();
				}
				if(currentState == STATE_CLOSED){
					currentState = STATE_EXIT;
					ServiceFloating.this.stopSelf();
				}
			}
		});
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null){
			windowManager.removeView(chatHead);
		}
		chatHead = null;
		windowManager = null;
		if(wl.isHeld()){
			wl.release();
			wl = null;
		}
	}

}