package cn.farcanton.animation;

import cn.farcanton.R;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class FrameAnimationTest extends Activity {
	
	private AnimationDrawable ad = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_animation);
	    
		final ImageView imageView = (ImageView)findViewById(R.id.imageView_frameAnim);
		
		ad = (AnimationDrawable) imageView.getBackground();
		
		Button btn = (Button)findViewById(R.id.frameAnim_btn);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//ad.start();
				
				RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
		                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		        anim.setInterpolator(new LinearInterpolator());
		        anim.setRepeatCount(Animation.INFINITE);
		        anim.setDuration(1400);
		        imageView.startAnimation(anim);
		        
			}
		});
		
		
		
        
		
	}
}
