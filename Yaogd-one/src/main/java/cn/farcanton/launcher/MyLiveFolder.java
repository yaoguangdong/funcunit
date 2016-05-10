package cn.farcanton.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.LiveFolders;

public class MyLiveFolder extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//这些都是参照源码模仿的。
		if(getIntent().getAction().equals(LiveFolders.ACTION_CREATE_LIVE_FOLDER)){
			Intent _returnIntent = new Intent();
			//intentFileter四大属性之一，uri。点击这个LiveFolder出现联系人列表
			_returnIntent.setData(People.CONTENT_URI);
			_returnIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_NAME, "yaoLiveFolder");
			_returnIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_DISPLAY_MODE, LiveFolders.DISPLAY_MODE_GRID);
			//点击图标启动的程序
			//_returnIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_BASE_INTENT, value)
			setResult(RESULT_OK,_returnIntent);
			finish();
		}
		
	}

}
