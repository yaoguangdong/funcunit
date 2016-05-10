package com.yaogd.useLocation;

import com.yaogd.R;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
/**
 * 基于位置信息变化的处理
 * @author yaoguangdong
 * 2014-5-1
 */
public class MainWithLocationListenerActivity extends Activity implements LocationListener {

//    private static final String LOG_TAG = "UseLocation";

    // LocationManagerの取得
    LocationManager mLocationManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_main);

        // LocationManagerの取得
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // LocationListenerを登録
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    /**
     * 位置情報の表示
     *
     * @param loc
     */
    private void displayLocation(Location loc) {

        ((TextView) findViewById(R.id.main_tv_latitude)).setText("latitude = " + loc.getLatitude());
        ((TextView) findViewById(R.id.main_tv_longitude)).setText("longitude = "
                + loc.getLongitude());
        ((TextView) findViewById(R.id.main_tv_altitude)).setText("altitude = " + loc.getAltitude());
        ((TextView) findViewById(R.id.main_tv_supprimental_info)).setText("count = " + count + "\n"
                + "Provider = " + loc.getProvider() + "\n" + "Time = " + loc.getTime() + "\n"
                + "Accuracy = " + loc.getAccuracy() + "\n" + "Speed = " + loc.getSpeed() + "\n"
                + "Bearing = " + loc.getBearing() + "\n");

    }

    int count = 0;

    // 位置情報が変更された時の動作
    @Override
    public void onLocationChanged(Location location) {
        count++;
        // 位置情報を表示
        displayLocation(location);
    }

    // LocationProviderが有効になった時の動作
    @Override
    public void onProviderEnabled(String provider) {
    }

    // LocationProviderが無効になった時の動作
    @Override
    public void onProviderDisabled(String provider) {
    }

    // LocationProviderの状態が変更された時の動作
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
