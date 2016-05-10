package com.yaogd.useLocation;

import com.yaogd.R;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainWithGetLastKnownLocationActivity extends Activity {

//    private static final String LOG_TAG = "UseLocation";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_main);

        // LocationManagerの取得
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 最後に取得された位置情報を取得
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // 位置情報を表示
        displayLocation(loc);
    }

    /**
     * 位置情報の表示
     *
     * @param loc
     */
    private void displayLocation(Location loc) {

        // 位置情報が一度も取得されていない場合
        if (loc == null) {
            ((TextView) findViewById(R.id.main_tv_latitude)).setText("latitude = N/A");
            ((TextView) findViewById(R.id.main_tv_longitude)).setText("longitude = N/A");
            ((TextView) findViewById(R.id.main_tv_altitude)).setText("altitude = N/A");
            return;
        }

        ((TextView) findViewById(R.id.main_tv_latitude)).setText("latitude = " + loc.getLatitude());
        ((TextView) findViewById(R.id.main_tv_longitude)).setText("longitude = "
                + loc.getLongitude());
        ((TextView) findViewById(R.id.main_tv_altitude)).setText("altitude = " + loc.getAltitude());
        ((TextView) findViewById(R.id.main_tv_supprimental_info)).setText("Provider = "
                + loc.getProvider() + "\n" + "Time = " + loc.getTime() + "\n" + "Accuracy = "
                + loc.getAccuracy() + "\n" + "Speed = " + loc.getSpeed() + "\n" + "Bearing = "
                + loc.getBearing() + "\n");

    }
}
