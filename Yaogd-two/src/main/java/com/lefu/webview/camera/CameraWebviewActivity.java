package com.lefu.webview.camera;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.lefu.A;
import com.yaogd.R;

/**
 * @author: yaoguangdong
 * @data: 2014-1-28
 */
public class CameraWebviewActivity extends Activity {
	
	private WebView webView;
	private String fileFullName;// 照相后的照片的全整路径
	private Handler mHandler = new MyHandler();
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wv_camera_main);
		
		webView = (WebView) findViewById(R.id.wv_01);
		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(true);
		webView.setWebViewClient(new MyWebViewClient(this)) ;
		webView.setWebChromeClient(new MyWebChromeClient(this,mHandler)) ;
		webView.addJavascriptInterface(new WebAppInterface(), "LocalDevice") ;
		
		webView.loadUrl("file:///android_asset/time.html");
//		webView.loadUrl("http://chenchi.duapp.com/text/index.html");
		
	}

	private class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					//启动照相线程
					new TakePicture().start();
					break;
				case 1:
					//启动相册线程
					new OpenPhoto().start();
					break;
				case 100:
					//数据上送成功
					Toast.makeText(CameraWebviewActivity.this, "数据上送成功" , Toast.LENGTH_SHORT).show() ;
					break;
				case -99:
					//数据上送失败上送失败
					Bundle data = msg.getData() ;
					String errorMsg = (String) data.get("sendError") ;
					Toast.makeText(CameraWebviewActivity.this, "数据上送失败" + errorMsg, Toast.LENGTH_SHORT).show() ;
					break;
				default:
					break;
			}
		}

	};
	
	
	/**
	 * 打开照相机
	 * @author: yaoguangdong
	 * @data: 2014-1-26
	 */
	private class TakePicture extends Thread{

		@Override
		public void run() {
			
			//调用摄像头拍照，并获得照片路径
			fileFullName = takePhoto();
			super.run();
		}
		
	}
	/**
	 * 打开相册
	 * @author: yaoguangdong
	 * @data: 2014-1-26
	 */
	private class OpenPhoto extends Thread{
		
		@Override
		public void run() {
			//调用相册选择
			Intent intent = new Intent(Intent.ACTION_PICK, null);  
	        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); 
	        startActivityForResult(intent, 201499); 
			super.run();
		}
		
	}
	
	public class WebAppInterface{
		
		@JavascriptInterface
		public boolean upload(String merchantName, String file1) {
			Map<String, String> params = new HashMap<String,String>();
			params.put("param", merchantName) ;
			String [] filePaths = new String[1] ;
			filePaths[0] = file1 ;
			////A.i("filePath from page ：" + file1) ;
			new UploadThread(mHandler,filePaths,params).start() ;
			
			return true;
		}
	}
	
	/**
	 * 调用摄像头的方法
	 */
	public String takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, "TakePhoto");
		// 判断是否有SD卡
		String sdDir = null;
		boolean isSDcardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (isSDcardExist) {
			sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			sdDir = Environment.getRootDirectory().getAbsolutePath();
		}
		// 确定相片保存路径
		String targetDir = sdDir + "/" + "YaogdCarmea";
		File file = new File(targetDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileName = targetDir + "/" + UUID.randomUUID().toString() + ".jpg";
		// 初始化并调用摄像头
		intent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileName)));
		startActivityForResult(intent, 201498);
		
		return fileName ;
	}

	/*
	 * (non-Javadoc)
	 * 判断是否从摄像Activity返回的
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == 201498 && resultCode == 201497) {
		if (requestCode == 201498) {
			webView.loadUrl("javascript:setLocalImage('" + fileFullName + "')");
		} else if(requestCode == 201499){
			 Uri originalUri = data.getData();        //获得图片的uri 
			 //这里开始的第二部分，获取图片的路径：
	         String[] proj = {MediaStore.Images.Media.DATA};
	         Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
	         int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	         if(  cursor.getCount()>0 && cursor.moveToFirst() ){
	        	//最后根据索引值获取图片路径
	        	fileFullName = cursor.getString(column_index);
	         }
	         webView.loadUrl("javascript:setLocalImage('" + fileFullName + "')");    
		} else {
			webView.loadUrl("javascript:setLocalImage('Please take your photo')");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
	        webView.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}

}