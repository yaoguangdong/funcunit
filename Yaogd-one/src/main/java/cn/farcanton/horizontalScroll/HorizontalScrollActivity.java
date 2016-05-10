package cn.farcanton.horizontalScroll;



import cn.farcanton.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class HorizontalScrollActivity extends Activity {
  public View view;
  public String[] titles = new String[] { "aaa", "aaa", "aaa  ", "aaa", "aaa",
			"aaa", "aaa  ", "aaa", "aaa", "aaa", "aaa", "aaa", "aaa", "aaa",
			"aaa", "aaa", "aaa", "aaa、", "aaa", "aaa" };
  public int[] images = new int[] { R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon };
  public boolean[] userType = new boolean[] { false ,true ,true ,false ,false ,
			false ,false ,false ,true ,true, true ,false ,true ,true ,false ,
			false ,true ,true ,true ,true };
     private ImageButton left_IB; //向左滑动
	private ImageButton right_IB; //向右滑动
	int per=5;
	private MyHorizontalScrollView horzontalview;//横屏滚动的自定义VIEW
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(view==null){
        	LayoutInflater li=this.getLayoutInflater();
        	view=li.inflate(R.layout.horizontal_scroll, null);
        	setContentView(view);
        	
        	int itemWidth=300/per+5;
        	int width=itemWidth * titles.length+500;
        	SubTitleGridView subtitle=new SubTitleGridView(this, itemWidth, titles, images, userType);//自定义用于滚动的
        	AppUtils.writeLog("mxt", "width:"+width);
        	LinearLayout layout1=(LinearLayout) view.findViewById(R.id.layout1);//第一行LinearLayout横向滚动轴    	
        	LinearLayout content1=new LinearLayout(this);
        	content1.setLayoutParams(new LayoutParams(width,80));//控制一整行图片显示的宽高
        	content1.addView(subtitle);
        	layout1.addView(content1);
        	
        	SubTitleGridView subtitle2=new SubTitleGridView(this, itemWidth, titles, images, userType);
        	LinearLayout layout2=(LinearLayout) view.findViewById(R.id.layout2);
        	LinearLayout content2=new LinearLayout(this);
        	content2.setLayoutParams(new LayoutParams(width,80));//控制一整行图片显示的宽高
        	content2.addView(subtitle2);
        	layout2.addView(content2);
        	
        	
        	
        	//LinearLayout score_list=(LinearLayout) view.findViewById(R.id.layout);
        	left_IB=(ImageButton) view.findViewById(R.id.left);
            right_IB=(ImageButton)view.findViewById(R.id.right);
        	
        	 horzontalview=(MyHorizontalScrollView) view.findViewById(R.id.horizontalview);//横屏滚动的自定义VIEW
        	horzontalview.setOnHorizontalScrollViewEvent(new OnHorizontalScrollViewScrollfinish() {
				
				@Override
				public void onRightChange(int l, int maxWidth) {
					if(maxWidth==1){
						right_IB.setVisibility(View.GONE);
					}else{
						right_IB.setVisibility(View.VISIBLE);
					}
					
				}
				
				@Override
				public void onLeftChange(int l) {
					if(l<=1){
						left_IB.setVisibility(View.GONE);
					}else{
						left_IB.setVisibility(View.VISIBLE);
					}
					
				}
			});
        	AppUtils.writeLog("mxt","1111111111:  "+left_IB);
        	left_IB.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					horzontalview.fling(-600);
				}
			});
        	right_IB.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					horzontalview.fling(600);
					
				}
			});
        }
    }
}