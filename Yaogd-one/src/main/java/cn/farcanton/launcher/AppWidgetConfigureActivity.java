package cn.farcanton.launcher;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import cn.farcanton.R;
import cn.farcanton.preference.SettingActivity;

public class AppWidgetConfigureActivity extends Activity{

	int appWidgetId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_widget_configure);
		
		appWidgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
		
		Button btn = (Button)findViewById(R.id.widget_btn_conf);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//更新
				AppWidgetManager appM = AppWidgetManager.getInstance(AppWidgetConfigureActivity.this);
				RemoteViews remoteView = new RemoteViews(getPackageName(),R.layout.layout_widget_provider);
				//得到输入值
				EditText editText = (EditText)findViewById(R.id.widget_conf_editText);
				remoteView.setTextViewText(R.id.widget_btn10, editText.getText());
				remoteView.setOnClickPendingIntent(R.id.widget_btn10, PendingIntent.getActivity(AppWidgetConfigureActivity.this, 0, 
						new Intent(AppWidgetConfigureActivity.this,SettingActivity.class), 0));
				
				appM.updateAppWidget(appWidgetId, remoteView);
				
				//返回
				Intent _returnIntent = new Intent();
				_returnIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
				
				setResult(RESULT_OK,_returnIntent);
				finish();
			}
		});
		
	}


	
}
