package cn.farcanton.compressBitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.farcanton.R;

public abstract class FrameActivity extends Activity{
	
	private static final String TAG = FrameActivity.class.getSimpleName() ;
	
	private ImageView rawImage, newImage;

	private TextView rawImg, newImg ;
	
	private Bitmap bitmap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.compress_bitmap);
		rawImage = (ImageView) findViewById(R.id.image1);
		newImage = (ImageView) findViewById(R.id.image2);
		rawImg = (TextView) findViewById(R.id.text1) ;
		newImg = (TextView) findViewById(R.id.text2) ;
		
	}
	
	public interface CompressImg{
		/**
		 * 压缩图片的尺寸 
		 */
		public Bitmap compressImgResize(String path , int reqWeight , int reqHeight, Context context);
		
	}
	
	/**
	 * 显示文本到控件上
	 */
	protected void showText(String content , int id){
		if(id == rawImg.getId())
			rawImg.setText(content);
		else
			newImg.setText(content);
	}
	/**
	 * 直接通过Drawable进行显示
	 * @param imageFile 图片文件
	 * @param id 显示的ImageView 控件的ID
	 */
	protected Drawable pasteImage(File imageFile , int id){
		Drawable da = null;
		if(imageFile != null){
			da = Drawable.createFromPath(imageFile.getAbsolutePath());
			if (da != null) {
				if(id == rawImage.getId())
					rawImage.setImageDrawable(da);
				else
					newImage.setImageDrawable(da);
			} else {
				Toast.makeText(FrameActivity.this, "file no exist ", 1000)
						.show();
			}
		}else{
			Toast.makeText(FrameActivity.this, "file no exist ", 1000)
			.show();
		}
		return da;
	}
	/**
	 * Drawable通过输入流的方式显示
	 * @param is 图片文件输入流
	 * @param id 显示的ImageView 控件的ID
	 */
	protected Drawable pasteImage(InputStream is , int id){
		Drawable da = null;
		if(is != null){
			da = Drawable.createFromStream(is, null);
			if (da != null) {
				if(id == rawImage.getId())
					rawImage.setImageDrawable(da);
				else
					newImage.setImageDrawable(da);
			} else {
				Toast.makeText(FrameActivity.this, "file no exist ", 1000)
						.show();
			}
		}else{
			Toast.makeText(FrameActivity.this, "file no exist ", 1000)
			.show();
		}
		return da;
	}
	/**
	 * Bitmap通过输入流的方式显示
	 * @param is 图片文件输入流
	 * @param id 显示的ImageView 控件的ID
	 * @param overLoadType 传入null
	 */
	protected Bitmap pasteImage(InputStream is , int id ,String overLoadType){
		Bitmap bm = null;
		if(is != null){
			bm = BitmapFactory.decodeStream(is);
			if (bm != null) {
				if(id == rawImage.getId())
					rawImage.setImageBitmap(bm) ;
				else
					newImage.setImageBitmap(bm) ;
			} else {
				Toast.makeText(FrameActivity.this, "file no exist ", 1000)
				.show();
			}
		}else{
			Toast.makeText(FrameActivity.this, "file no exist ", 1000)
			.show();
		}
		return bm;
	}
	/**
	 * Bitmap通过输入流的方式显示
	 * @param is 图片文件输入流
	 * @param id 显示的ImageView 控件的ID
	 * @param overLoadType 传入null
	 */
	protected void pasteImage(Bitmap bitmap ,int id){
		if(bitmap != null){
			if(id == rawImage.getId())
				rawImage.setImageBitmap(bitmap) ;
			else
				newImage.setImageBitmap(bitmap) ;
		}
	}
	/**
	 * 释放bitmap 所占的内存
	 * @param bitmap
	 */
	public void releaseBitmap(Bitmap bitmap){
		//解决Android Bitmap内存溢出最有效的办法就是，使用完bitmap之后，
		//调用bitmap对象的recycle()方法释放所占用的内存，以便于下一次使用。
		if(bitmap != null && ! bitmap.isRecycled() ){
			bitmap.recycle();  //回收图片所占的内存
			System.gc() ; //提醒系统及时回收
		}
	}
}
