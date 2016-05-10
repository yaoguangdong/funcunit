package com.lefu8.mobile.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

public class AlarmExample extends Activity{

	protected void onCreate(android.os.Bundle savedInstanceState) {
		
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE) ;
		PendingIntent sender = PendingIntent.getActivity(this, 0, 
				new Intent(this,AlarmExample.class), PendingIntent.FLAG_CANCEL_CURRENT) ;
		
		//10秒后执行一次，执行sender
		am.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, sender) ;
		//每隔10秒重复执行一次
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 10000, sender) ;
		
	};
}
