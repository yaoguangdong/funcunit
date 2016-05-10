package com.yaogd.useLocation;

import com.yaogd.R;

import android.app.Activity;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MainWithGpsStatusActivity extends Activity {

    private static final String LOG_TAG = "UseLocation";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_main);

        // LocationManagerの取得
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        lm.addGpsStatusListener(new GpsStatus.Listener() {

            @Override
            public void onGpsStatusChanged(int event) {
                switch (event) {

                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        Log.d(LOG_TAG, "GPS_EVENT_FIRST_FIX");
                        break;

                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        Log.d(LOG_TAG, "GPS_EVENT_SATELLITE_STATUS");
                        break;

                    case GpsStatus.GPS_EVENT_STARTED:
                        Log.d(LOG_TAG, "GPS_EVENT_STARTED");
                        break;

                    case GpsStatus.GPS_EVENT_STOPPED:
                        Log.d(LOG_TAG, "GPS_EVENT_STOPPED");
                }
            }

        });
    }
}
