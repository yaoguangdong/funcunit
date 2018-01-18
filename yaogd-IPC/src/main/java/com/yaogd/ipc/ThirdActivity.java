package com.yaogd.ipc;

import android.app.Activity;
import android.os.Bundle;

import com.yaogd.lib.A;

public class ThirdActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		A.v(ThirdActivity.class, "onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		A.v(ThirdActivity.class, "onResume");
		
		setResult(0x30);
		finish();
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		A.v(ThirdActivity.class, "onPause");
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		A.v(ThirdActivity.class, "onDestroy");
		super.onDestroy();
	}
	
}
