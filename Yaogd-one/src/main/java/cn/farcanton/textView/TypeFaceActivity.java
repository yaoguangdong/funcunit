package cn.farcanton.textView;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;
import cn.farcanton.R;

public class TypeFaceActivity extends Activity {
   
	private TextView mtextView3;
	// MCC，Mobile Country Code，移动国家代码（中国的为460）
	private int MCC ;
	// MNC，Mobile Network Code，移动网络号码（中国移动为0，中国联通为1，中国电信为2）
	private int MNC ;
	// LAC，Location Area Code，位置区域码
	private int LAC ;
	// CID，Cell Identity，基站编号
	private int CID ;
	// 邻区基站总数
	private int baseStationNum ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_view);
        mtextView3 = (TextView)findViewById(R.id.text111) ;
        
        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/CrimsonVermillion.ttf");
        //应用字体
        mtextView3.setTypeface(typeFace); 
        //获取基站信息
        String baseStationInfo = getBaseStationInfo();
        mtextView3.setText("the english type face CrimsonVermillion.ttf\n" + baseStationInfo);
		
    }
    /** 
     * 功能描述：通过手机信号获取基站信息 
     * # 通过TelephonyManager 获取lac:mcc:mnc:cell-id 
     */ 
	private String getBaseStationInfo() {
		
		TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  
        
		int type = mTelephonyManager.getNetworkType();//获取网络类型
		//在中国，网络类型:移动的2G是EGDE，联通的2G为GPRS，电信的2G为CDMA，电信的3G为EVDO
		//NETWORK_TYPE_EVDO_A : 电信3G
		//NETWORK_TYPE_CDMA : 电信2G
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_A || type == TelephonyManager.NETWORK_TYPE_CDMA || type ==TelephonyManager.NETWORK_TYPE_1xRTT)
		{
			//Android 基站分CdmaCellLocation和GsmCellLocation，要根据不同的SIM卡转成不同的对象
			CdmaCellLocation location = (CdmaCellLocation) mTelephonyManager.getCellLocation();
			if(location == null)
				return "";
			CID = location.getBaseStationId();
			LAC = location.getNetworkId();
			// 返回值MCC移动国家代码 + MNC移动网络号码
			String operator = mTelephonyManager.getNetworkOperator();  
			MCC = Integer.parseInt(operator.substring(0, 3));  
			MNC= Integer.parseInt(operator.substring(3));  
        
		}
		//NETWORK_TYPE_EDGE : 移动2G
		//NETWORK_TYPE_GPRS : 联通的2G
		else if(type == TelephonyManager.NETWORK_TYPE_EDGE || type == TelephonyManager.NETWORK_TYPE_GPRS){
			GsmCellLocation location = (GsmCellLocation)mTelephonyManager.getCellLocation();
			if(location == null)
				return "";
			String operator = mTelephonyManager.getNetworkOperator();
			MCC = Integer.parseInt(operator.substring(0,3));
			MNC = Integer.parseInt(operator.substring(3));
			CID = location.getCid();
			LAC = location.getLac();
		}
		// 获取邻区基站信息  
        List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();  
        baseStationNum = infos.size();  

		return getBaseInfo(infos);
	}
	/**
	 * 拼接数据
	 * @return
	 */
	private String getBaseInfo(List<NeighboringCellInfo> infos){
		StringBuffer baseStationInfo = new StringBuffer();
		baseStationInfo.append("MCC:" + MCC) ;
		baseStationInfo.append("\nMNC:" + MNC) ;
		baseStationInfo.append("\nLAC:" + LAC) ;
		baseStationInfo.append("\nCID:" + CID) ;
		baseStationInfo.append("\nMCC:" + MCC) ;
		
		baseStationInfo.append("\n base Station Num:" + baseStationNum) ;
		for (NeighboringCellInfo info : infos) {  
			// 取出当前邻区的LAC 
			baseStationInfo.append("\nother LAC : " + info.getLac()); 
			// 取出当前邻区的CID
			baseStationInfo.append("\nother CID : " + info.getCid());   
			// BSSS，Base station signal strength，基站信号强度
			baseStationInfo.append("\nother BSSS : " + (-113 + 2 * info.getRssi())); 
        }  
		
		return baseStationInfo.toString();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		int currentVersion = android.os.Build.VERSION.SDK_INT;  
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {  
            Intent startMain = new Intent(Intent.ACTION_MAIN);  
            startMain.addCategory(Intent.CATEGORY_HOME);  
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            startActivity(startMain);  
            System.exit(0);  
            
            ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);       
            manager.killBackgroundProcesses(getPackageName());  
           // 在android2.2以后，如果服务在ondestroy里加上了start自己，用kill backgroudprocess通常无法结束自己。
            
        } else {// android2.1  
        	
        	//public void restartPackage (String packageName)
        	//Since: API Level 3
        	//Have the system perform a force stop of everything associated with the given application package.
        	//All processes that share its uid will be killed, all services it has running stopped, 
        	//all activities removed, etc. In addition, a ACTION_PACKAGE_RESTARTED broadcast will be sent, 
        	//so that any of its registered alarms can be stopped, notifications removed, etc. 
        	//官方的这段说明非常关键:its registered alarms can be stopped, notifications removed
        	//所以通常有alarms的或者后台服务的一般不用这种方法。
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);  
            am.restartPackage(getPackageName());  
        } 
	}
	
	
    
}