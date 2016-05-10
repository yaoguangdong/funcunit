package com.lefu8.mobile.service;//package com.lefu8.mobile.service;
//
//import java.util.Date;
//import java.util.List;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
//import com.lefu8.mobile.A;
//import com.lefu8.mobile.AppConfig;
//import com.lefu8.mobile.AppContext;
//import com.lefu8.mobile.client.bean.request.RequestSendReservalDataBean;
//import com.lefu8.mobile.client.bean.response.ResponseSendReservalDataBean;
//import com.lefu8.mobile.client.business.BusinessSendReservalDataImpl;
//import com.lefu8.mobile.db.impl.ReversalDALImpl;
//import com.lefu8.mobile.db.impl.ReversalInfo;
//import com.lefu8.mobile.db.impl.ReversalInfo.ReverseStatus;
//import com.lefu8.mobile.receiver.ReceiverAlarmBoot;
///**
// * 上送冲正失败数据服务
// * @author yaoguangdong
// * 2013-9-17
// */
//public class SendUpService extends Service {
//
//	private List<ReversalInfo> reversalList;
//	
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//	
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		
//	}
//
//	@Override
//	public void onStart(Intent intent, int startId) {
//		super.onStart(intent, startId);
//		////A.i("scanning unreversaled data ... " );
//		//获取最近一次处理冲正失败的任务执行时间
//		long lastestTaskMillise = AppContext.getLastestTaskTime();
//		
//		Date taskNow = new Date();
//		
//		if (lastestTaskMillise == 0L || 
//				taskNow.getTime() - lastestTaskMillise >= AppConfig.SPACINGIN_TERVAL) {
//			//扫描冲正失败的记录，上送if need
//			processFailed() ;
//			//更新任务执行时间
//			lastestTaskMillise = new Date().getTime();
//			AppContext.setLastestTaskTime(this, lastestTaskMillise);
//		}
//		
//		Intent _intent = new Intent(this,ReceiverAlarmBoot.class);
//		_intent.putExtra("Date", lastestTaskMillise);
//		
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, _intent,PendingIntent.FLAG_ONE_SHOT);
//        //设置一个PendingIntent对象，发送广播
//        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
//        //获取AlarmManager对象
//        alarmManager.set(AlarmManager.RTC_WAKEUP, lastestTaskMillise + AppConfig.SPACINGIN_TERVAL, pendingIntent);
//        
//        stopSelf() ;
//	}
//
//	private void processFailed() {
//		int result = -1 ;
//		//执行扫描 , 获得本地冲正记录
//		ReversalDALImpl reversalDALImpl = new ReversalDALImpl(this) ;
//		
//		reversalList = reversalDALImpl.queryReversals(ReverseStatus.DEAD);
//		if(reversalList != null && ! reversalList.isEmpty()){
//			//上送冲正数据
//			RequestSendReservalDataBean requestSendReservalDataBean = new RequestSendReservalDataBean() ;
//	        requestSendReservalDataBean.field5 = AppContext.getPhoneMac() ;
//	        requestSendReservalDataBean.setData(reversalList) ;
//	        BusinessSendReservalDataImpl businessSendReservalDataImpl = new BusinessSendReservalDataImpl() ;
//	        ResponseSendReservalDataBean responseSendReservalDataBean;
//			try {
//				responseSendReservalDataBean = businessSendReservalDataImpl.send(requestSendReservalDataBean);
//				if("00".equals(responseSendReservalDataBean.responseCode)){
//					result = 0 ;
//					////A.i("send up reversal data " + reversalList.size() + " items.");
//				}else{
//					////A.i("send up reversal data error 01");
//					result = 23534645 ;
//				}
//			} catch (Exception e) {
//				////A.e("send up reversal data error 02",e);
//				result = 23534645 ;
//			}
//		}else{
//			////A.i("no unreversaleed data items!") ;
//		}
//		//如果上送成功，删除记录
//		if(result == 0){
//			ReversalDALImpl xxx = new ReversalDALImpl(this) ;
//			for(ReversalInfo reversalInfo : reversalList){
//				xxx.deleteReversal(reversalInfo.batchNo, reversalInfo.systemtTrackNo, 
//						reversalInfo.terminalCode) ;
//			}
//			////A.i("delete unreversaleed data：" + reversalList.size() + "条") ;
//		}else if(result != -1){
//			////A.i("send unreversaleed data failed" ) ;
//		}
//	}
//
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//	}
//	
//}
