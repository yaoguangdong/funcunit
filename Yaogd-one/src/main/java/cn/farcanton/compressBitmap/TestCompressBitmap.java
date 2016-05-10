package cn.farcanton.compressBitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import cn.farcanton.R;

/**
 * 处理图片导致的内存溢出：
 * ①压缩读取：
 * 文件形式: file.length()和流的形式: 讲图片文件读到内存输入流中,看它的byte数，
 * 这两种形式的图片大小和文件真实大小一致。
 * 内存中的bitmap形式的，大小500K-->10M
 * 因此会出现内存溢出。所以在读取图片文件时进行尺寸的压缩，最后内存中的bitmap就会减小。
 * ②不要一次性加载太多图片。
 */
public class TestCompressBitmap extends FrameActivity {

	private Bitmap bitmap;
	
	private String assetsFile = "xx.jpg" ;
	
//	private String [] fileNameInSdCards = {"1536_2048_1871KB.jpg","640_480_61KB.jpg","1024_768_48KB.jpg","1280_1024_436KB.jpg","2816_2212_1023KB.JPG","4256_2832_5282KB.jpg"} ;
//	private String [] fileNameInSdCards = {"1536_2048_1871KB.jpg"} ;
	private String sdcaredFile = "/mnt/sdcard/YaogdCarmea/1M.jpg" ;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
//		test1();
//		test2();
//		test3();
//		test4();
		
