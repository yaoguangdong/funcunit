package cn.farcanton.titlebarFootbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.farcanton.R;

public class CustomTitleFootBar extends Activity {
	TextView txt1;  
    TextView edittv;  
    ImageView imgbtn;  
    boolean isFirst = true;  
    boolean istopFirst = true;  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        //requestWindowFeature(Window.FEATURE_NO_TITLE); 
        
        // 先给Activity注册界面进度条功能      //在setContentView方法之前
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  
        setContentView(R.layout.custom_titlebar_footbar);
        // 在需要显示进度条的时候调用这个方法   //在setContentView方法之后
        setProgressBarIndeterminateVisibility(true); 
        //放在标题栏上的水平进度条不能设置进度条的最大刻度。这是因为系统已经将最大刻度值设为10000。
        setProgress(143) ;
        // 在不需要显示进度条的时候调用这个方法   
        //setProgressBarIndeterminateVisibility(false); 
        
        
        edittv = (TextView)findViewById(R.id.edit);  
        txt1 = (TextView)findViewById(R.id.txt1);  
        imgbtn = (ImageView)findViewById(R.id.undo);  
        edittv.setOnTouchListener(new OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                if(isFirst) {  
                    txt1.setText("编辑");  
                    Toast.makeText(CustomTitleFootBar.this, "bianji", 1000).show(); 
                    isFirst = false;  
                }else {  
                    txt1.setText("退出编辑");  
                    Toast.makeText(CustomTitleFootBar.this, "bianji", 1000).show(); 
                    isFirst = true;  
                }  
                return false;  
            }  
        });  
        imgbtn.setOnTouchListener(new OnTouchListener() {  
  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                if(istopFirst) {  
                    txt1.setText("编辑");  
                    istopFirst = false;  
                }else {  
                    txt1.setText("退出编辑");  
                    istopFirst = true;  
                }  
                return false;  
            }  
        });  
    }  
}
