package com.yaogd.customView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.lefu.A;
import com.yaogd.R;

public class BallPoolActivity extends Activity implements OnSeekBarChangeListener{

	private BallPoolImage bp ;
	private BallPoolText cp ;
	private SeekBar seekBar ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ball_pool_layout);
		cp = (BallPoolText) findViewById(R.id.b_p_text);
		bp = (BallPoolImage) findViewById(R.id.ball_p_img);
		
		seekBar = (SeekBar)findViewById(R.id.seek_bar19);
		seekBar.setOnSeekBarChangeListener(this) ;
		
		bp.setImageResource(R.drawable.ball_pool_progress_finish) ;
		
		//额外的测试WallpaperDrawable.传递到WallpaperDrawable中的canvas是整个屏幕的canvas。
		//所以效果就是按照整个屏幕的比例进行放大并剪裁。
//		WallpaperDrawable test1 = (WallpaperDrawable)findViewById(R.id.test_only_wallpaperDrawable) ;
//		test1.setBitmap(loadBitmapFormAssets()) ;
		
		RollBanner rollBanner = (RollBanner)findViewById(R.id.test_only_wallpaperDrawable2) ;
		rollBanner.setBitmap(loadBitmapFormAssets()) ;
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		float rate = progress / 100.0f ;
		String text = String.valueOf(progress) + "%";
		cp.setRateForShow(rate, text) ;
		bp.setRateForShow(rate, text) ;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	private Bitmap loadBitmapFormAssets() {
		byte[] imagedata = null;
		try {
//			InputStream inputStream = getAssets().open("miaomiao.jpg");
			InputStream inputStream = getAssets().open("roll_banner02.png");
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] byteBuffer = new byte[1024];
			int size = 0;
			try {
				while ((size = inputStream.read(byteBuffer)) != -1) {
					outputStream.write(byteBuffer, 0, size);
				}
				imagedata = outputStream.toByteArray();
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				A.e("U.readData", e) ;
			}
			
		} catch (IOException e) {
			A.e("", e) ;
		}

		A.i("imagedata size:" + (imagedata == null ? 0 : imagedata.length));

		return BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length, new Options());
		
	}
	
}
