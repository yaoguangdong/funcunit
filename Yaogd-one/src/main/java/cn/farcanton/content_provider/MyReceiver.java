package cn.farcanton.content_provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Toast.makeText(context, 
				"onReceiver和onHandler一样都在主线程中，注意发送消息>5s或接受消息处理>10s，出现ANR",
				Toast.LENGTH_LONG).show();
		Toast.makeText(context, intent.getStringExtra("getContacts"), Toast.LENGTH_LONG).show();
		
	}
	
}
