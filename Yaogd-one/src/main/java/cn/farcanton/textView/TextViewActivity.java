package cn.farcanton.textView;

import org.xml.sax.XMLReader;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import cn.farcanton.R;

public class TextViewActivity extends Activity {
   
	private TextView mtextView,mtextView2,mtextView3;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_view);
        mtextView = (TextView)findViewById(R.id.text1) ;
        mtextView2 = (TextView)findViewById(R.id.text1) ;
        mtextView3 = (TextView)findViewById(R.id.text111) ;
        
        mtextView3.setText("the english type face CrimsonVermillion.ttf");
        ImageGetter imageGetter = new Html.ImageGetter() {
			
			@Override
			public Drawable getDrawable(String arg0) {
				BitmapDrawable bitd = (BitmapDrawable)getResources().getDrawable(R.drawable.icon) ; 
				//bitmapDrawable 必须设置bound才能使用
				bitd.setBounds(0, 0, bitd.getIntrinsicWidth(), bitd.getIntrinsicHeight()) ;
				return bitd ;
			}
		};
		
		mtextView.setText(Html.fromHtml("<b>中国</b><nihao>hello</nihao><img src='127.0.0.1:80/TCMweb1.2/cat.jpg'></img><h1>北京</h1><font color='#11FF33'>朝阳</font>")) ;
		mtextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰 
		
		TagHandler tagHandler = new Html.TagHandler() {
			
			@Override
			public void handleTag(boolean opening, String tag, Editable output,
					XMLReader arg3) {
				Toast.makeText(TextViewActivity.this, tag, Toast.LENGTH_SHORT).show();
				
			}
		};
		//使用HTML编码这个字符串 String android.text.TextUtils.htmlEncode(String s)
		//mtextView.setText(Html.fromHtml("<b>中国</b><nihao>hello</nihao><img src='127.0.0.1:80/TCMweb1.2/cat.jpg'/><h1>北京</h1><font color='#11FF33'>朝阳</font>",imageGetter,tagHandler)) ;

		SpannableStringBuilder ssb = new SpannableStringBuilder("大家好，努力学习天天向上，我的人生我做主") ;
		ImageSpan imageSpan = new ImageSpan(TextViewActivity.this,R.drawable.icon);
		
		ClickableSpan clickspan = new ClickableSpan(){

			@Override
			public void onClick(View arg0) {
				Toast.makeText(TextViewActivity.this, "nihao", Toast.LENGTH_SHORT).show();
				
			}
			
		};
		ssb.setSpan(imageSpan, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) ;
		ssb.setSpan(clickspan, 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		mtextView2.setText(ssb);
		
		
		ToggleButton tb = (ToggleButton)findViewById(R.id.toggleBtn) ;
		tb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击图片切换，也可以由selector.xml完成
				
				
			}
		});
		
		
    }
    
    
}