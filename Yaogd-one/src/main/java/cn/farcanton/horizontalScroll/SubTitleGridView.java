package cn.farcanton.horizontalScroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.farcanton.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubTitleGridView extends LinearLayout {
	protected GridView gvSubTitle;
	protected PicureAdapter saSubTitle;// 适配器在下面有自定义类
	protected ArrayList<HashMap<String,String>> srcSubTitle;//数据源
	protected String[]titles;
	protected int[] images;
	protected Context context;
	public SubTitleGridView(Context context,int itemWidth, String[] titles, int[] images,boolean[] userType) {
		super(context);
		this.setOrientation(VERTICAL);
		gvSubTitle=new GridView(context);
		
		gvSubTitle.setColumnWidth(itemWidth);//设置每个分页按钮的宽度
		gvSubTitle.setNumColumns(GridView.AUTO_FIT);//分页按钮数量自动设置

		addView(gvSubTitle,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		saSubTitle=new PicureAdapter(titles, images, userType, context);
		gvSubTitle.setHorizontalSpacing(1);//各张图片之间的左右间隔
		gvSubTitle.setAdapter(saSubTitle);
		
	}
	
}

class PicureAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Picture> pictures;// Picture类型在下面有自定义类
	public PicureAdapter(String[] titles,int[] images,boolean[] userTypes,Context context){
		super();
		pictures=new ArrayList<Picture>();
		inflater=LayoutInflater.from(context);
		for(int i=0;i<images.length;i++){
			Picture picture=new Picture(titles[i], images[i], userTypes[i]);
			pictures.add(picture);
		}
	}
	//返回头像组合的个数
	public int getCount(){
		if(null!=pictures){
			return pictures.size();
		}else{
			return 0;
		}
	}
	//返回某一个头像组合（包括头像，sina图标，和用户昵称）
	public Object getItem(int position){
		return pictures.get(position);
	}
	//
	public long getItemId(int position){
		return position;
	}
	public View getView(int position,View convertView,ViewGroup parent){
		ViewHolder viewHolder;//下面有自定义的类（定义用户名，头像，微博图标的一个类）
		if(convertView==null){
			convertView=inflater.inflate(R.layout.picture_item, null);//头像微博昵称的布局
			viewHolder=new ViewHolder();
			
			viewHolder.title=(TextView)convertView.findViewById(R.id.title);
			viewHolder.image=(ImageView)convertView.findViewById(R.id.image_head);
			//viewHolder.userType=(ImageView)convertView.findViewById(R.id.image_sina);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.title.setText(pictures.get(position).getTitle());
		viewHolder.image.setImageResource(pictures.get(position).getImageId());
		if(pictures.get(position).isUserTypes()){
			//viewHolder.userType.setVisibility(View.VISIBLE);
		}else{
			//viewHolder.userType.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
}

class ViewHolder{
	public TextView title;
	public ImageView image;

}

class Picture {
	private String title;
	private int imageId;
	private boolean userTypes;
	public Picture() {
		super();
	}
	public Picture(String title, int imageId, boolean userTypes) {
		super();
		this.title = title;
		this.imageId = imageId;
		this.userTypes = userTypes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public boolean isUserTypes() {
		return userTypes;
	}
	public void setUserTypes(boolean userTypes) {
		this.userTypes = userTypes;
	}
	
	
}
