package cn.farcanton.gridView;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.farcanton.R;
import cn.farcanton.advancedListView.AsyncVideoInfoLoader;
import cn.farcanton.advancedListView.AsyncVideoInfoLoader.ImageCallback;

public class ImageAdapter extends BaseAdapter{
	private Context mContext = null;
	//视图对象
	private GridView gridView = null ;
	//数据源
	private ArrayList<VideoEntity> v_list = null;
	//带缓存的异步图片加载器
	private AsyncVideoInfoLoader asyncVideoInfoLoader ;
	
	public ImageAdapter(Context context,GridView gridView) {
		super();
		this.mContext = context;
		this.gridView = gridView;
		asyncVideoInfoLoader = new AsyncVideoInfoLoader();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;
		if (convertView == null)
		{
			
			holder = new ViewHolder();
			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gird_view_item, null);
            holder.imageView = (ImageView)convertView.findViewById(R.id.itemImage);
			holder.textView = (TextView)convertView.findViewById(R.id.itemText);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		VideoEntity v_entity = v_list.get(position) ;

		holder.textView.setText(v_list.get(position).getName());
		
		//load the image and set it on the ImageView
		String imageUrl = v_entity.getVideoImageUrl();
		//使用imageUrl标记回调对象
		holder.imageView.setTag(imageUrl);
		
		asyncVideoInfoLoader.loadDrawable(imageUrl, new ImageCallback() {
			//这种回调函数，是由系统调用，什么时候调用，调用多少次都由"系统"决定。
			//在这里，由AsyncVideoInfoLoader中的handler+thead来调用，每一次调用会更新界面。
			@Override
			public void iamgeLoaded(Drawable imDrawable, String imageUrl) {
				//imageViewByTag在这种逻辑下就是imageView对象
				ImageView imageViewByTag = (ImageView)gridView.findViewWithTag(imageUrl); 
				
				if(imageViewByTag != null){
					if(imDrawable != null){
						imageViewByTag.setImageDrawable(imDrawable) ;
					}
					else{
						imageViewByTag.setImageResource(R.drawable.icon);
					}
				}
			}
		}) ;
		
		return convertView;
	}
	private class ViewHolder{
	    ImageView imageView;
	    TextView textView;
	}
	
	
	@Override
	public int getCount() {
		return v_list.size();
	}

	@Override
	public Object getItem(int position) {
		return v_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<VideoEntity> getV_list() {
		return v_list;
	}

	public void setV_list(ArrayList<VideoEntity> v_list) {
		this.v_list = v_list;
	}

}
