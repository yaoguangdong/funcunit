package com.lefu.webview.fileUpload;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.lefu.A;
import com.yaogd.R;

/**
 * 商户整改web版
 * @author handongxu
 * 2015年1月6日
 */
public class UIWebMerchantReform extends Activity implements MyWebChromeClient.WebCall{
	
	public static final String cache = Environment.getExternalStorageDirectory() + "/" ;
	
	private WebView webView;
	private boolean isChosing = false ;
	private ValueCallback<Uri> uploadMsg ;
	
	public static final int JS_CALL_CAMERA = 0x435;
	public static final int JS_CALL_PHOTO = 0x436;
	public static final int PROCESS_PIC = 0x437;
	public static final int PROCESS_PIC_FINISH = 0x438;
	
	private String initFilePath ;
	private String saveFilePath ;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case JS_CALL_CAMERA:
				//启动照相线程
				new TakePicture().start();
				break;
			case JS_CALL_PHOTO:
				// 启动相册线程
				new OpenPhoto().start();
				break;
			case PROCESS_PIC:
				//压缩从照相机选择或者从相册选择的照片
				compressPicture();
				break;
			case PROCESS_PIC_FINISH:
				//压缩完成处理
				Uri uri = Uri.fromFile(new File(saveFilePath));
				uploadMsg.onReceiveValue(uri);
				uploadMsg = null;
				break;
			default:
				break;
			}
		}

	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_reform_web);
		
		initviews();
		loadwebview();
		
	}
	
	private void initviews(){
		 webView = (WebView) findViewById(R.id.merchant_webview);
		
	}
	
	private void loadwebview() {
		
		webView.setWebViewClient(new MyWebViewClient(this));
		MyWebChromeClient chromeClient = new MyWebChromeClient() ;
		chromeClient.setWebCall(this) ;
		webView.setWebChromeClient(chromeClient) ;
	
		webView.loadUrl("http://192.168.13.36:8080/webapp/");
	}

	/**
	 * 调用自定义相机
	 * @author: yaoguangdong
	 * @data: 2014-1-26
	 */
	private class TakePicture extends Thread{
		
		@Override
		public void run() {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, "TakePhoto");
			// 确定相片保存路径
			File dir = new File(cache);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String filePath = cache + System.currentTimeMillis() + ".jpg";
			File file = new File(filePath);
			if ( ! file.exists()) {
				try {
					file.createNewFile() ;
				} catch (IOException e) {
				}
			}
			initFilePath = filePath ;
			// 初始化并调用摄像头
			intent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent, JS_CALL_CAMERA);
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
	        startActivityForResult(intent, JS_CALL_PHOTO); 
		}
		
	}
	
	/**
	 * 压缩处理图片
	 */
	private void compressPicture(){
		new Thread(){
			public void run() {
				A.i("compress currPicPath:" + initFilePath);
				//压缩尺寸
				Bitmap bitmap = PicTools.compressImgResize(initFilePath, 720, 1024);
				//压缩质量
				String savePath = cache + System.currentTimeMillis() + "_c.jpg" ;
				PicTools.compressBmpToFile(bitmap, 50, 200, savePath) ;
				
				saveFilePath = savePath ;
				handler.sendEmptyMessage(PROCESS_PIC_FINISH) ;
			};
		}.start();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if (requestCode == JS_CALL_CAMERA ) {
				 UIHelper.sendMsgToHandler(handler, PROCESS_PIC) ;
			}else if(requestCode == JS_CALL_PHOTO){
				Uri originalUri = data.getData();        //获得图片的uri 
				//这里开始的第二部分，获取图片的路径：
		        String[] proj = {MediaStore.Images.Media.DATA};
		        Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
		        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		        if(  cursor.getCount()>0 && cursor.moveToFirst() ){
		        	//最后根据索引值获取图片路径
		        	initFilePath = cursor.getString(column_index);
		        }
		        UIHelper.sendMsgToHandler(handler, PROCESS_PIC) ;
			}else{
				//nothing
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			webView.clearHistory() ;
			webView.clearView() ;
			finish() ;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void fileChose(ValueCallback<Uri> uploadMsg) {
		if( ! isChosing){
			isChosing = true ;
			this.uploadMsg = uploadMsg ;
			showChoseDialog() ;
		}
	}

	private void showChoseDialog() {
		Builder builder = new Builder(this);
		builder.setTitle("请选择程序");
		builder.setSingleChoiceItems(R.array.single_chose_image, -1, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//which,0:照相机,1:图库
				if(which == 0){
					UIHelper.sendMsgToHandler(handler, JS_CALL_CAMERA) ;
				}else{
					UIHelper.sendMsgToHandler(handler, JS_CALL_PHOTO) ;
				}
				dialog.dismiss() ;
			}
		}); 
		builder.setCancelable(true);
		builder.create();
		builder.show();
	}

}
