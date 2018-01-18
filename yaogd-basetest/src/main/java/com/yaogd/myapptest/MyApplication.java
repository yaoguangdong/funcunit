package com.yaogd.myapptest;

import android.app.Application;
import android.content.Context;

/**
 * Description:
 * Created by yaoguangdong on 2018/1/11.
 */

public class MyApplication extends Application {

    private static Context sContext;

    public static Context getContext(){
        return sContext;
    }

    @Override
    public void onCreate() {
        sContext = this;
        super.onCreate();
    }
}
