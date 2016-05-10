package com.lefu.camera;

import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lefu.A;

public class CameraPreview extends SurfaceView implements 
        SurfaceHolder.Callback {
	public float picWratio ;
	public float picHratio ;
	private Context mCxt ;
	private int pictureSizeWidth ;
	private int previewSizeWidth ;
	private int pictureSizeHeight ;
	private int previewSizeHeight ;
	
    //拍照中
//    public boolean isTaking = false;  
    /** 监听接口 */ 
    private OnCameraStatusListener listener; 
    private SurfaceHolder holder; 
    private Camera camera; 
    private List<String> focusModes;
	
    // 创建一个PictureCallback对象，并实现其中的onPictureTaken方法 
    private PictureCallback pictureCallback = new PictureCallback() { 

        // 该方法用于处理拍摄后的照片数据 
        @Override 
        public void onPictureTaken(byte[] data, Camera camera) { 

        	stopCamera(OnCameraStatusListener.TAKE_PICTURE_FINISHED) ;
            //处理照片数据 
            if (null != listener) {
                listener.onCameraStopped(data); 
            } 
        } 
    };

    // Preview类的构造方法 
    public CameraPreview(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        this.mCxt = context ;
        // 获得SurfaceHolder对象 
        holder = getHolder(); 
        // 指定用于捕捉拍照事件的SurfaceHolder.Callback对象 
        holder.addCallback(this); 
        // 设置SurfaceHolder对象的类型 
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
    } 

    // 在surface创建时激发 
    public void surfaceCreated(SurfaceHolder holder) { 
        
        try { 
        	camera = Camera.open(); 
        	if(camera != null){
        		// 设置用于显示拍照摄像的SurfaceHolder对象 
                camera.setPreviewDisplay(holder); 
        	} else{
        		throw new Exception();
        	}
        } catch (Exception e) { 
        	stopCamera(OnCameraStatusListener.OPEN_FAILED) ;
        } 
    } 

    private void stopCamera(int cameraStatus){
    	if(camera != null){
    		camera.stopPreview(); 
            // 释放手机摄像头 
            camera.release(); 
            camera = null; 
    	}
    	if (null != listener) {
        	listener.onCameraStatus(cameraStatus) ;
        } 
    }
    // 在surface销毁时激发 
    public void surfaceDestroyed(SurfaceHolder holder) { 
    	stopCamera(OnCameraStatusListener.TAKE_PICTURE_FINISHED) ;
    } 

    // 在surface的大小发生改变时激发 
    public void surfaceChanged(final SurfaceHolder holder, int format, int w, 
            int h) { 
        try { 
            // 获取照相机参数 
            Parameters parameters = camera.getParameters();
            // 初始化照片尺寸
            parameters = initPicSize(parameters,w,h) ;
            // 获得手机支持的聚焦模式
            this.focusModes = parameters.getSupportedFocusModes();
            parameters.setJpegQuality(100) ;
            parameters.setFlashMode(Parameters.FLASH_MODE_ON) ;
            // 设置照片格式 
            parameters.setPictureFormat(PixelFormat.JPEG);
            // 设置照相机参数 
            camera.setParameters(parameters); 
            // 开始拍照 
            camera.startPreview(); 
        } catch (Exception e) { 
        	stopCamera(OnCameraStatusListener.CAMERA_UNDEFINED_ERROR) ;
        } 
    } 
    
    private Parameters initPicSize(Parameters parameters, int w, int h){
		//读取参数
		if(readCameraParams()){
			parameters.setPreviewSize(previewSizeWidth, previewSizeHeight);
    		picWratio = pictureSizeWidth / (float)w;
    		picHratio = pictureSizeHeight / (float)h;
            parameters.setPictureSize(pictureSizeWidth, pictureSizeHeight);
//            Log.v("yaogd", "pictureW/screenW=" + picWratio);
//            Log.v("yaogd", "pictureH/screenH=" + picWratio);
            Log.v("yaogd", "读取预览尺寸" + previewSizeWidth + "  * " + previewSizeHeight);
            Log.v("yaogd", "读取照片分辨率 " + pictureSizeWidth + " * " + pictureSizeHeight);
		}
    	else {
    		float f = w / h ;
        	// 设置预览尺寸 
            List previewSizeList = parameters.getSupportedPreviewSizes();
            Camera.Size previewSize = null ;
            if(previewSizeList.isEmpty()){
            	parameters.setPreviewSize(w, h);
            }else{
            	for(int i = 0; i < previewSizeList.size(); i++){
                	previewSize = ((Camera.Size)previewSizeList.get(i));
                	if (previewSize.width / previewSize.height == f){
                		previewSizeWidth = previewSize.width ;
                		previewSizeHeight = previewSize.height ;
                		parameters.setPreviewSize(previewSize.width, previewSize.height);
                    	break ;
                	}
                }
            }
            // 设置照片分辨率 
            List pictureSizeList = parameters.getSupportedPictureSizes();
            Camera.Size pictureSize = null ;
            if(pictureSizeList.isEmpty()){
            	parameters.setPictureSize(w, h);
            }else{
            	for(int i = 0; i < pictureSizeList.size(); i++){
                	pictureSize = ((Camera.Size)pictureSizeList.get(i));
                	if ((pictureSize.width < 2500) && 
                			(pictureSize.width > 1000) && 
                			(pictureSize.width / pictureSize.height == f)){
                		picWratio = pictureSize.width / (float)w;
                		picHratio = pictureSize.height / (float)h;
                		pictureSizeWidth = pictureSize.width ;
                		pictureSizeHeight = pictureSize.height;
                        parameters.setPictureSize(pictureSize.width, pictureSize.height);
                		break ;
                	}
                }
            }
            //将常用参数保存
            writeCameraParams();
    	}
    	
        return parameters;
    }

    // 停止拍照，并将拍摄的照片传入PictureCallback接口的onPictureTaken方法 
    public void takePicture() { 
        if (camera != null) { 
        	try
            {
	        	if ((this.focusModes != null) && (this.focusModes.contains("auto")))
	            {
	        		// 自动对焦 
	                camera.autoFocus(new AutoFocusCallback() { 
	                    @Override 
	                    public void onAutoFocus(boolean success, Camera camera) { 
	                        if (null != listener) {
	                        	if(success){
	                        		listener.onCameraStatus(OnCameraStatusListener.FOCUS_SUCCESS); 
	                        	} else{
	                        		listener.onCameraStatus(OnCameraStatusListener.FOCUS_FAILED); 
	                        	}
	                        } 
	                        Log.i("yaogd", "auto focus:" + success); 
	                        // 自动对焦成功后才拍摄 
	                        if (success) { 
	                            camera.takePicture(null, null, pictureCallback); 
	                        } 
	                    } 
	                }); 
	            }
	            else
	            {
	              this.camera.takePicture(null, null, this.pictureCallback);
	            }
            }catch (Exception localException) {
            	stopCamera(OnCameraStatusListener.CAMERA_UNDEFINED_ERROR) ;
            }
        } 
    } 

    // 设置监听事件 
    public void setOnCameraStatusListener(OnCameraStatusListener listener) { 
        this.listener = listener; 
    } 

    /** 
     * 相机拍照监听接口 
     */ 
    public interface OnCameraStatusListener { 

    	public static final int OPEN_SUCCESS = 1 ;
    	public static final int OPEN_FAILED = -1 ;
    	public static final int FOCUS_SUCCESS = 2 ;
    	public static final int FOCUS_FAILED = -2 ;
    	public static final int TAKE_PICTURE_FINISHED = 100 ;
    	public static final int CAMERA_UNDEFINED_ERROR = -100 ;
    	
        // 相机拍照结束事件 
        void onCameraStopped(byte[] data); 

        // 拍摄过程中的相机状态 
        void onCameraStatus(int status); 
    } 

    //写入相机参数
	private void writeCameraParams() {
		A.writeIntPreferences(mCxt,"picHeight", pictureSizeHeight) ;
		A.writeIntPreferences(mCxt,"picWidth", pictureSizeWidth) ;
		A.writeIntPreferences(mCxt,"preHeight", previewSizeHeight) ;
		A.writeIntPreferences(mCxt,"preWidth", previewSizeWidth) ;
	}
    //读取相机参数
	private boolean readCameraParams() {
		pictureSizeHeight = A.readIntPreferences(mCxt,"picHeight") ;
		pictureSizeWidth = A.readIntPreferences(mCxt,"picWidth") ;
		previewSizeHeight = A.readIntPreferences(mCxt,"preHeight") ;
		previewSizeWidth = A.readIntPreferences(mCxt,"preWidth") ;
		
		if(previewSizeWidth != 0 && previewSizeHeight != 0 
				&& pictureSizeWidth != 0 && pictureSizeHeight != 0){
			return true ;
		}else{
			return false ;
		}
	}
    
} 