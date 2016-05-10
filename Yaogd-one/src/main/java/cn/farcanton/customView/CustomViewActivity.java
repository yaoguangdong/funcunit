package cn.farcanton.customView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class CustomViewActivity extends Activity {

	private static final String TAG = "TestActivity" ;
	private CustomBtnView cbv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.custom_btn) ;
//		
//		cbv = (CustomBtnView)findViewById(R.id.custombtn) ;
//		cbv.setOnClickListener(new CustomBtnView.myOnClickListener() {
//			
//			@Override
//			public void myClick() {
//				Log.i(TAG, "customBtnView clicked bizness");
//				
//			}
//		});

//		setContentView(R.layout.custom_layout) ;
		
		View view = new FlectionView(this);
		setContentView(view);
		
		
	}

	
}
