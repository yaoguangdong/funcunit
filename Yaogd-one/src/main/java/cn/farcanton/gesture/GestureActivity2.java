package cn.farcanton.gesture;

import java.util.ArrayList;

import cn.farcanton.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.widget.Toast;

public class GestureActivity2 extends Activity {

	private GestureLibrary mGestureLibrary  ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture1);
        
      //1,手势库的加载
        mGestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if(!mGestureLibrary.load())
        	finish()  ;
        
        //2,用view去接收手势
        GestureOverlayView goView = (GestureOverlayView)findViewById(R.id.gestureOverlayView1)  ;
        goView.addOnGesturePerformedListener(new OnGesturePerformedListener() {
			
			@Override
			public void onGesturePerformed(GestureOverlayView arg0, Gesture arg1) {
				// TODO Auto-generated method stub
				//3,识别（匹配）手势
			
				ActivityManagerActivity.recongize(GestureActivity2.this, mGestureLibrary, arg1)  ;
			}
		}) ;
    }
    static class ActivityManagerActivity {

    	public static void recongize(Context curContext,GestureLibrary mGestureLibrary , Gesture arg1)
    	{
    		ArrayList<Prediction> predictions = mGestureLibrary.recognize(arg1)  ;
    		if(predictions.size() >0)
    		{
    			Prediction prediction = predictions.get(0) ;
    			//精度
    			if(prediction.score > 5.0)
    			{
    				Toast.makeText(curContext, prediction.name, 3000).show()  ;
    				if(prediction.name.equals("next"))
    				{
    					
    					
    				}else if(prediction.name.equals("end")){
    					
    				}
    					
    				else if(prediction.name.equals("first")){
    					
    				}
    				else if(prediction.name.equals("prev"))
    				{
    					
    				}
    				else if(prediction.name.equals("create"))
    				{
    					
    				}
    			}
    		}
    	}
    }
}
