package com.yaogd.hook;

import android.app.Application;

import com.yaogd.hook.utils.HookUtil;

/**
 * Description:
 * Created by yaoguangdong on 2017/11/22.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        HookUtil.hookIActivityManager();
    }
}