		test5();
	}
	/**
	 * 如果尺寸大，先压缩大小到1024x768
	 * 然后压缩质量到300k以内。
	 */
	private void test5(){
		File file = new File("/mnt/sdcard/YaogdCarmea/") ;
		if(!file.exists()){
			file.mkdirs() ;
		}
		bitmap = BitmapFactory.decodeFile(sdcaredFile);
		
		Bitmap result = TheBestCompress.imageZoom(bitmap, 100) ;
		dumpBitmap(result, "test3.jpg") ;
//		for(String fileName : fileNameInSdCards){
//			Log.i("yaogd","---fileName:" + fileName ) ;
//			// 压缩图片的尺寸 
//			bitmap = new CompressImgResizeFree2().compressImgResize(sdcaredFile + fileName, 1024, 768, this);
//			// 压缩图片的大小
//			byte [] result = compressBmpToFile(bitmap,100,200) ;
//			writeToFile(result,fileName) ;
//		}
		
	}
	
	/**
	 * 压缩图片尺寸 
	 */
	private void test4() {
		//显示原始的图片
		File file = new File(sdcaredFile);
		Drawable da = pasteImage(file,R.id.image1);

		// 压缩图片的尺寸 
		bitmap = new CompressImgResizeFree2().compressImgResize(sdcaredFile, 200, 100, this);
		
		// 显示原始图片信息
		int rawHeight = da.getIntrinsicHeight();
		int rawWidth = da.getIntrinsicWidth() ;
		showText("raw:("+rawWidth+","+rawHeight+"),size:"+file.length()+"bytes",R.id.text1);
		// 显示压缩后的图片信息
		int newWidth = bitmap.getWidth();
		int newHeight = bitmap.getHeight();
		// 显示压缩后的图片
		pasteImage(bitmap,R.id.image2);
		showText("raw:("+newWidth+","+newHeight+"),new size:"+0+"bytes",R.id.text2);
	}
	/**
	 * 压缩质量
	 * @param bmp
	 * @param minSize KB 压缩到最小质量，防止压缩过度导致的失真
	 * @param maxSize KB 压缩到的最大允许质量。
	 * @return
	 */
	public byte[] compressBmpToFile(Bitmap bmp, int minSize, int maxSize){
		if(maxSize < minSize){
			return null ;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		while (options >= 0 ) { 
			
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
			int size = baos.toByteArray().length / 1024 ;
			
			Log.i("yaogd", "size" + size + "options" + options) ;
			options -= 10 ;
			baos.reset();
			
		}
		return baos.toByteArray() ;
	}
	
	/**
	 * 压缩图片质量,直接压缩
	 * 
	 */
	private void test3() {
		
		//显示原始的图片
		File file = new File(sdcaredFile);
		bitmap = BitmapFactory.decodeFile(sdcaredFile);
		byte [] result = compressBmpToFile(bitmap,200,300) ;
		writeToFile(result,"1536_2048_1871KB.jpg") ;
		
		// 显示原始图片信息
		Drawable da = pasteImage(file,R.id.image1);
		int rawHeight = da.getIntrinsicHeight();
		int rawWidth = da.getIntrinsicWidth() ;
		showText("raw:("+rawWidth+","+rawHeight+"),size:"+file.length()+"bytes",R.id.text1);
		
		// 显示压缩后的图片信息
		int newWidth = bitmap.getWidth();
		int newHeight = bitmap.getHeight();
		// 显示压缩后的图片
		pasteImage(bitmap,R.id.image2);
		showText("raw:("+newWidth+","+newHeight+"),new size:"+0+"bytes",R.id.text2);
	}
	/**
	 * 压缩图片尺寸，严格压缩
	 */
	private void test2() {
				
		//显示原始的图片
		File file = new File(sdcaredFile);
		Drawable da = pasteImage(file,R.id.image1);
		// 显示原始图片信息
		int rawHeight = da.getIntrinsicHeight();
		int rawWidth = da.getIntrinsicWidth() ;
		showText("raw:("+rawWidth+","+rawHeight+"),size:"+file.length()+"bytes",R.id.text1);

		// 压缩图片的尺寸 
		bitmap = new CompressImgResizeFree2().compressImgResize(sdcaredFile, 200, 100, this);
		
		dumpBitmap(bitmap,"1536_2048_1871KB.jpg") ;
		
		// 显示压缩后的图片
		pasteImage(bitmap,R.id.image2);
		
		// 显示压缩后的图片信息
		int newWidth = bitmap.getWidth();
		int newHeight = bitmap.getHeight();
		
		showText("raw:("+newWidth+","+newHeight+"),new size:"+0+"bytes",R.id.text2);
		//释放废弃的内存
		//releaseBitmap(bitmap);
		
	}
	/**
	 * 压缩图片尺寸，自由压缩
	 */
	private void test1(){
		//显示原始的图片
		InputStream is = null;
		try {
			is = this.getAssets().open(assetsFile);
			Drawable da = pasteImage(is,R.id.image1);
			// 显示原始图片信息
			int rawHeight = da.getIntrinsicHeight();
			int rawWidth = da.getIntrinsicWidth() ;
			int rawSize = this.getAssets().open(assetsFile).read();
			
			showText("raw:("+rawWidth+","+rawHeight+"),size:"+rawSize+"bytes",R.id.text1);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 压缩图片的尺寸 
		bitmap = new CompressImgResizeFree().compressImgResize(assetsFile, 200, 100, this);	
		
		// 显示压缩后的图片
		pasteImage(bitmap,R.id.image2);
		// 显示压缩后的图片信息
		int newWidth = bitmap.getWidth();
		int newHeight = bitmap.getHeight();
		showText("raw:("+newWidth+","+newHeight+")",R.id.text2);
		//释放废弃的内存
		//releaseBitmap(bitmap);
	}
	
	private void writeToFile(byte [] fileBytes, String fileName){
		try {
			String newFileName = android.os.Environment.getExternalStorageDirectory().toString()+"/YaogdCarmea/["+
					fileName + "].jpeg";
            File newFile = new File(newFileName); 
            if( ! newFile.exists()){
            	newFile.createNewFile() ;
            }
			FileOutputStream fos = new FileOutputStream(newFile);
			fos.write(fileBytes);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void dumpBitmap(Bitmap b,String fileName) {
	     if (b != null) {
	         java.io.FileOutputStream fos = null;
	         try {
	             String filename = android.os.Environment.getExternalStorageDirectory().toString()+"/YaogdCarmea/["+
	            		 fileName + "].jpeg";
	             File file = new File(filename); 
	             if( ! file.exists()){
	            	 file.createNewFile() ;
	             }
	             fos = new java.io.FileOutputStream(file);
	             b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	             
	            //这个是等比例缩放:
	     		//bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
	     		//这个是截取图片某部分:
	     		//bitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
	     		
	             fos.flush() ;
	         } catch (java.io.IOException ex) {
	        	 Log.e("yaogd", "",ex) ;
	         } finally {
	             if (fos != null) {
		             try {
		                 fos.close();
		             } catch (java.io.IOException ex) {
		             }
	             }
	         }
	     }
	     
	}
	
	/**
	 * 把bitmap转换成String
	 * @param bm
	 * @return
	 */
	public static String bitmapToString(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
	
	
}



