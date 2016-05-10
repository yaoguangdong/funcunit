package cn.farcanton.compressBitmap;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 压缩图片的尺寸 
 * yaogd1633@163.com
 * time:2013-10-11
 */
public class CompressImgResizeFree implements FrameActivity.CompressImg{
	
	/**
     * Get the size in bytes of a bitmap.
     * @param bitmap
     * @return size in bytes
     */
    public static int getBitmapSize(Bitmap bitmap) {
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    
	/**
	 * 压缩图片的尺寸 ，自由压缩 ，
	 * path :图片路径
	 * maxSize :max(width,height),目标参数，想要压缩的长或宽的最大尺寸
	 */
	@Override
	public Bitmap compressImgResize(String path, int reqWidth , int reqHeight ,Context context) {
		
		Bitmap bitmap = null;
		try {
			//取得图片   
			InputStream is = context.getAssets().open(path);
			//对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
			//设为false表示把图片读进内存中
			options.inJustDecodeBounds = true;
			// 当options不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight 
			BitmapFactory.decodeStream(is, null, options);
			is.close();

			// 生成压缩的图片   
			int i = 0;
			
			while (true) {
				// 这一步是根据要设置的大小，使宽和高都能满足  .
				if ((options.outWidth >> i <= reqWidth)
						&& (options.outHeight >> i <= reqHeight)) {
					//  重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！   
					is = context.getAssets().open(path);
					// 这个参数表示 新生成的图片为原始图片的几分之一。   缩放的倍数，SDK中建议其值是2的指数值 
					// Math.pow 是计算2的i次方
					options.inSampleSize = (int) Math.pow(2.0D, i);
					//这里之前设置为了true，所以要改为false，否则就创建不出图片   
					options.inJustDecodeBounds = false;

					bitmap = BitmapFactory.decodeStream(is, null, options);
					break;
				}
				i += 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
		
	}

}
