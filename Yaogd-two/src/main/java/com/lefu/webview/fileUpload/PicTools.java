package com.lefu.webview.fileUpload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

/**
 * @function 图片工具类
 * @author panjp
 * @time 2013-9-12
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
		if(size < minSize ){//size<=60
			result = baos.toByteArray() ;
		} else if(size <= maxSize){//60<size<=90
			result = baos.toByteArray() ;
		} else if(size >= 90 && size <130){
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
			result = baos.toByteArray() ;
		} else if(size >= 130 && size <150){
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 45, baos);
			result = baos.toByteArray() ;
		} else if(size >= 150 && size <170){
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
			result = baos.toByteArray() ;
		} else if(size >= 170 && size <190){
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
			result = baos.toByteArray() ;
		} else{
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
			result = baos.toByteArray() ;
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
	    	final int heightRatio = Math.round((float) height/ (float) reqHeight);
	    	final int widthRatio = Math.round((float) width / (float) reqWidth);            
	    	inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    
	    return inSampleSize ;
	}
	
	/** 
	 * 把一个View的对象转换成bitmap 
	 */
	public static Bitmap getViewBitmap(View v) {
		v.clearFocus();
		v.setPressed(false);

		//能画缓存就返回false 
		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			Log.e("BtPrinter", "failed getViewBitmap(" + v + ")", new RuntimeException());
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view 
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}

	/**
	* 将彩色图转换为黑白图
	* 
	* @param 位图
	* @return 返回转换好的位图
	*/
	public static Bitmap convertToBlackWhite(Bitmap bmp) {
		int width = bmp.getWidth(); // 获取位图的宽
		int height = bmp.getHeight(); // 获取位图的高
		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		bmp.recycle() ;//add by yaogd
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
				grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		Bitmap newBmp = Bitmap.createBitmap(width, height, Config.RGB_565);

		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

		return newBmp;
	}

	/**
	 * 将Bitmap存为 .bmp格式图片
	 * @param bitmap
	 * @param filename
	 */
	public static void saveBmp(Bitmap bitmap, String filename) {
		if (bitmap == null)
			return;
		// 位图大小
		int nBmpWidth = bitmap.getWidth();
		int nBmpHeight = bitmap.getHeight();
		// 图像数据大小
		int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);
		try {
			// 存储文件名
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileos = new FileOutputStream(filename);
			// bmp文件头
			int bfType = 0x4d42;
			long bfSize = 14 + 40 + bufferSize;
			int bfReserved1 = 0;
			int bfReserved2 = 0;
			long bfOffBits = 14 + 40;
			// 保存bmp文件头
			writeWord(fileos, bfType);
			writeDword(fileos, bfSize);
			writeWord(fileos, bfReserved1);
			writeWord(fileos, bfReserved2);
			writeDword(fileos, bfOffBits);
			// bmp信息头
			long biSize = 40L;
			long biWidth = nBmpWidth;
			long biHeight = nBmpHeight;
			int biPlanes = 1;
			int biBitCount = 24;
			long biCompression = 0L;
			long biSizeImage = 0L;
			long biXpelsPerMeter = 0L;
			long biYPelsPerMeter = 0L;
			long biClrUsed = 0L;
			long biClrImportant = 0L;
			// 保存bmp信息头
			writeDword(fileos, biSize);
			writeLong(fileos, biWidth);
			writeLong(fileos, biHeight);
			writeWord(fileos, biPlanes);
			writeWord(fileos, biBitCount);
			writeDword(fileos, biCompression);
			writeDword(fileos, biSizeImage);
			writeLong(fileos, biXpelsPerMeter);
			writeLong(fileos, biYPelsPerMeter);
			writeDword(fileos, biClrUsed);
			writeDword(fileos, biClrImportant);
			// 像素扫描
			byte bmpData[] = new byte[bufferSize];
			int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
			for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
				for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3) {
					int clr = bitmap.getPixel(wRow, nCol);
					bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
					bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color.green(clr);
					bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color.red(clr);
				}

			fileos.write(bmpData);
			fileos.flush();
			fileos.close();
			bitmap.recycle() ;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	private static void writeWord(FileOutputStream stream, int value) throws IOException {
		byte[] b = new byte[2];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);
		stream.write(b);
	}

	private static void writeDword(FileOutputStream stream, long value) throws IOException {
		byte[] b = new byte[4];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);
		b[2] = (byte) (value >> 16 & 0xff);
		b[3] = (byte) (value >> 24 & 0xff);
		stream.write(b);
	}

	private static void writeLong(FileOutputStream stream, long value) throws IOException {
		byte[] b = new byte[4];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);
		b[2] = (byte) (value >> 16 & 0xff);
		b[3] = (byte) (value >> 24 & 0xff);
		stream.write(b);
	}

}
