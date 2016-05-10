package com.lefu.webview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.yaogd.R;
/*
 * WebView不但可以运行一段HTML代码，还可以与Javascript相互调用。
 * 该例实现从Android中调出个人资料，然后通过Javascript显示出来。
 * 同时，在java代码中也可以直接调用javascript方法，这样就可以互相调取数据了，如
 * WebView.loadUrl("javascript:方法名()");
 * 为了让WebView从Apk文件中加载Assets，Android SDK提供了一个Schema，前缀为
 * file:///android_assets/。WebView遇到这样的Schema就去当前包中的Assets目录中找内容
 * 所以使用了以下代码来加载网页：
 * mWebView.loadUrl("file:///android_asset/PersonalData.html");
 * 
 */
public class WebViewLoadContact extends Activity
{
	private WebView mWebView;
	private PersonalData mPersonalData;
	private Handler mHandler = new Handler();  
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wv_load_contact_main);
		mPersonalData = new PersonalData();
		mWebView = (WebView)this.findViewById(R.id.WebView01);
		//设置支持JavaScript
		mWebView.getSettings().setJavaScriptEnabled(true);
		//把本类的一个实例添加到js的全局对象window中，
        //这样就可以使用window.PersonalData来调用它的方法
		mWebView.addJavascriptInterface(new WebAppInterface(), "PersonalData") ;
		//加载网页
		mWebView.loadUrl("file:///android_asset/PersonalData.html");
	}
	/**
	 * loadDataWithBaseURL的使用，好处在于可以设定，访问失败时的url。
	public void webLoad(){
    	AssetManager am=getAssets();
    	String webcode="";
    	try{
    		InputStream is=am.open("aa.htm");
    		int size=is.available();
    		byte [] buffer=new byte[size];
    		is.read(buffer);
    		webcode=buffer.toString();
    		webcode=new String(buffer,"gb2312");
    		is.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	//loadDataWithBaseURL把webcode中的内容装载到内嵌的浏览器中
    	WebView web=(WebView)findViewById(R.id.webView);
    	web.loadDataWithBaseURL(null, webcode,"text/html","utf-8",null);
    }
	*/

	final class WebAppInterface  {  
		WebAppInterface () {}  
  
        /** 
         * 该方法被浏览器端调用 
         * If you've set your targetSdkVersion to 17 or higher, 
         * you must add the @JavascriptInterface annotation to any method that you want 
         * available to your JavaScript (the method must also be public). 
         * If you do not provide the annotation, the method is not accessible 
         * by your web page when running on Android 4.2 or higher.
         */  
        @JavascriptInterface
        public void clickOnAndroid() {  
            mHandler.post(new Runnable() {  
                public void run() {  
                    //调用js中的onJsAndroid方法  
                    mWebView.loadUrl("javascript:onJsAndroid()");  
                }  
            });  
        }  
        
        //在js脚本中调用得到PersonalData对象
        @JavascriptInterface
    	public PersonalData getPersonalData()
    	{
    		return mPersonalData;
    	}
    }  
	
	//js脚本中调用显示的资料
	class PersonalData
	{
		String  mID;
		String  mName;
		String  mAge;
		String  mBlog;	
		public PersonalData()
		{
			this.mID="123456789";
			this.mName="Android";
			this.mAge="100";
			this.mBlog="http://yarin.javaeye.com";
		}
		public String getID()
		{
			return mID;
		}
		public String getName()
		{
			return mName;
		}
		public String getAge()
		{
			return mAge;
		}
		public String getBlog()
		{
			return mBlog;
		}
	}
}



