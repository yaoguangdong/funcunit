package dalelane.android.mqtt;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class MQTTNotifier extends Activity{
	private StatusUpdateReceiver statusUpdateIntentReceiver;  
	private MQTTMessageReceiver  messageIntentReceiver;  

	@Override  
	public void onCreate(Bundle savedInstanceState)   
	{  
		Intent svc = new Intent(this, MQTTService.class);  
		startService(svc); 
		bindService(new Intent(this, MQTTService.class),   
	            new ServiceConnection() {                                  
	                @SuppressWarnings("unchecked")  
	                @Override  
	                public void onServiceConnected(ComponentName className, final IBinder service)   
	                {  
	                    MQTTService mqttService = ((MQTTService.LocalBinder<MQTTService>)service).getService();                                      
	                    mqttService.rebroadcastReceivedMessages();  
	                    unbindService(this);  
	                }  
	                @Override  
	                public void onServiceDisconnected(ComponentName name) {}
	            },   
	            0); 
	    //...  

	    statusUpdateIntentReceiver = new StatusUpdateReceiver();  
	    IntentFilter intentSFilter = new IntentFilter(MQTTService.MQTT_STATUS_INTENT);  
	    registerReceiver(statusUpdateIntentReceiver, intentSFilter);  

	    messageIntentReceiver = new MQTTMessageReceiver();  
	    IntentFilter intentCFilter = new IntentFilter(MQTTService.MQTT_MSG_RECEIVED_INTENT);  
	    registerReceiver(messageIntentReceiver, intentCFilter);  
	      
	    //...  
	}  

	public class StatusUpdateReceiver extends BroadcastReceiver  
	{  
	    @Override   
	    public void onReceive(Context context, Intent intent)  
	    {  
	        Bundle notificationData = intent.getExtras();  
	        String newStatus = notificationData.getString(MQTTService.MQTT_STATUS_MSG);	    	  

	        //... 
	    }  
	}  
	public class MQTTMessageReceiver extends BroadcastReceiver  
	{  
	    @Override   
	    public void onReceive(Context context, Intent intent)  
	    {  
	        Bundle notificationData = intent.getExtras();  
	        String newTopic = notificationData.getString(MQTTService.MQTT_MSG_RECEIVED_TOPIC);  
	        String newData  = notificationData.getString(MQTTService.MQTT_MSG_RECEIVED_MSG);	    	  

	       // ... 
	    }  
	}  

	
	@Override  
	protected void onDestroy()   
	{  
	    //...  

	    unregisterReceiver(statusUpdateIntentReceiver);  
	    unregisterReceiver(messageIntentReceiver);  

	    //...  
	    Intent svc = new Intent(this, MQTTService.class);  
	    stopService(svc);
	} 
	@Override  
	public void onWindowFocusChanged(boolean hasFocus)   
	{  
	    super.onWindowFocusChanged(hasFocus);  
	    if (hasFocus)  
	    {  
	        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
	        mNotificationManager.cancel(MQTTService.MQTT_NOTIFICATION_UPDATE);  
	    }  
	} 
}
