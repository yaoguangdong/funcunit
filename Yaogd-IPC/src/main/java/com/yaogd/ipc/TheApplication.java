package com.yaogd.ipc;

import android.app.Application;
import android.content.Context;

/**
 * Created by yaoguangdong on 2016/5/11.
 */
public class TheApplication extends Application{

    public static Context context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

}
