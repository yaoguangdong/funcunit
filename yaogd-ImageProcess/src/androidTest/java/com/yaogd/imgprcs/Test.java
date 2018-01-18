package com.yaogd.imgprcs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;

import com.yaogd.lib.A;
import com.yaogd.lib.TestFrame;

/**
 * 数据库测试
 * @author yaoguangdong
 * 2014-10-27
 */
public class Test extends TestFrame {

	private String WORKING_PATH = Environment.getExternalStorageDirectory().toString() + "/yaogdimg/" ;
	private int REQ_WIDTH = 480 ;
	private int REQ_HEIGHT = 320 ;
	private int MIN_SIZE = 100 ;
	private int MAX_SIZE = 300 ;
	
	private String RAW_FILE_01 = "raw2988X5312_2.3M.jpg";
	private String RAW_FILE_02 = "raw5312X2988_5M.jpg";
	private String RAW_FILE_03 = "raw1080X1920_146k.png";
	private String RAW_FILE_04 = "raw1920X1200_1.68M.jpg";
	private String RAW_FILE_05 = "raw1920_1200_1.1M.jpg";
	private String RAW_FILE_06 = "raw1024X768_768K.jpg";
	
	private String DES_FILE_01 = "des2988X5312_2.3M.jpeg";
	private String DES_FILE_02 = "des5312X2988_5M.jpeg";
	private String DES_FILE_03 = "des1080X1920_146k.jpeg";
	private String DES_FILE_04 = "des1920X1200_1.68M.jpeg";
	private String DES_FILE_05 = "des1920_1200_1.1M.jpeg";
	private String DES_FILE_06 = "des1024X768_768K.jpeg";
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        File file = new File(WORKING_PATH); 
        if( ! file.exists()){
        	file.mkdirs() ;
        }
        
    }
	
	/**
	 * 先运行一次，在SDcard中准备好原始图片
	 * 从assets中读取出来，放到yaogdimg中
	 */
	@MediumTest
	public void testStep1PrepareImage(){
		copyAssetsToSdcard(RAW_FILE_01, WORKING_PATH + RAW_FILE_01) ;
		copyAssetsToSdcard(RAW_FILE_02, WORKING_PATH + RAW_FILE_02) ;
		copyAssetsToSdcard(RAW_FILE_03, WORKING_PATH + RAW_FILE_03) ;
		copyAssetsToSdcard(RAW_FILE_04, WORKING_PATH + RAW_FILE_04) ;
		copyAssetsToSdcard(RAW_FILE_05, WORKING_PATH + RAW_FILE_05) ;
		copyAssetsToSdcard(RAW_FILE_06, WORKING_PATH + RAW_FILE_06) ;
	}
	
    @LargeTest
    public void testStep2Compress() {
    	try {
    		
    		compressFunction1(WORKING_PATH + RAW_FILE_01, WORKING_PATH + DES_FILE_01) ;
    		compressFunction1(WORKING_PATH + RAW_FILE_02, WORKING_PATH + DES_FILE_02) ;
    		compressFunction1(WORKING_PATH + RAW_FILE_03, WORKING_PATH + DES_FILE_03) ;
    		compressFunction1(WORKING_PATH + RAW_FILE_04, WORKING_PATH + DES_FILE_04) ;
    		compressFunction1(WORKING_PATH + RAW_FILE_05, WORKING_PATH + DES_FILE_05) ;
    		compressFunction1(WORKING_PATH + RAW_FILE_06, WORKING_PATH + DES_FILE_06) ;
    		
    		//实际测试证明第二种压缩方式的结果更准确一些
    		compressFunction2(WORKING_PATH + RAW_FILE_01, WORKING_PATH + DES_FILE_01) ;
    		compressFunction2(WORKING_PATH + RAW_FILE_02, WORKING_PATH + DES_FILE_02) ;
    		compressFunction2(WORKING_PATH + RAW_FILE_03, WORKING_PATH + DES_FILE_03) ;
    		compressFunction2(WORKING_PATH + RAW_FILE_04, WORKING_PATH + DES_FILE_04) ;
    		compressFunction2(WORKING_PATH + RAW_FILE_05, WORKING_PATH + DES_FILE_05) ;
    		compressFunction2(WORKING_PATH + RAW_FILE_06, WORKING_PATH + DES_FILE_06) ;
    		
		} catch (Exception e) {
			A.e( "", e) ;
			assertNotNull(null);
		}
    }
    
    private void compressFunction1(String srcFilePath, String desFilePath){
    	//测试压缩图片尺寸
		Bitmap bmp = PicTools.compressImgResize(srcFilePath, REQ_WIDTH, REQ_HEIGHT) ;
		//测试压缩图片质量
		PicTools.compressBmpToFile(bmp, MIN_SIZE, MAX_SIZE, desFilePath + "_2.jpeg") ;
    }
    
    private void compressFunction2(String srcFilePath, String desFilePath){
    	//测试压缩图片尺寸
		Bitmap bmp = PicTools.compressImgResize(srcFilePath, REQ_WIDTH, REQ_HEIGHT) ;
		//测试压缩图片质量的新方法
		Bitmap bmp2 = PicTools.compressBmp(bmp, MAX_SIZE) ;
		
		PicTools.dumpBmpToFile(bmp2, desFilePath + "_3.jpeg") ;
    }
    
    /**
	 * 将assets中的文件拷贝到sdcard中
	 * @param srcFilePath
	 * @param desFilePath
	 */
	private void copyAssetsToSdcard(String srcFilePath, String desFilePath){
		InputStream is = null;
		try {
			is = getContext().getAssets().open(srcFilePath);
			File file = new File(desFilePath); 
			if( ! file.exists()){
				file.createNewFile() ;
			}
			FileOutputStream fos = new FileOutputStream(file);
			
			byte[] b = new byte[1024];
			while((is.read(b)) != -1){
				fos.write(b);
			}
			
			fos.flush() ;
			fos.close() ;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
