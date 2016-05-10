package cn.farcanton.advancedListView;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.farcanton.R;
import cn.farcanton.advancedListView.AsyncVideoInfoLoader.ImageCallback;
/**
 * 列表list适配器
 * @author Administrator
 *
 */
public class VideoListAdapter extends ArrayAdapter<VideoInfo>{

	private int count;
	private ListView listView;
	private AsyncVideoInfoLoader asyncVideoInfoLoader ;
	
	public int getCount(){
		return count;
	}
	public VideoListAdapter(Activity activity,List<VideoInfo> VInfos,ListView listView){
		super(activity,0,VInfos);
		this.listView = listView ;
		count = VInfos.size();
		asyncVideoInfoLoader = new AsyncVideoInfoLoader();
	}
	
	public View getView(int position,View convertView,ViewGroup parent){
		Activity activity = (Activity) getContext();
		// Inflate the views from XML
		View rowView = convertView;
		ListViewCache viewCache ;
		if(rowView == null){
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.advanced_listview_item, null) ;
			viewCache = new ListViewCache(rowView) ;
			//setTag,may be setTarget,set one Object.
			rowView.setTag(viewCache) ;
		}else{
			viewCache = (ListViewCache) rowView.getTag();
		}
		VideoInfo videoInfo = getItem(position) ;
		//load the image and set it on the ImageView
		String imageUrl = videoInfo.getV_imageUrl() ;
		ImageView imageView = viewCache.getImageView() ;
		imageView.setTag(imageUrl);
		asyncVideoInfoLoader.loadDrawable(imageUrl, new ImageCallback() {
			//这种回调函数，是由系统调用，什么时候调用，调用多少次都由"系统"决定。
			//在这里，由AsyncVideoInfoLoader中的handler+thead来调用，每一次调用会更新界面。
			@Override
			public void iamgeLoaded(Drawable imDrawable, String imageUrl) {
				//imageViewByTag在这种逻辑下就是imageView对象
				ImageView imageViewByTag = (ImageView)listView.findViewWithTag(imageUrl); 
				
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
		//set the text on the TextView
		TextView videoName = viewCache.getmVname() ;
		videoName.setText(videoInfo.getV_name()) ;
		
		TextView videoSize = viewCache.getmVsize() ;
		videoSize.setText(videoInfo.getV_size()+"Mb") ;
		
		return rowView;
	}
}
