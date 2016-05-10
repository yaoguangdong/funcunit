package cn.farcanton.compressBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

/**
 * 压缩图片的质量
 * yaogd1633@163.com
 * time:2013-10-11
 */
public class CompressImgResizeFree4 implements FrameActivity.CompressImg{

	/**
	 * 压缩图片的质量
	 * scale 设置缩小比例
	 */
	public Bitmap compressImgQuality(Bitmap image,double scale) {
		//获得Bitmap的高和宽 
    	int bmpWidth=image.getWidth(); 
    	int bmpHeight=image.getHeight();
    	
    	//计算出这次要缩小的比例 
    	float scaleWidth=(float)(bmpWidth*scale); 
    	float scaleHeight=(float)(bmpHeight*scale); 
    	 
    	//产生resize后的Bitmap对象 
    	Matrix matrix=new Matrix(); 
    	matrix.postScale(scaleWidth, scaleHeight); 
    	Bitmap resizeBmp=Bitmap.createBitmap(image, 0, 0, bmpWidth, bmpHeight, matrix, true);
    	
    	int [] colors = {Color.BLACK,Color.WHITE};
    	
    	Bitmap resizeBmp2=Bitmap.createBitmap(colors,bmpWidth, bmpHeight,Bitmap.Config.RGB_565);
    	
    	return resizeBmp;
	}
	
	public Bitmap compressImgQuality(Bitmap image, int quality) {
		return null;
	}
	
	@Deprecated
	@Override
	public Bitmap compressImgResize(String path, int reqWeight, int reqHeight,
			Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
