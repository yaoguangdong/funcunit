package com.yaogd.hook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yaogd.lib.A;

public class SecondActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		A.v(SecondActivity.class, "onCreate");
		
		startActivity(getIntent().setClass(this, ThirdActivity.class).setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT));
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		A.v(SecondActivity.class, "onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		A.v(SecondActivity.class, "onPause");
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		A.v(SecondActivity.class, "onDestroy");
		super.onDestroy();
	}
	
}
