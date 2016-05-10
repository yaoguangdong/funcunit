package cn.farcanton.compressBitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 压缩图片的尺寸 
 * yaogd1633@163.com
 * time:2013-10-11
 */
public class CompressImgResizeFree2 implements FrameActivity.CompressImg{

	/**
	 * 压缩图片的尺寸 
	 * path :图片路径
	 * int reqWidth , 目标参数，想要压缩的宽的尺寸
	 * int reqHeight ,目标参数，想要压缩的长的尺寸
	 */
	@Override
	public Bitmap compressImgResize(String path, int reqWidth , int reqHeight , Context context) {
		// 获取源图片的大小
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
		BitmapFactory.decodeFile(path, opts);
		// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 设置采样率缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		//options.inSampleSize = 2; //图片宽高都为原来的二分之一，即图片为原来的四分之一
		//这种方法只是对图片做了一个缩放处理，降低了图片的分辨率，在需要保证图片质量的应用中不可取。
		newOpts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight) ;
		Log.i("yaogd", "newOpts.inSampleSize:" + newOpts.inSampleSize);
		// inJustDecodeBounds设为false表示把图片读进内存中
		newOpts.inJustDecodeBounds = false;
		// inSampleSize = 2 加载时二次采样，缩小为原图的1/4,
		Bitmap destBm = BitmapFactory.decodeFile(path, newOpts);
		return destBm;
	}
	
	/**
	 * 另一个节省内存的方法是调整位图的大小
	 */
	private Bitmap porcessImg(Bitmap bt){
		Bitmap bm = Bitmap.createScaledBitmap(bt, 480, 320, false);
		int width = bm.getWidth() ;
		int height = bm.getHeight() ;
		int x = width >> 1 ;
		int y = height >> 1 ;
		int [] pixels1 = new int[(width*height)] ;
		int [] pixels2 = new int[(width*height)] ;
		int [] pixels3 = new int[(width*height)] ;
		int [] pixels4 = new int[(width*height)] ;
		
		bm.getPixels(pixels1, 0, width, 0, 0, width >> 1, height >> 1) ;
		bm.getPixels(pixels2, 0, width, x, 0, width >> 1, height >> 1) ;
		bm.getPixels(pixels3, 0, width, 0, y, width >> 1, height >> 1) ;
		bm.getPixels(pixels4, 0, width, x, y, width >> 1, height >> 1) ;
		
		if(bm.isMutable()){
			bm.setPixels(pixels2, 0, width, 0, 0, width >> 1, height >> 1) ;
			bm.setPixels(pixels4, 0, width, x, 0, width >> 1, height >> 1) ;
			bm.setPixels(pixels1, 0, width, 0, y, width >> 1, height >> 1) ;
			bm.setPixels(pixels3, 0, width, x, y, width >> 1, height >> 1) ;
		}
		
		return bm ;
	}
	
	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth , int reqHeight ){
		int height = options.outHeight;
	    int width = options.outWidth; 
	    int inSampleSize = 1;
	    if (height > reqHeight || width > reqWidth) {
	    	final int heightRatio = Math.round((float) height/ (float) reqHeight);
	    	final int widthRatio = Math.round((float) width / (float) reqWidth);            
	    	inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    
	    return inSampleSize ;
	}
	
}
