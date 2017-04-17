package com.yaogd.lib;

import android.content.Context;

/**
 * Description:
 * Created by yaoguangdong on 2017/4/17.
 */
public class CommonUtils {

    /**
     * 检查网络状态
     * @param context 上下文环境
     */
    public static boolean isNetAvailable(Context context) {
        android.net.ConnectivityManager cManager = (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

}
