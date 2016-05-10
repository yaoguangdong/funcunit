package com.lefu.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
/**
 * 照片处理部分
 * 从Activity中提取出来，增加反编译的难度
 * @author: yaoguangdong
 * @data: 2014-2-5
 */
public class ProcessPhoto {

	public static Uri takePhotoEnd(Context cxt, long dateTaken, byte[] data, CameraMask maskView, CameraPreview cameraPreview){
		Uri uri = null ;
		Bitmap bitmap = null ;
    	byte [] result = null ;
        if (maskView.visiable){
        	bitmap = BitmapFactory.decodeByteArray(data, 0, data.length); 
            int x = (int)(maskView.mFrame.left * cameraPreview.picWratio) ;
            int y = (int)(maskView.mFrame.top * cameraPreview.picHratio) ;
            int width = (int)(maskView.mFrame.width() * cameraPreview.picWratio ) ;
            int height = (int)(maskView.mFrame.height() * cameraPreview.picHratio) ;
            Log.i("yaogd", "x=" + x + "y=" + y + "width=" + width + "height=" + height) ;
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, x, y, width, height );
            if (bitmap2 != bitmap) {
            	bitmap.recycle();
            	bitmap = bitmap2;
            }
            uri = insertImage(cxt, dateTaken , bitmap, null); 
        } else{
        	//压缩
        	ICompressImg compressImg = new CompressImgImpl();
        	bitmap = compressImg.compressImgResize(data, 768, 1024, cxt) ;
        	result = compressImg.compressBmpToFile(bitmap, 200, 300) ;
        	uri = insertImage(cxt, dateTaken , null, result); 
        }
        return uri; 
	}
	
	/** 
     * 存储图像并将信息添加入媒体数据库 
     */ 
    private static Uri insertImage(Context cxt, long dateTaken, Bitmap source, byte[] jpegData) { 
    	String path = Environment.getExternalStorageDirectory() + "/SpecialCameraData/";
        // 图像名称 
        String filename = DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString() + ".jpg"; 
        String filePath = path + filename; 
        OutputStream outputStream = null; 
        try { 
            File dir = new File(path); 
            if (!dir.exists()) { 
                dir.mkdirs(); 
            } 
            File file = new File(path, filename); 
            if (file.createNewFile()) { 
                outputStream = new FileOutputStream(file); 
                if (source != null) { 
                    source.compress(CompressFormat.JPEG, 100, outputStream); 
                } else { 
                    outputStream.write(jpegData); 
                } 
                outputStream.flush();
            } 
        } catch (Exception e) { 
            return null; 
        } finally { 
            if (outputStream != null) { 
                try { 
                    outputStream.close(); 
                } catch (Throwable t) { 
                } 
            } 
            //释放bitmap的内存
            if (source != null)
            {
            	source.recycle();
            	source = null;
            }
            
        } 
        ContentValues values = new ContentValues(7); 
        values.put(MediaStore.Images.Media.TITLE, filename); 
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename); 
        values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken); 
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); 
        values.put(MediaStore.Images.Media.DATA, filePath); 
        ContentResolver cr = cxt.getContentResolver() ;
        
//        //经过查询，得知现在的BUCKET_DISPLAY_NAME值为：SpecialCameraData
//        Uri uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
//		Cursor cursor = cr.query(uri, null, null, null, null) ;
//		String [] xx = cursor.getColumnNames() ;
//		cursor.moveToFirst() ;
//		int index = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME) ;
//		////A.i("BUCKET_DISPLAY_NAME:" + cursor.getString(index)) ;
		
        return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
    } 
    
    /**
     * 从媒体数据库中 删除图片文件夹
     * BUCKET_DISPLAY_NAME是存放图片文件夹的名称
     * The bucket display name of the image. 
     * This is a read-only property that is automatically computed from the DATA column. 
     */
    public static boolean deletePhoto(Context cxt){
    	ContentResolver cr = cxt.getContentResolver() ;
    	int rows = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "='SpecialCameraData'", null); 
    	return rows > 0 ;
    }
}
