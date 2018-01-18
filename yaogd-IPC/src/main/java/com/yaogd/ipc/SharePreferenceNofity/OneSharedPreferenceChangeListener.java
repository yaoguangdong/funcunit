package com.yaogd.ipc.SharePreferenceNofity;

import com.yaogd.lib.A;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class OneSharedPreferenceChangeListener implements OnSharedPreferenceChangeListener {

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		A.i("SharedPreferences key changed");
	}

}
