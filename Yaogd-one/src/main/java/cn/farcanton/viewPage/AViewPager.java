package cn.farcanton.viewPage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import cn.farcanton.R;

public class AViewPager extends Activity{
	private View view1, view2;//需要滑动的页卡
	//在这里需要说明一下，在上面的图片中我们看到了，PagerTabStrip，PagerTitleStrip，
	//他们其实是viewpager的一个指示器，前者效果就是一个横的粗的下划线，后者用来显示各个页卡的标题，
	//当然而这也可以共存。在使用他们的时候需要注意，看下面的布局文件，要在android.support.v4.view.ViewPager里面添加
	//android.support.v4.view.PagerTabStrip以及android.support.v4.view.PagerTitleStrip。
	private PagerTabStrip pagerTabStrip;//一个viewpager的指示器，效果就是一个横的粗的下划线
	private List<String> titleList;//viewpager的标题
   
	private ViewPager viewPager1;
	private ArrayList<View> views;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.view_pager_main);
		super.onCreate(savedInstanceState) ;
		
		viewPager1 = (ViewPager) findViewById(R.id.viewpager) ;
		
		pagerTabStrip=(PagerTabStrip) findViewById(R.id.pagertab);
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.gold)); 
		pagerTabStrip.setDrawFullUnderline(false);
		pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.azure));
		pagerTabStrip.setTextSpacing(50);
		
		views = new ArrayList<View>();
		LayoutInflater lf = getLayoutInflater().from(this);
		
		view1 = lf.inflate(R.layout.view_pager1, null);
		view2 = lf.inflate(R.layout.view_pager2, null);
		views.add(view1);
		views.add(view2);
		
		titleList = new ArrayList<String> ();
		titleList.add("title1") ;
		titleList.add("title2") ;
		titleList.add("title3") ;
		
        viewPager1.setAdapter(new PageAdapter(views,titleList,AViewPager.this));
        
        viewPager1.setOnPageChangeListener(new OnPageChangeListener() {
             //页面选择
        	@Override
            public void onPageSelected(int position) {
               Toast.makeText(AViewPager.this, String.valueOf(position+1)+"/"+String.valueOf(views.size()), 1000).show() ;
            }
  
            @Override
            public void onPageScrollStateChanged(int state) {
            }
  
            @Override
            public void onPageScrolled(int position,float positionOffset, int positionOffsetPixels) {
            	
            }
        });

	}
}
