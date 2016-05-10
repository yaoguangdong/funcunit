package com.lefu.camera;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class CompressImgImpl implements ICompressImg{
	/**
	 * 压缩图片的尺寸 
	 */
	public Bitmap compressImgResize(byte[] picData , int reqWidth , int reqHeight, Context context){
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
    	localOptions.inInputShareable = true;
    	localOptions.inPurgeable = true;
    	localOptions.inJustDecodeBounds = true;
    	BitmapFactory.decodeByteArray(picData, 0, picData.length, localOptions);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 设置采样率缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		//options.inSampleSize = 2; //图片宽高都为原来的二分之一，即图片为原来的四分之一
		//这种方法只是对图片做了一个缩放处理，降低了图片的分辨率，在需要保证图片质量的应用中不可取。
		newOpts.inSampleSize = calculateInSampleSize(localOptions, reqWidth, reqHeight) ;
		Log.i("yaogd", "newOpts.inSampleSize:" + newOpts.inSampleSize);
		// inJustDecodeBounds设为false表示把图片读进内存中
		newOpts.inJustDecodeBounds = false;
		// 获取缩放后图片
		Bitmap destBm = BitmapFactory.decodeByteArray(picData, 0, picData.length, newOpts);
		return destBm;
	}
	/**
	 * 压缩质量
	 * @param minSize KB 压缩到最小质量，防止压缩过度导致的失真
	 * @param maxSize KB 压缩到的最大允许质量。
	 */
	public byte[] compressBmpToFile(Bitmap bmp, int minSize, int maxSize) {
		if(maxSize < minSize){
			return null ;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 80;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		
		int step = 20 ;
		int oldSize = 0;
		int newSize = baos.toByteArray().length / 1024 ;
		Log.i("yaogd", "newSize" + newSize + "options" + options) ;
		while (newSize > maxSize || newSize < minSize ) { 
			//一下的方法已经废弃，因为没有算出压缩比和大小的函数关系。
			if(options == 0 || options == 100){
				break ;
			}
			//如果压缩后的尺寸仍然太大,继续降低压缩比率
			if (newSize > maxSize){
				int addStep = 0 ;
				if(oldSize == 0){
					options -= step;
				} else{
					if(oldSize < maxSize){
						addStep = (int)(( (double)(newSize - maxSize )/(newSize - oldSize ) ) * step + 1); 
					}else{
						addStep = (int)(( (double)(newSize - maxSize )/(oldSize - maxSize ) ) * step + 1); 
					} 
					options -= addStep;
					Log.i("yaogd", "addStep:" + addStep) ;
				}
				if(options <= 0){
					options = 0 ;
				}
			} else if(newSize < minSize){
				int subStep = 0;
				if(oldSize == 0){
					subStep = step / 2 ;
				} else {
					if(oldSize > newSize){
						subStep = (int)(( (double)(minSize - newSize)/(oldSize - newSize) ) * step + 1); 
					} else{
						subStep = (int)(( (double)(minSize - newSize)/(minSize - oldSize) ) * step + 1); 
					}
				}
				
				options += subStep;
				while(options >= 100){
					options -= subStep ;
					subStep = subStep / 2;
					if(subStep == 0){
						options = 100 ;
					} else{
						options += subStep ;
					}
				}
				Log.i("yaogd", "subStep:" + subStep) ;
			} else{
				break ;
			}
			baos.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
			oldSize = newSize ;
			newSize = baos.toByteArray().length / 1024 ;
			Log.i("yaogd", "oldSize" + oldSize + "newSize" + newSize + "options" + options) ;
		}
		return baos.toByteArray() ;
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