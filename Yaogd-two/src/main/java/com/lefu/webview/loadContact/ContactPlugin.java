package com.lefu.webview.loadContact;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.yaogd.R;

public class ContactPlugin {
	private WebView webView;
	private ContactService contactService = new ContactService();;
	private Handler handler;
	
	public ContactPlugin(Context context,Handler handler) {
		super();
		this.handler = handler;
		webView = (WebView)((Activity)context).findViewById(R.id.WebView01);
	}
	//方法二。使用异步加载
	@JavascriptInterface
	public void show(){
		handler.post(new Runnable(){

			@Override
			public void run() {
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
					
					//迄今为止我遇到的最不可想象的BUG，居然错误提示webview对象是null（空指针异常）
					//index.html中javaScript中方法名原来的也叫show，这里又这样写
					//webView.loadUrl("javascript:show('"+ json +"')");
					
					webView.loadUrl("javascript:init('"+ json +"')");
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    	
			}
			
		});
	}
	
}
