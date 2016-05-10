package cn.farcanton.hardwareinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import cn.farcanton.R;
/**
 * 通用类
 * 获取手机硬件信息包括：
 * 设备ID、手机型号、操作系统版本、屏幕尺寸、屏幕分辨率、设备IMEI、
 * CPU信息、RAM信息、ROM信息、电池电量、MAC地址、运营商。
 * SDCard的总量、剩余和是否存在
 * @author: yaoguangdong
 * @data: 2014-2-8
 */
public class FetchHardwareInfo extends Activity {

	private TextView mTextView ;
	private StringBuffer outputText ;
	
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0) ;
			//level + % 就是电量。
		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hardware_info);
        
        mTextView = (TextView)findViewById(R.id.outputText) ;
        
        mTextView.setText(fetchHardInfo()) ;
		
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)) ;
    }
    
    /**
	 * 获取屏幕尺寸
	 */
	public Point a(Activity act){
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int widthPixels= dm.widthPixels;//屏幕分辨率
		int heightPixels= dm.heightPixels;//屏幕分辨率
		float density = dm.density;//屏幕密度
		int screenWidth = (int) (widthPixels * density) ;
		int screenHeight = (int) (heightPixels * density) ;
		return new Point(screenWidth,screenHeight) ;
	}
    
    private String fetchHardInfo(){
    	TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE); 
    	outputText = new StringBuffer();
    	outputText.append("deviceID:" + tm.getDeviceId()) ;
    	//获取手机号码,部分手机可以获取，不能获取的为空
    	outputText.append("手机号:" + tm.getLine1Number()) ;
    	//获取IMSI号码
    	outputText.append("唯一的用户ID:" +  tm.getSubscriberId()) ;
    	
    	outputText.append("设备型号:" +  Build.MODEL) ; 
    	outputText.append("操作系统版本:" +  Build.VERSION.SDK) ; 
    	outputText.append("操作系统版本:" +  Build.VERSION.RELEASE) ; 
    	
    	DisplayMetrics displayMetrics = new DisplayMetrics(); 
    	getWindowManager().getDefaultDisplay().getMetrics(displayMetrics); 
    	
    	outputText.append("手机屏幕分辨率:" +  displayMetrics.widthPixels + "x" + displayMetrics.heightPixels) ; 
    	
    	outputText.append("设备IMEI:" + android.provider.Settings.System.getString(getContentResolver(), "android_id"));
    	
		//获取运营商信息
		String providersName = getProvidersName(tm.getSubscriberId());
		outputText.append("运营商：" + providersName) ;
		outputText.append("CPU最大频率：" + getMaxCpuFreq()) ;
		
		outputText.append("CPU信息：" + getCpuInfo()) ;
		
		outputText.append("RAM总大小：" + getTotalMemroy()) ;
		outputText.append("RAM可用大小：" + getAvailMemroy()) ;
		outputText.append("ROM总大小：" + getRomMemroy()[0]) ;
		outputText.append("ROM可用：" + getRomMemroy()[1]) ;
		outputText.append("ROM可用：" + getSDCardMemroy()[0]) ;
		
		outputText.append("电池电量：" + getSDCardMemroy()[0]) ;
		
		outputText.append("MAC地址：" + getMAC()) ;
		
		outputText.append("SDCard存在：" + existSDCard()) ;
		outputText.append("SDCard的总量：" + getSDAllSize() + "MB") ;
		outputText.append("SDCard的剩余：" + getSDFreeSize() + "MB") ;
		
		return outputText.toString();
    }
    /**
     * 判断SDCard是否存在
     */
	private boolean existSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
     * 判断SDCard的剩余空间
     */
	public long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}
	/**
     * 判断SDCard的总量
     */
	public long getSDAllSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

    private String formatSize(long size){
    	String suffex = null ;
    	float fSize = 0 ;
    	if(size >= 1024){
    		suffex = "KB" ;
    		fSize = fSize / 1024 ;
    		
    		if(fSize >= 1024){
        		suffex = "MB" ;
        		fSize = fSize / 1024 ;
        	}
        	if(fSize >= 1024){
        		suffex = "GB" ;
        		fSize = fSize / 1024 ;
        	}
    	} else{
    		fSize = size ;
    	}
    	DecimalFormat df = new DecimalFormat("#0.00") ;
    	
    	return df.format(fSize) + suffex ;
    	
    }
    
    private String getMAC() {
		WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE) ;
		WifiInfo ci = wm.getConnectionInfo() ;
		return ci.getMacAddress() ;
		
	}

	/**
     * 第一行是cpu型号，第二行是频率
     * @return
     */
    private String[] getCpuInfo() {
		String str1 = "proc/cpuinfo" ;
		String str2 = "" ;
		String [] cupInfo = {"",""} ;
		String [] arrayOfString ; 
		try{
			FileReader fr = new FileReader(str1) ;
			BufferedReader br = new BufferedReader(fr, 8192) ;
			str2 = br.readLine() ;
			arrayOfString = str2.split("\\s+") ;
			for(int i = 1; i < arrayOfString.length; i++){
				cupInfo[0] += arrayOfString[i] + "" ;
			}
			str2 = br.readLine() ;
			arrayOfString = str2.split("\\s+") ;
			cupInfo[1] += arrayOfString[2];
			br.close();
		}catch(Exception e){
			
		}
		
		return cupInfo;
	}

	private long[] getSDCardMemroy() {
		long [] sdCardInfo = new long[2];
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			File sdcardDir = Environment.getExternalStorageDirectory() ;
			StatFs sf = new StatFs(sdcardDir.getPath());
			long bSize = sf.getBlockSize() ;
			long bCount = sf.getBlockCount() ;
			long availBlocks = sf.getAvailableBlocks() ;
			
			sdCardInfo [0] = bSize * bCount ;//总大小
			sdCardInfo [1] = bSize * availBlocks ;//剩余大小
			
		}
		
		return sdCardInfo;
	}

	private long getAvailMemroy() {
    	
		ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE) ;
		MemoryInfo mi = new ActivityManager.MemoryInfo() ;
		am.getMemoryInfo(mi) ;
		
		return mi.availMem;
	}

	private String getTotalMemroy() {
    	String str1 = "proc/meminfo" ;
    	String str2 = null ;
    	try{
    		FileReader fr = new FileReader(str1) ;
    		BufferedReader br = new BufferedReader(fr, 8192) ;
    		while((str2 = br.readLine()) != null){
    			Log.i("yaogd", "---" + str2 ) ;
    		}
    	} catch(Exception e){
    		
    	}
		return null;
	}

	/** 
     * Role:Telecom service providers获取手机服务商信息 <BR> 
     * 需要加入权限<uses-permission 
     * android:name="android.permission.READ_PHONE_STATE"/> <BR> 
     * Date:2012-3-12 <BR> 
     * @author CODYY)peijiangping 
     */  
    public String getProvidersName(String imsi) {  
        String ProvidersName = null;   
        if(imsi == null)
   		 return ProvidersName;
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。   
        System.out.println(imsi);  
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {  
            ProvidersName = "中国移动";  
        } else if (imsi.startsWith("46001")) {  
            ProvidersName = "中国联通";  
       } else if (imsi.startsWith("46003")) {  
            ProvidersName = "中国电信";  
        }  
        return ProvidersName;  
    } 
    
    // 获取CPU最大频率（单位KHZ）
    // "/system/bin/cat" 命令行
    // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
   public static String getMaxCpuFreq() {
           String result = "";
           ProcessBuilder cmd;
           try {
                   String[] args = { "/system/bin/cat",
                                   "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
                   cmd = new ProcessBuilder(args);
                   Process process = cmd.start();
                   InputStream in = process.getInputStream();
                   byte[] re = new byte[24];
                   while (in.read(re) != -1) {
                           result = result + new String(re);
                   }
                   in.close();
           } catch (IOException ex) {
                   ex.printStackTrace();
                   result = "N/A";
           }
           return result.trim();
   }
   
   public long[] getRomMemroy() {  
       long[] romInfo = new long[2];  
       //Total rom memory  
       romInfo[0] = getTotalInternalMemorySize();  
  
       //Available rom memory  
       File path = Environment.getDataDirectory();  
       StatFs stat = new StatFs(path.getPath());  
       long blockSize = stat.getBlockSize();  
       long availableBlocks = stat.getAvailableBlocks();  
       romInfo[1] = blockSize * availableBlocks;  
         
       return romInfo;  
   }  
  
   public long getTotalInternalMemorySize() {  
       File path = Environment.getDataDirectory();  
       StatFs stat = new StatFs(path.getPath());  
       long blockSize = stat.getBlockSize();  
       long totalBlocks = stat.getBlockCount();  
       return totalBlocks * blockSize;  
   } 
       
}
