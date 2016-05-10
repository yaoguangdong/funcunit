package cn.farcanton.preference;

import cn.farcanton.R;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.setting);
		
		Preference pref = findPreference("silentMode");
		
		pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				Toast.makeText(SettingActivity.this, arg0.getTitle(), 1000).show();
				
				return false;
			}
		});
		
	}

}
