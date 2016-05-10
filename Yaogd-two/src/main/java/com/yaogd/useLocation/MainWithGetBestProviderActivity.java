package com.yaogd.useLocation;

import com.yaogd.R;

import android.app.Activity;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
/**
 * 根据条件判断最合适的位置提供者
 * @author yaoguangdong
 * 2014-5-1
 */
public class MainWithGetBestProviderActivity extends Activity {

//    private static final String LOG_TAG = "UseLocation";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bestprovider_main);

        // LocationManagerの取得
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);

        String bestProvider = lm.getBestProvider(criteria, true);

        ((TextView) findViewById(R.id.main_tv_bestprovider)).setText("最適なロケーションプロバイダ = "
                + bestProvider);
    }
}
