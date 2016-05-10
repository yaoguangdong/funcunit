package com.yaogd.imgprcs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.yaogd.lib.A;

/**
 * 图片工具类
 * author yaoguangdong
 * 2015-9-25
 */
public class PicTools {
	
	/**
	 * 压缩图片的尺寸 
	 * path :图片路径
	 * int reqWidth , 目标参数，想要压缩的宽的尺寸
	 * int reqHeight ,目标参数，想要压缩的长的尺寸
	 */
	public static Bitmap compressImgResize(String srcPath, int reqWidth , int reqHeight ) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inInputShareable = true;
		opts.inPurgeable = true;
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(srcPath, opts);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight) ;
		newOpts.inJustDecodeBounds = false;
		Bitmap destBm = BitmapFactory.decodeFile(srcPath, newOpts);
		
		return destBm;
	}
	
	/**
	 * 压缩质量
	 * 通过大量的测试找到的合适的值，找到质量和体积的平衡点
	 * @param minSize KB 压缩到最小质量，防止压缩过度导致的失真
	 * @param maxSize KB 压缩到的最大允许质量。
	 * @param savePath
	 */
	public static void compressBmpToFile(Bitmap bmp, int minSize, int maxSize, String savePath) {
		byte[] result;
		if(maxSize < minSize || bmp == null){
			return ;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//默认按照80压缩一次
		bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		
		int size = baos.toByteArray().length / 1024 ;
		A.i("option=80-->Size:" + size) ;
		if(size < minSize ){//size<=60
			result = baos.toByteArray() ;
		} else if(size <= maxSize){//60<size<=90
			result = baos.toByteArray() ;
		} else if(size >= 90 && size <130){
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
			result = baos.toByteArray() ;
			A.i("option=60-->Size:" + result.length  / 1024 ) ;
		} else if(size >= 130 && size <150){
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 45, baos);
			result = baos.toByteArray() ;
			A.i("option=45-->Size:" + result.length  / 1024 ) ;
		} else if(size >= 150 && size <170){
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
			result = baos.toByteArray() ;
			A.i("option=30-->Size:" + result.length  / 1024 ) ;
		} else if(size >= 170 && size <190){
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
			result = baos.toByteArray() ;
			A.i("option=20-->Size:" + result.length  / 1024 ) ;
		} else{
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
			result = baos.toByteArray() ;
			A.i("option=15-->Size:" + result.length  / 1024 ) ;
		}
		//回收bitmap资源
		bmp.recycle() ;
		bmp = null ;
		try {
			File file = new File(savePath); 
			if( ! file.exists()){
				file.createNewFile() ;
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray()) ;
			fos.flush() ;
			fos.close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth , int reqHeight ){
		int height = options.outHeight;
	    int width = options.outWidth; 
	    int inSampleSize = 1;
	    if (height > reqHeight || width > reqWidth) {
	    	// 计算出实际宽高和目标宽高的比率
	    	final int heightRatio = Math.round((float) height/ (float) reqHeight);
	    	final int widthRatio = Math.round((float) width / (float) reqWidth);   
	    	// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
	    	inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    
	    return inSampleSize ;
	}
	
    /**
     * 图片压缩方法：（使用compress的方法）
     * 
     * @explain 如果bitmap本身的大小小于maxSize，则不作处理
     * @param bitmap  要压缩的图片
     * @param maxSize 压缩后的大小，单位kb
     */
    public static Bitmap compressBmp(Bitmap bitmap, double maxSize) {
        // 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 格式、质量、输出流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 获取bitmap大小 是允许最大大小的多少倍
        double i = mid / maxSize;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (i > 1) {
            // 缩放图片 此处用到平方根 将宽度和高度压缩掉对应的平方根倍
            // （保持宽高不变，缩放后也达到了最大占用空间的大小）
            bitmap = scale(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));
        }
        return bitmap;
    }
    
    /***
     * 图片的缩放方法
     * 
     * @param src ：源图片资源
     * @param newWidth ：缩放后宽度
     * @param newHeight ：缩放后高度
     */
    public static Bitmap scale(Bitmap src, double newWidth, double newHeight) {
        // 记录src的宽高
        float width = src.getWidth();
        float height = src.getHeight();
        // 创建一个matrix容器
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 开始缩放
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建缩放后的图片
        return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height,
                matrix, true);
    }
	
    public static void dumpBmpToFile(Bitmap bmp, String savePath){
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//默认按照80压缩一次
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		//回收bitmap资源
		bmp.recycle() ;
		bmp = null ;
		try {
			File file = new File(savePath); 
			if( ! file.exists()){
				file.createNewFile() ;
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray()) ;
			fos.flush() ;
			fos.close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
}
