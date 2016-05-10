package com.lefu.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yaogd.R;
/*
 * 基于WebKit的浏览器实现
 * 简介：
 * WebKit的前身是KED小组的KHTML。后来Apple将其发扬光大，未来云端计算都会在终端执行。
 * Javascript弹出框有如下三种： 
 *  alert(); 
 *  window.confirm("Are you srue?");  
 *  window.prompt("Please input some word";,"this is text");  
 *  WebChromeClient 中对三种dialog进行了捕捉，但不幸的是，并没有回调函数可以使用，
 *  或者说不能获得用户是点击“OK”还是“CANCEL”的操作结果。
 */
public class WebViewDialog extends Activity
{
	private final String DEBUG_TAG	= "Activity01";
	private Button		mButton;
	private EditText	mEditText;
	private WebView		mWebView;
	
	/** SetJavaScriptEnabled,会引入XSS漏洞到你的应用程序。要注意。*/
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wv_dialog_main);
		mButton = (Button) findViewById(R.id.Button01);
		mEditText = (EditText) findViewById(R.id.EditText01);
		mWebView = (WebView) findViewById(R.id.WebView01);
		//设置支持JavaScript脚本
		WebSettings webSettings = mWebView.getSettings();  
		webSettings.setJavaScriptEnabled(true);
		//设置可以访问文件
		webSettings.setAllowFileAccess(true);
		
		webSettings.setSavePassword(false);  
        webSettings.setSaveFormData(false);  
		//设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		//设置WebViewClient
		mWebView.setWebViewClient(new WebViewClient()
		{   
		    public boolean shouldOverrideUrlLoading(WebView view, String url) 
		    {   
		        //setJavaScriptEnabled(true);为安全考虑，本站之外的网页链接，使用手机内置浏览器加载。
		        if (Uri.parse(url).getHost().equals("www.example.com")) {
		            // This is my web site, so do not override; let my WebView load the page
		        	view.loadUrl(url); 
		            return false;
		        }
		        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
		        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		        startActivity(intent);
		        return true;
		    }  
			@Override
			public void onPageFinished(WebView view, String url) 
			{
				super.onPageFinished(view, url);
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) 
			{
				super.onPageStarted(view, url, favicon);
			}
		});
		//设置WebChromeClient
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			//处理javascript中的alert
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) 
			{
				//构建一个Builder来显示网页中的对话框
				Builder builder = new Builder(WebViewDialog.this);
				builder.setTitle("提示对话框");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								//点击确定按钮之后,继续执行网页中的操作
								result.confirm();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			};
			@Override
			//处理javascript中的confirm
			public boolean onJsConfirm(WebView view, String url, String message,
					final JsResult result) 
			{
				Builder builder = new Builder(WebViewDialog.this);
				builder.setTitle("带选择的对话框");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result.confirm();
							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result.cancel();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			};
			@Override
			//处理javascript中的prompt
			//message为网页中对话框的提示内容
			//defaultValue在没有输入时，默认显示的内容
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, final JsPromptResult result) {
				//自定义一个带输入的对话框由TextView和EditText构成
				final LayoutInflater factory = LayoutInflater.from(WebViewDialog.this);
				final View dialogview = factory.inflate(R.layout.wv_dialog_prom, null);
				//设置TextView对应网页中的提示信息
				((TextView) dialogview.findViewById(R.id.TextView_PROM)).setText(message);
				//设置EditText对应网页中的输入框
				((EditText) dialogview.findViewById(R.id.EditText_PROM)).setText(defaultValue);
				
				Builder builder = new Builder(WebViewDialog.this);
				builder.setTitle("带输入的对话框");
				builder.setView(dialogview);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								//点击确定之后，取得输入的值，传给网页处理
								String value = ((EditText) dialogview.findViewById(R.id.EditText_PROM)).getText().toString();
								result.confirm(value);
							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								result.cancel();
							}
						});
				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
								result.cancel();
							}
						});
				builder.show();
				return true;
			};
			@Override
			//设置网页加载的进度条
			public void onProgressChanged(WebView view, int newProgress) 
			{
				WebViewDialog.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}

			@Override
			//设置应用程序的标题title
			public void onReceivedTitle(WebView view, String title) 
			{
				WebViewDialog.this.setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
	}
	/**
	 * When your WebView overrides URL loading, 
	 * it automatically accumulates a history of visited web pages. 
	 * You can navigate backward and forward through the history with goBack() and goForward().
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
	    	mWebView.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}
	
}
