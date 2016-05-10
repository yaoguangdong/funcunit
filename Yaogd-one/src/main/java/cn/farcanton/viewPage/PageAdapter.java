package cn.farcanton.viewPage;

import java.util.ArrayList;
import java.util.List;

import cn.farcanton.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class PageAdapter extends PagerAdapter{

	private List<View> views ;
	private List<String> titleList;
	private Context context ;
	PageAdapter(ArrayList<View> views,List<String> titleList,Context context){
		this.views = views;
		this.titleList = titleList;
		this.context = context;
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		//官方提示这样写
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		//返回页卡的数量
		return views.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position,
			Object object) {
		//删除页卡
		container.removeView(views.get(position));
	}

	@Override
	public int getItemPosition(Object object) {

		return super.getItemPosition(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {

		return titleList.get(position);//直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		//这个方法用来实例化页卡       
		View thisView = views.get(position);
		container.addView(thisView);//添加页卡
		Button weibo_button=(Button)(thisView.findViewById(R.id.btnviewpager));//这个需要注意，我们是在重写adapter里面实例化button组件的，如果你在onCreate()方法里这样做会报错的。
		if(weibo_button != null){
			weibo_button.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Toast.makeText(context, "click", 1000).show();
				}
			});
		}
		return thisView;
	}

}
