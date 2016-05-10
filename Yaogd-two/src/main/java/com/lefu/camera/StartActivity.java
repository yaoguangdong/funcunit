package com.lefu.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.yaogd.R;

public class StartActivity extends Activity{

	private ImageView imageView ;
	
	@Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.take_photo ) ;
        imageView = (ImageView)findViewById(R.id.show_image01) ;
        
        // 调用自定义相机 
        Intent intent = new Intent(this, CameraActivity.class); 
        startActivityForResult(intent, 2); 
    } 

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        if (2 == requestCode) { // 自定义相机返回处理 
            // 拍照成功后，响应码是20 
            if (resultCode == 20) { 
                Bundle bundle = data.getExtras(); 
                // 获得照片uri 
                Uri uri = Uri.parse(bundle.getString("uri")); 
                // 获得拍照时间 
                long dateTaken = bundle.getLong("dateTaken"); 
                try { 
                    // 从媒体数据库获取该照片 
                    Bitmap cameraBitmap = MediaStore.Images.Media.getBitmap( 
                            getContentResolver(), uri); 
                    // 预览图像 
                    imageView.setImageBitmap(cameraBitmap);
//                    // 从媒体数据库删除该照片（按拍照时间） 
//                    getContentResolver().delete( 
//                    		MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
//                            MediaStore.Images.Media.DATE_TAKEN + "=" 
//                                    + String.valueOf(dateTaken), null); 
                    
                } catch (Exception e) { 
                    Log.e("yaogd", "error", e) ;
                } 
            } 
        } 
        super.onActivityResult(requestCode, resultCode, data); 
    }

	//文通证件识别的部分
//	protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
//	  {
//	    super.onActivityResult(paramInt1, paramInt2, paramIntent);
//	    Bitmap localBitmap2;
//	    File localFile;
//	    if ((paramInt1 == 11) && (paramInt2 == -1))
//	    {
//	      this.selectPath = readPreferences("FreeSystemCamera", "selectPath");
//	      Bitmap localBitmap1 = BitmapFactory.decodeFile(this.selectPath);
//	      float f1 = 0.0F;
//	      float f2 = 0.0F;
//	      if ((localBitmap1.getWidth() > 2048) || (localBitmap1.getHeight() > 1536))
//	      {
//	        float f3 = localBitmap1.getWidth();
//	        float f4 = localBitmap1.getHeight();
//	        f1 = (float)(0.9D * (2048.0F / f3));
//	        f2 = (float)(0.9D * (1536.0F / f4));
//	      }
//	      Matrix localMatrix = new Matrix();
//	      localMatrix.postScale(f1, f2);
//	      localBitmap2 = Bitmap.createBitmap(localBitmap1, 0, 0, localBitmap1.getWidth(), localBitmap1.getHeight(), localMatrix, true);
//	      localFile = new File(this.selectPath);
//	      if (localFile.exists())
//	        localFile.delete();
//	    }
//	    try
//	    {
//	      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFile));
//	      localBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, localBufferedOutputStream);
//	      localBufferedOutputStream.flush();
//	      localBufferedOutputStream.close();
//	    }
}
