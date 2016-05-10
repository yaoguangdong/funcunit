package cn.farcanton.intentService;

import cn.farcanton.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FirstActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.intent_service_test) ;
		
		findViewById(R.id.btn19).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstActivity.this,TestIntentService.class);
				
				startService(intent);
			}
		});
	}

}
