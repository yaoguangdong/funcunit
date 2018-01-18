package com.yaogd.ipc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.yaogd.ipc.SharePreferenceNofity.OneSharedPreferenceChangeListener;
import com.yaogd.ipc.asyncNotify.OneContentObserver;
import com.yaogd.lib.A;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		A.v(MainActivity.class, "onCreate");
//		//测试FLAG_ACTIVITY_FORWARD_RESULT的使用：MainActivity---startActivityForResult(SecondActivity---startActivity(ThirdActivity
//		startActivityForResult(new Intent(this, SecondActivity.class), 0x10);
		
		//测试添加系统窗口
		Button btn = new Button(getApplicationContext());
		btn.setText("Hello window");
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				A.i("clicked...");
			}
		});
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				150,
				100,
				WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING,
				PixelFormat.RGB_888);
		lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
		lp.setTitle("title");
		lp.token = new Binder();
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService("window");
		btn.setTag("123");
		wm.addView(btn, lp);
		
		super.onCreate(savedInstanceState);
		
		//测试ContentObserver
		Uri uri = Uri.parse("test://haha");
		getContentResolver().registerContentObserver(uri, true, new OneContentObserver());
		//调用notifyChange后，会通知到OneContentObserver
		getContentResolver().notifyChange(uri, null);
		
		//测试文件系统更改的通知
		
		SharedPreferences sp = getSharedPreferences("ad_sp", Context.MODE_PRIVATE);
		
		sp.registerOnSharedPreferenceChangeListener(new OneSharedPreferenceChangeListener());
		
		sp.edit().putInt("testkey", 100).commit();
		
	}
	
	@Override
	protected void onResume() {
		A.v(MainActivity.class, "onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		A.v(MainActivity.class, "onPause");
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		A.v(MainActivity.class, "onDestroy");
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		A.d(MainActivity.class, "onActivityResult,resultCode:" + resultCode);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
