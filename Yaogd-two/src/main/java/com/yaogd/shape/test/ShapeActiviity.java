package com.yaogd.shape.test;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.yaogd.R;
/**
 * 
 * @author yaoguangdong
 * 2014-4-29
 */
public class ShapeActiviity extends Activity {

    private Button btn1;  
    
    @Override  
    public void onCreate(Bundle savedInstanceState){  
        super.onCreate(savedInstanceState); 
        
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //4.4以上状态栏的透明效果
//        if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {//4.4
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            
//        }
      //4.4以上状态栏的透明效果
        /**
        <style name="Theme.Timetodo" parent="@android:style/Theme.Holo.Light">
            <!-- translucent system bars -->
            <item name="android:windowTranslucentStatus">true</item>
            <item name="android:windowTranslucentNavigation">true</item>
        </style>
        **/
        
        setContentView(R.layout.shape_test);  
        
        btn1 = (Button)findViewById(R.id.shape_test_btn);
        
        
    }

}
