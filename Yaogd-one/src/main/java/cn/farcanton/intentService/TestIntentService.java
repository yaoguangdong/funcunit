package cn.farcanton.intentService;

import cn.farcanton.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TestIntentService extends IntentService{

	public static final int NOTIFICATION_ID = 987654320;
	private static Notification mNotification = null;
	private static NotificationManager mManager = null; 
	    
	//必须要有无参的构造方法
	public TestIntentService() {
		super("TestIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mNotification = new Notification(R.drawable.drawable_animation, "有可更新的应用",System.currentTimeMillis());
		mNotification.contentView = new RemoteViews(getApplication().getPackageName(), R.layout.notify_content);
		mNotification.contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, AppDownloadActivity.class), 0);
		
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotification.vibrate = new long[] { 100, 250, 100, 500};
		mManager.notify(NOTIFICATION_ID, mNotification);
		
		Log.i("yaogd", "intentService start") ;
		
//		try {
//			Toast.makeText(this, "没有最新的app，2秒后启动第二个界面", 1000).show() ;
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setClass(this, SecondActivity.class) ;
//		startActivity(intent) ;
	}

	@Override
	public void onCreate() {
		//Toast.makeText(this, "有最新的app，任务栏上启动foreGroundService", 1000).show() ;
		
//		Notification foreGroundNoti = new Notification(R.drawable.cloud,null,System.currentTimeMillis());
//		// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
//		foreGroundNoti.flags |= Notification.FLAG_ONGOING_EVENT;
//		// 点击后自动清除Notification
//		foreGroundNoti.flags |= Notification.FLAG_AUTO_CANCEL;
//		foreGroundNoti.flags |= Notification.FLAG_SHOW_LIGHTS;
//		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 
//				0, new Intent(getApplicationContext(),AppDownloadActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
//		foreGroundNoti.setLatestEventInfo(getApplicationContext(), 
//				getResources().getString(R.string.content_title), 
//				getResources().getString(R.string.content_text), pendingIntent); 
//		
//		startForeground(NOTIFICATION_ID, foreGroundNoti);
		super.onCreate();
	}
	

}
