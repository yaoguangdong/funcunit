package com.yaogd.useLocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

public class LocationReceiver extends BroadcastReceiver {

//    private static final String LOG_TAG = "UseLocation";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {

            LocationManager lm = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            String message = "Location" + "\n" + " Longitude: " + location.getLongitude() + "\n"
                    + " Latitude: " + location.getLatitude();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        }
        if (intent.hasExtra(LocationManager.KEY_PROXIMITY_ENTERING)) {

            Toast.makeText(context, "指定された領域に入りました！", Toast.LENGTH_LONG).show();

        }

    }

}
