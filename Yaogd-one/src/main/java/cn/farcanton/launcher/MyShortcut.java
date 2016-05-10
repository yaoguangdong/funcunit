package cn.farcanton.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * 这个actvtiy接受桌面上的添加快捷方式动作，在快捷方式列表中出现。
 * @author Administrator
 *
 */
public class MyShortcut extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(getIntent().getAction().equals(Intent.ACTION_CREATE_SHORTCUT)){
			Intent _returnIntent = new Intent();
			_returnIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "yaoShortcut");
			_returnIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.EXTRA_SHORTCUT_ICON);
			_returnIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this,LauncherTestmain.class));
			setResult(RESULT_OK, _returnIntent);
			//默认启动窗体，在这里先结束掉。
			finish();
			
		}
		
		
		
		
		
	}

}
