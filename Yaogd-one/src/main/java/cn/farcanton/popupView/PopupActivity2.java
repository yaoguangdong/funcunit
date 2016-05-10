package cn.farcanton.popupView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;
import cn.farcanton.R;

public class PopupActivity2 extends Activity implements iRibbonMenuCallback{
	private TextView menu3; 
	private PopupWindow popupWindow;
	private ItemTextListAdapter adpater = null;
	private ItemTextListAdapter adpater2 = null;
	private ViewFlipper viewFlipper = null;
	private Button btn1 = null;
	private Button btn2 = null;
	
	private RibbonMenuView rbmView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.blank);
        setContentView(R.layout.custom_titlebar_footbar);

        menu3 = (TextView)findViewById(R.id.menu3);
        menu3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPopupWindow();
			}
		});
        
        rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView1);
        rbmView.setMenuClickCallback(this);
        rbmView.setMenuItems(R.menu.ribbon_menu);
        rbmView.showMenu(); 
        
    }

	@Override
	public void RibbonMenuItemClick(int itemId) {
		
		Log.i("yaogd", "RibbonMenuItemClick") ;
		
	}
	
	private void showPopupWindow(){
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View menuView = (View)mLayoutInflater.inflate(R.layout.popup_window_main, null, true);//弹出窗口包含的视图
		
		viewFlipper = (ViewFlipper) menuView.findViewById(R.id.popViewFlipper);
		btn1 = (Button)menuView.findViewById(R.id.popViewTitle1);
		btn2 = (Button)menuView.findViewById(R.id.popViewTitle2);
		btn1.setOnClickListener(new PopItemChangeListener(2));
		btn2.setOnClickListener(new PopItemChangeListener(1));
		
		initListData();
		ListView listView = (ListView)menuView.findViewById(R.id.listViewFirst);
		listView.setAdapter(adpater);
		ListView listView2 = (ListView)menuView.findViewById(R.id.listViewSecond);
		listView2.setAdapter(adpater2);
		
		int mScreenWidth = getWindowManager().getDefaultDisplay().getWidth(); 
        int mScreenHeight = getWindowManager().getDefaultDisplay().getHeight(); 
		popupWindow = new PopupWindow(menuView, mScreenWidth*2/3,mScreenHeight*3/5, true);//创建弹出窗口，指定大小   
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_bg_popup));//设置弹出窗口的背景
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);//设置窗口显示的动画效果
		int[] location = new int[2];  
		menu3.getLocationOnScreen(location);  
		popupWindow.showAtLocation(menu3,  Gravity.NO_GRAVITY, location[0]- menu3.getWidth(), location[1]-popupWindow.getHeight());//设置窗口显示的位置
		popupWindow.update();
		
	}
	private void initListData(){
		String[] mItems = getResources().getStringArray(R.array.popArray1);
		adpater = new ItemTextListAdapter(this,mItems);
		mItems = getResources().getStringArray(R.array.popArray2);
		adpater2 = new ItemTextListAdapter(this,mItems);		
	}
	private class PopItemChangeListener implements View.OnClickListener{
		private int index;
		public PopItemChangeListener(int index){
			this.index = index;
		}
		@Override
		public void onClick(View v) {
			
			viewFlipper.setDisplayedChild(index);
		}
		
	}
	/**
     * 数据适配器
     */
	public class ItemTextListAdapter extends SimpleAdapter
	{
		private Context mContext;
		private String[] mItems ;
		public ItemTextListAdapter(Context context,String[] items)
		{
			super(context, null, 0, null, null);
			this.mItems = items;
			this.mContext = context;
		}

		@Override
		public int getCount()
		{
			return mItems.length;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			ItemHolder holder;
			if (convertView == null )
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.popup_window_item, null, true);
				holder = new ItemHolder();
				holder.menuName = (TextView) convertView.findViewById(R.id.pop_tv_item);
				
				convertView.setTag(holder);
			}
			else
			{
				holder = (ItemHolder) convertView.getTag();
			}
			holder.menuName.setText(mItems[position]);
			return convertView;
		}
	}

	public static class ItemHolder
	{
		TextView menuName;
	}
	
}
