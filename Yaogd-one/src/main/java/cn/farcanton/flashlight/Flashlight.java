package cn.farcanton.flashlight;

import cn.farcanton.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

public class Flashlight extends Activity {
    private static final float MAX_BRIGHTNESS = 0;//亮度：0~1之间的浮点数。
    private float brightness = 1.0f ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_btn);
        
        ContentResolver cr = getContentResolver();
        if(isAutoBrightness(cr)){
        	stopAutoBrightness(this) ;
        }
        SharedPreferences mSettings = getSharedPreferences("lightness",Activity.MODE_PRIVATE);
        brightness = mSettings.getFloat("lightness", 1.0f) ;
        setBrightness(brightness) ;
        
        getWakeLock() ;

    }
    
    @Override
	protected void onDestroy() {
		SharedPreferences mSettings = getSharedPreferences("lightness",Activity.MODE_PRIVATE);
		Editor editor = mSettings.edit() ;
		editor.putFloat("lightness", brightness) ;
		super.onDestroy();
	}

	/**
     * 判断是否开启了自动亮度调节
     * @param aContext
     * @return
     */
    public static boolean isAutoBrightness(ContentResolver aContentResolver) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }
    /**
     * 取得亮度
     * @return
     */
    private float getBrightness()
    {
    	float brightness = 0;
    	try
    	{
    		ContentResolver cr = getContentResolver();
    		brightness = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
    	}
    	catch(SettingNotFoundException snfe)
    	{
    		brightness = MAX_BRIGHTNESS;
    	}
    	return brightness;
    }
    /**
     * 停止自动亮度调节
     * @param activity
     */
    public static void stopAutoBrightness(Activity activity) {
    	//SCREEN_BRIGHTNESS_MODE_AUTOMATIC是开启自动调节
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    private void setBrightness(float brightness)
    {
    	WindowManager.LayoutParams lp = getWindow().getAttributes();
    	lp.screenBrightness = brightness;
    	getWindow().setAttributes(lp);
    }
    
    private PowerManager.WakeLock getWakeLock()
    {
    	final PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
    	//使用PowerManager.WakeLock来保证程序运行时保持手机屏幕的恒亮。
    	//wakeLock.release();解锁
    	PowerManager.WakeLock w = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "aFlashLight");
    	w.acquire();
    	
    	return w;
    }
    /**
     * 如果要保持屏幕的亮度，就要保存亮度设置状态到系统设置中，
     * 但本软件不是亮度调节软件，
     * 是个手电筒，亮度状态保存在程序偏好中即可。
     * @param resolver
     * @param brightness
     */
    public static void saveBrightness(ContentResolver resolver, int brightness) {
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
        android.provider.Settings.System.putInt(resolver, "screen_brightness",brightness);
        // resolver.registerContentObserver(uri, true, myContentObserver);
        resolver.notifyChange(uri, null);
    }


    
}