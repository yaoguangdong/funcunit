package com.yaogd.screenon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yaogd.screenon.service.ServiceFloating;

/**
 * Description:
 * Created by yaoguangdong on 2016/7/20.
 */
public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, ServiceFloating.class));
        finish();
    }

}
