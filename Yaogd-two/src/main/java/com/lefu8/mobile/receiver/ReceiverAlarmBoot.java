package com.lefu8.mobile.receiver;//package com.lefu8.mobile.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import com.lefu8.mobile.service.SendUpService;
///**
// * 接收alarm和bootStart的广播
// * yaoguangdong
// */
//public class ReceiverAlarmBoot  extends BroadcastReceiver {
//	
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		
////		////A.i("扫描冲正失败数据  " + Tools.formatDate(new Date(intent.getLongExtra("Date",0)), "yyyy-MM-dd HH:mm:ss"));
//		
//		Intent _Intent = new Intent(context, SendUpService.class);
//        context.startService(_Intent);
//	}
//
//}
