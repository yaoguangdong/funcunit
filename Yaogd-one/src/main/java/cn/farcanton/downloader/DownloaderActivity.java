package cn.farcanton.downloader;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

public class DownloaderActivity extends Activity{

	private long myDownloadReference = 0L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//API 9
		DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse("http://ishare.iask.sina.com.cn/f/16732631.html");
		DownloadManager.Request request = new Request(uri);
		request.setAllowedNetworkTypes(Request.NETWORK_WIFI); //限制在有WIFI的时候才下载
		request.setShowRunningNotification(true) ;
		//进度条的标题
//		request.setTitle("notification title") ;
//		request.setDescription("notification description") ;
		//指定外部存储的任意位置
//		request.setDestinationUri(Uri.fromFile(f)); 
		//指定外部存储的任意位置，线程卸载时被删除。
//	    request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "android.jpg");
		//望被其他的应用共享,被Media Scanner扫描到的文件
//		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "Android_Rock.mp3");
		
		
		
		myDownloadReference = downloadManager.enqueue(request);

		//IntentFilter filter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED); 
		//下载完成
		IntentFilter filter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// EXTRA_DOWNLOAD_ID指向已下载文件的ID
				long reference = intent.getLongExtra(
						DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				if (myDownloadReference == reference) {
					// Do something with downloaded file.
				}
			}
		};
		registerReceiver(receiver, filter);

		
	}
}
