package com.lefu.camera;

import android.content.Context;
import android.graphics.Bitmap;

public interface ICompressImg{
	/**
	 * 压缩图片的尺寸 
	 */
	public Bitmap compressImgResize(byte[] picData, int reqWidth, int reqHeight, Context context);
	/**
	 * 压缩质量
	 * @param minSize KB 压缩到最小质量，防止压缩过度导致的失真
	 * @param maxSize KB 压缩到的最大允许质量。
	 */
	public byte[] compressBmpToFile(Bitmap bmp, int minSize, int maxSize) ;
	
}