package cn.farcanton.floatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import cn.farcanton.R;

public class WindowLike extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 先去除应用程序标题栏 注意：一定要在setContentView之前
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
    	setContentView(R.layout.float_activity);
    	
    	 Intent intent = new Intent();
         intent.setAction("android.intent.action.VIEW");
         Uri content_url = Uri.parse("http://www.163.com");
         intent.setData(content_url);
         startActivity(intent);
    }
}