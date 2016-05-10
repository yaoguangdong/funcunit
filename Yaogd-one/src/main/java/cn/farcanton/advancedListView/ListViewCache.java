package cn.farcanton.advancedListView;

import cn.farcanton.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 列表item布局视图类
 * @author Administrator
 *
 */
public class ListViewCache {
	private View baseView;
	private TextView mVname;
	private TextView mVsize;
	private ImageView imageView;
	
	public ListViewCache(View baseView) {
		this.baseView = baseView; 
	}
	public TextView getmVname() {
		if(mVname == null){
			mVname = (TextView) baseView.findViewById(R.id.v_name) ;
		}
		return mVname;
	}
	public TextView getmVsize() {
		if(mVsize == null){
			mVsize = (TextView) baseView.findViewById(R.id.v_size) ;
		}
		return mVsize;
	}
	public ImageView getImageView() {
		if(imageView == null){
			imageView = (ImageView) baseView.findViewById(R.id.v_image) ;
		}
		return imageView;
	}
	
	
}
