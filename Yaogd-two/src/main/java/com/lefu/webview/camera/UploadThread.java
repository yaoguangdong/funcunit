package com.lefu.webview.camera;

import java.io.File;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lefu.A;
import com.lefu.webview.camera.UploadUtil.FormFile;
import com.lefu.webview.camera.UploadUtil.ResponseBody;

/**
 * 上传参数和图片
 * @author: yaoguangdong
 * @data: 2014-1-26
 */
public class UploadThread extends Thread{
	private String [] filePaths;
	private Map<String, String> params ;
	private Handler mHandler = null ;
			
	UploadThread(Handler handler, String [] filePaths, Map<String, String> params){
		this.mHandler = handler ;
		this.params = params ;
		this.filePaths = filePaths ;
	}
	@Override
	public void run() {
		Message msg = new Message();
		UploadUtil uploadUtil = new UploadUtil() ;
		for(String key : params.keySet()){
			uploadUtil.putParams(key, params.get(key)) ;
		}
		for(String filePath : filePaths){
			File file = new File(filePath);  
            if (file != null) {  
            	FormFile formFile = uploadUtil.new FormFile(file.getName(), file, "file", "image/jpg");
            	uploadUtil.putFormFiles(formFile) ;
            }  
		}
		
		ResponseBody responseBody = uploadUtil.post() ;
    	
        if(UploadUtil.OK != responseBody.responseCode ){
        	//数据上送失败
			msg.what = 99 ;
			Bundle data = new Bundle();
			data.putString("sendError", responseBody.responseMsg) ;
			////A.i("responseBody.responseMsg" + responseBody.responseMsg) ;
			msg.setData(data) ;
			mHandler.sendMessage(msg);
        } else{
        	msg.what = 100 ;
        	mHandler.sendMessage(msg);
        }
		
	}
	
}