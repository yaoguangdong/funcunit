package com.yaogd.useLocation;

import com.yaogd.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
/**
 * 位置更新是调用广播接收器
 * addProximityAlert是经过纬度34.69372940196991、经度135.51647543907166为中心半径
 * 为1km以内（大阪市北区东天满附近）时，就会调用Receiver
 * @author yaoguangdong
 * 2014-5-1
 */
public class MainWithPendingIntentActivity extends Activity {

//    private static final String LOG_TAG = "UseLocation";

    // LocationManagerの取得
    LocationManager mLocationManager = null;

    PendingIntent pendingIntent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_main);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        int requestCode = 0x432f;

        Intent intent = new Intent(this, LocationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // LocationListenerを登録
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0F,
                pendingIntent);

        // 緯度 34.69372940196991
        // 経度 135.51647543907166
        // 範囲 1000m
        // 期限 無期限
        mLocationManager.addProximityAlert(34.69372940196991, 135.51647543907166, 1000, -1,
                pendingIntent);
    }

    @Override
    public void onPause() {
        super.onPause();

        // mLocationManager.removeUpdates(pendingIntent);
        // mLocationManager.removeProximityAlert(pendingIntent);
    }
}
