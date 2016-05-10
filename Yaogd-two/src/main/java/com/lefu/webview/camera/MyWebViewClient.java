package com.lefu.webview.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * 设置处理JavaScript的引擎
 * WebViewClient主要帮助WebView处理各种通知、请求事件的
 * @author: yaoguangdong
 * @data: 2014-1-24
 */
public class MyWebViewClient extends WebViewClient{

	private Context cxt ;
	
	public MyWebViewClient(Context cxt) {
		this.cxt = cxt ;
	}
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.startsWith("tel:"))
	    {
	      Intent intent = new Intent("android.intent.action.DIAL", Uri.parse(url));
	      cxt.startActivity(intent);
	    }
		if (url.contains("target=_blank")) {
			Intent intent = new Intent("android.intent.action.VIEW",
					Uri.parse(url));
			cxt.startActivity(intent);
		} else {
			view.loadUrl(url);
		}
		//其中baseUrl与historyUrl配合，HtmlData，是html的String,好处是可以动态替换html中的某些占位符。
		//view.loadDataWithBaseURL(baseUrl, HtmlData, "text/html", "utf-8", historyUrl);
		return true;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
	}

	@Override
	public void doUpdateVisitedHistory(WebView view, String url,
			boolean isReload) {
		super.doUpdateVisitedHistory(view, url, isReload);
	}

	@Override
	public void onFormResubmission(WebView view, Message dontResend,
			Message resend) {
		super.onFormResubmission(view, dontResend, resend);
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
	}

	@Override
	public void onReceivedHttpAuthRequest(WebView view,
			HttpAuthHandler handler, String host, String realm) {
		super.onReceivedHttpAuthRequest(view, handler, host, realm);
	}

	@Override
	public void onReceivedLoginRequest(WebView view, String realm,
			String account, String args) {
		super.onReceivedLoginRequest(view, realm, account, args);
	}

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		super.onReceivedSslError(view, handler, error);
	}

	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {
		super.onScaleChanged(view, oldScale, newScale);
	}

	@Override
	public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
		super.onUnhandledKeyEvent(view, event);
	}

	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view,
			String url) {
		return super.shouldInterceptRequest(view, url);
	}

	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		return super.shouldOverrideKeyEvent(view, event);
	}
}
