package com.lefu.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lefu.camera.CameraPreview.OnCameraStatusListener;
import com.yaogd.R;

public class CameraActivity extends Activity implements 
        OnCameraStatusListener {

	private ImageButton imageButton ;
    private CameraMask maskView ;
    private CameraPreview mCameraPreview; 
	private boolean isTaking = false ;
    
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        // 设置横屏 
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
        // 设置全屏 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        // 设置布局视图 
        setContentView(R.layout.camera); 
        
        // 照相预览界面 
        mCameraPreview = (CameraPreview) findViewById(R.id.pre_view); 
        mCameraPreview.setOnCameraStatusListener(this); 
        maskView = (CameraMask) findViewById(R.id.mask_view) ;
        
        imageButton = ((ImageButton)findViewById(R.id.take_pic_btn)) ;
        imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isTaking) { 
		        	isTaking = true ;
		            mCameraPreview.takePicture(); 
		        } 
			}
		}) ;
    } 
    
    /** 
     * 相机拍照结束 
     */ 
    @Override 
    public void onCameraStopped(byte[] data) { 
    	long dateTaken = System.currentTimeMillis(); 
    	Uri uri = ProcessPhoto.takePhotoEnd(this, dateTaken, data, maskView, mCameraPreview) ;
        // 返回结果 
        Intent intent = getIntent(); 
        intent.putExtra("uri", uri.toString()); 
        intent.putExtra("dateTaken", dateTaken); 
        setResult(20, intent); 
        // 关闭当前Activity 
        isTaking = false ;
        finish(); 
    } 

    /** 
     * 拍摄时自动对焦事件 
     */ 
    @Override 
    public void onCameraStatus(int status) { 
        switch(status){
        	case OnCameraStatusListener.FOCUS_FAILED :
        		Toast.makeText(this, "聚焦失败，请重新拍照", Toast.LENGTH_SHORT).show();
        		isTaking = false ;
        		break ;
        	case OnCameraStatusListener.OPEN_FAILED :
        		Toast.makeText(this, "抱歉，相机故障", Toast.LENGTH_SHORT).show() ;
        		isTaking = false ;
        		break ;
        	case OnCameraStatusListener.TAKE_PICTURE_FINISHED :
        		isTaking = false ;
        		break ;
        	case OnCameraStatusListener.CAMERA_UNDEFINED_ERROR :
        		Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show() ;
        		isTaking = false ;
        		break ;
        }
    } 
    
    public void freeCamera()
    {
    	if (this.maskView != null)
    	{
    		this.maskView.destroyDrawingCache() ;
    		this.maskView = null;
    	}
    	if (this.mCameraPreview != null)
    	{
    		this.mCameraPreview.destroyDrawingCache() ;
    		this.mCameraPreview = null;
    	}
    }
    
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
    {
    	if ((paramInt == KeyEvent.KEYCODE_BACK) && (paramKeyEvent.getRepeatCount() == 0))
    	{
    		finish();
    		freeCamera();
    	}
    	return super.onKeyDown(paramInt, paramKeyEvent);
    }
    
} 