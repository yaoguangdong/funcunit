package com.yaogd.hook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yaogd.lib.A;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivityForResult(new Intent(this, SecondActivity.class), 0x10);
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
