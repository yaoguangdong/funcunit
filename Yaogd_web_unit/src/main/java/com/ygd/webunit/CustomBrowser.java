package com.ygd.webunit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * 专用于测试静态网页。
 * @author yaoguangdong
 * 2014-8-15
 */
public class CustomBrowser extends Activity implements OnClickListener{

	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.browser_main);
		//step 1
		initViews();
		//step 2
		//测试静态网页
		webView.loadUrl("file:///android_asset/ygd/smartphone/index.html");
		
	}

	private void initViews() {
		((TextView)findViewById(R.id.main_head_title)).setText("Inspire the Next");
		findViewById(R.id.main_head_back).setVisibility(View.VISIBLE) ;
		findViewById(R.id.main_head_back).setOnClickListener(this) ;
		
		webView = (WebView)findViewById(R.id.web_m_page_01) ;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_head_back:
			if (webView.canGoBack()) {
				webView.goBack();
			} 
			break;
		default:
			
			break;
		}
		
	}
	
}
