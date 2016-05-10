package com.lefu8.mobile.service;//package com.lefu8.mobile.service;
//
//import java.util.Date;
//
//import android.app.IntentService;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Build;
//import android.provider.Settings;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.lefu8.mobile.A;
//import com.lefu8.mobile.AppConfig;
//import com.lefu8.mobile.AppContext;
//import com.lefu8.mobile.AppManager;
//import com.lefu8.mobile.client.bean.request.RequestUpdateAppBean;
//import com.lefu8.mobile.client.bean.response.ResponseUpdateAppBean;
//import com.lefu8.mobile.client.business.BusinessUpdateAppImpl;
//import com.lefu8.mobile.client.core.TimeOutException;
//import com.lefu8.mobile.ui.start.AppUpdate;
//import com.lefu8.mobile.ui.start.LoginScreen;
//import com.lefu8.mobile.utils.Tools;
//
//
//public class AppServiceUpdate extends IntentService {
//
//	public AppServiceUpdate() {
//		super("AppServiceUpdate");
//	}
//
//	@Override
//	protected void onHandleIntent(Intent intent) {
//		//判断缓存是否已登录，来确定是否属于乐富支付的使用者。
//		RequestUpdateAppBean requestUpdateAppBean = fetchHardWareInfo();
//		requestUpdateAppBean.field5 = AppContext.getPhoneMac();
//        requestUpdateAppBean.loginKey = AppContext.getLoginKey() ;
//        requestUpdateAppBean.macKey = AppContext.getMacKey() ;
//        requestUpdateAppBean.operatorCode = AppContext.getOperatorCode() ;
//        
//        BusinessUpdateAppImpl businessUpdateAppImpl = new BusinessUpdateAppImpl() ;
//		try {
//			////A.i("debug-1" + requestUpdateAppBean.toString()) ;
//			ResponseUpdateAppBean responseUpdateAppBean = businessUpdateAppImpl.send(requestUpdateAppBean);
//			////A.i("debug-2" + responseUpdateAppBean.toString()) ;
//			if("00".equals(responseUpdateAppBean.responseCode)){
//	        	//1.在服务端返回非强制更新时，需要做处理。
//	        	if( ! ResponseUpdateAppBean.UPDATE_FORCE.equals(responseUpdateAppBean.updateType)){
//	        		loginProcess(intent,responseUpdateAppBean) ;
//	        	}
//	        	if(ResponseUpdateAppBean.UPDATE_FORCE.equals(responseUpdateAppBean.updateType)){
//	        		updateProcess(intent,true,responseUpdateAppBean.url) ;
//	        	}else if(ResponseUpdateAppBean.UPDATE_OPTIONAL.equals(responseUpdateAppBean.updateType)){
//	        		updateProcess(intent,false,responseUpdateAppBean.url) ;
//	        	}else if(ResponseUpdateAppBean.UPDATE_NONE.equals(responseUpdateAppBean.updateType)){
//	        		////A.i("no update,time:"+Tools.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")) ;
//	        	}else if(ResponseUpdateAppBean.UPDATE_UNRECONGNIZED_OS.equals(responseUpdateAppBean.updateType)){
//	        		////A.i("unrecongnized phone OS,time:"+Tools.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")) ;
//	        	}
//	        }
//		} catch (TimeOutException e) {
//			////A.i("debug-3,TimeOutException") ;
//		} catch (Exception e) {
//			////A.e("debug-3",e) ;
//		}
//		
//	}
//	
//	/**
//	 * 登录过期处理
//	 * 2.如果手机缓存已登录状态，但是服务端返回非登录状态，则手机端要先清除旧的登录信息，
//     * 跳到登录页面并提示用户“登录过期，请重新登录！”。
//	 */
//	private void loginProcess(Intent intent, ResponseUpdateAppBean responseUpdateAppBean) {
//		
//		if (AppContext.isLoginStatus() && 
//				ResponseUpdateAppBean.UNLOGIN.equals(responseUpdateAppBean.loginStatus) ){
//			//在appStart中判断是否需要重新登录。因为本service和appStart是异步执行
//			AppContext.needRelogin = true ;
////			////A.i( "login overdue !") ;
//			resetMsg() ;
//			//如果本服务过慢了，出现这两个界面时需要先结束掉。
//			if(AppManager.getInstance().findActivity(AppConfig.ACTIVITY_NAVIGATION) != null ||
//					AppManager.getInstance().findActivity(AppConfig.ACTIVITY_INTRODUCTION) != null){
//				AppManager.getInstance().killActivity(AppConfig.ACTIVITY_NAVIGATION);
//				AppManager.getInstance().killActivity(AppConfig.ACTIVITY_INTRODUCTION);
//			}
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setClass(this, LoginScreen.class) ;
//			AppContext.isFromRelogin = true ;
//			startActivity(intent) ;
//			
//		}
//		
//	}
//
//	/**
//	 * 用户登出后，重置信息
//	 */
//	private void resetMsg() {
//		SharedPreferences preference = getSharedPreferences(AppConfig.CONFIG_TAG, Context.MODE_PRIVATE);
//		SharedPreferences.Editor editor = preference.edit();
//		//登出后，清除一些本地信息
//		editor.putBoolean("loginStatus", false);
//		editor.putString("operatorCode", "");
//		editor.putString("loginKey", "");
//		editor.putString("macKey", "");
//		editor.putString("merchantName", "");
//		editor.putString("terminalCode", "");
//		editor.putString("storeCode", "");
//		editor.commit();
//	}
//
//	/**
//	 * 更新状况处理
//	 */
//	private void updateProcess(Intent intent,boolean isForce,String URL){
//		//暂停3秒后，启动更新结果页面,以保证在其他页面（产品页、登录页、工作页）之上
//		try {
//			Thread.sleep(3000) ;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		intent.putExtra("URL", URL) ;
//		intent.putExtra("isForce", isForce);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setClass(this, AppUpdate.class) ;
//		startActivity(intent) ;
//		
//		//停止更新服务
//		stopSelf();
//	}
//	
//	@Override
//	public void onCreate() {
//		
//		super.onCreate();
//	}
//	/**
//	 * 封装硬件信息
//	 */
//	private RequestUpdateAppBean fetchHardWareInfo(){
//		RequestUpdateAppBean requestUpdateAppBean = new RequestUpdateAppBean();
//		
//		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE); 
//		String deviceID = tm.getDeviceId();
//		if(TextUtils.isEmpty(deviceID)){
//			deviceID = Settings.System.getString(getContentResolver(), "android_id") ;
//		}
//		if(AppContext.isLoginStatus()){
//			requestUpdateAppBean.setData(
//					loadVersion(), deviceID, Build.MODEL, 
//					AppContext.getDisplayWidth() + "x" + AppContext.getDisplayHeight(), 
//					Build.VERSION.RELEASE, 
//					getProvidersName(tm.getSubscriberId()), 
//					getNetType()) ;
//		}else{
//			//没有登录则以此格式上传
//			requestUpdateAppBean.setData(
//					loadVersion(), null, null, null, null, 
//					null, null) ;
//		}
//		return requestUpdateAppBean;
//	}
//	
//	/**
//	 * 加载本地应用的版本
//	 * @return
//	 */
//	private String loadVersion(){
//		String version = "NULL" ;
//		try {
//			PackageManager manager = this.getPackageManager();
//			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
//			version = info.versionName;
//		} catch (Exception e) {
//			 e.printStackTrace();
//			 Log.e("yaogd", e.getMessage());  
//		}
//        return version ;
//	}
//	/**
//	 * 获取网络类型
//	 * @return
//	 */
//	private String getNetType(){
//		String result = "NULL" ;
//		
//		ConnectivityManager connectMgr = (ConnectivityManager) this
//		        .getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo info = connectMgr.getActiveNetworkInfo();
//		
//		if(info == null){
//			Log.e("yaogd", "网络没有连接");
//		}else if(info.getType() == ConnectivityManager.TYPE_WIFI){
//			result = "WIFI" ;
//		}else if(info.getType() == ConnectivityManager.TYPE_MOBILE){
//			result = "GPRS" ;
//		}
//		return result; 
//	}
//	/** 
//     * Role:Telecom service providers获取手机服务商信息  
//     * 需要加入权限<uses-permission 
//     * android:name="android.permission.READ_PHONE_STATE"/> 
//     */  
//    public String getProvidersName(String imsi) {  
//        String ProvidersName = null;   
//        if(imsi == null)
//   		 	return ProvidersName;
//        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
//        // 系统内部使用代号：01移动、02联通、03电信、04其他。
//        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {  
//            ProvidersName = "01";  
//        } else if (imsi.startsWith("46001")) {  
//            ProvidersName = "02";  
//        } else if (imsi.startsWith("46003")) {  
//            ProvidersName = "03";  
//        } else {  
//            ProvidersName = "04";  
//        }  
//        return ProvidersName;  
//    } 
//
//}
