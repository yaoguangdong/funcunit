package com.lefu.webview.fileUpload;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

public class MyWebChromeClient extends WebChromeClient {

	private WebCall webCall ;
	
	public void setWebCall(WebCall webCall){
		this.webCall = webCall ;
	}
	
	// For Android 3.0+
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		webCall.fileChose(uploadMsg);
	}

	// For Android < 3.0
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		openFileChooser(uploadMsg, "");
	}

	// For Android > 4.1.1
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		openFileChooser(uploadMsg, acceptType);
	}
	
	public interface WebCall{
		public void fileChose(ValueCallback<Uri> uploadMsg) ;
	}

}