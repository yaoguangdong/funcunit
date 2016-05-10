package com.lefu.webview.loadContact;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

import com.yaogd.R;

public class ShowContactActivity extends Activity {
    private WebView webView;
    private ContactService contactService;
    private Handler handler = new Handler();
    @SuppressLint("JavascriptInterface")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wv_load_contact_main);
        contactService = new ContactService();

        webView = (WebView)findViewById(R.id.WebView01);
        System.out.println(webView==null);
        
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
 
        webView.addJavascriptInterface(new ContactPlugin(this,handler), "itcast");
        webView.loadUrl("file:///android_asset/getContact.html");
        //将来与videoweb结合
       // webview.loadUrl("http://192.168.1.10:8080/videoweb/index.html");
    }
    	////方法一。使用异步加载
    	//为了演示java和js互通可以注释掉以下，换成现在的ContactPlugin方法
		public void getContacts(){
    		List<Contact> contacts = contactService.getContacts();//得到联系人数据
    		try {
				JSONArray array = new JSONArray();
				for(Contact contact : contacts){
					JSONObject item = new JSONObject();
					item.put("id", contact.getId());
					item.put("name", contact.getName());
					item.put("mobile", contact.getMobile());
					array.put(item);
				}
				String json = array.toString();//转成json字符串
				webView.loadUrl("javascript:init('"+ json +"')");
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	
    	public void call(String mobile){
    		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ mobile));
    		startActivity(intent);
    	}
}
