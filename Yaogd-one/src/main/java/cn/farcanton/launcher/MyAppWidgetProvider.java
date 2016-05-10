package cn.farcanton.launcher;

import cn.farcanton.R;
import cn.farcanton.preference.SettingActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class MyAppWidgetProvider extends AppWidgetProvider{

	//计数器，界面更新数据用
	private static int count= 0;
	//删除一个appwidget时候触发。
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Log.i("yaolog","onDeleted");
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		
		Log.i("yaolog","onDisabled");
	}

	//第一个appwidget添加的时候
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		
		Log.i("yaolog","onEnabled");
	}

	//receiver默认的消息接收重载
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		
		Log.i("yaolog","onReceive");
	}

	//到了时间间隔的时候，appwidget接收到的消息
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		count ++;
		for(int i = 0 ; i < appWidgetIds.length ; i++){
			RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.layout_widget_provider);
			remoteView.setTextViewText(R.id.widget_btn10, "update "+count);
			remoteView.setOnClickPendingIntent(R.id.widget_btn10, PendingIntent.getActivity(context, 0, 
					new Intent(context,SettingActivity.class), 0));
			
			
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteView);
			
		}
		
		Log.i("yaolog","onUpdate");
	}

	
}
