package cn.farcanton.launcher;

import cn.farcanton.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 点击按钮，自动创建桌面快捷方式。
 * @author Administrator
 *
 */
public class LauncherTestmain extends Activity{

	private Button clickBtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_shortcut_btn);
		
		clickBtn = (Button)findViewById(R.id.launcher_createshortcut_btn);
		
		clickBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent _returnIntent = new Intent();
				_returnIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
				_returnIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "AutoCreateShortcut");
				_returnIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(LauncherTestmain.this, R.drawable.ic_launcher));
				_returnIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(LauncherTestmain.this,LauncherTestmain.class));
				sendBroadcast(_returnIntent);
				
			}
		});
		//联系Meta-data,官网上有使用方法。
		ActivityInfo activityInfo;
		try {
			activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
			String metaValue = activityInfo.metaData.getString("cn.farcanton.launcher.LauncherTestmainxxxxx");
			Toast.makeText(this, metaValue, 3000).show();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

}
