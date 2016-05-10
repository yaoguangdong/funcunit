package cn.farcanton.listView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.farcanton.R;

public class ListViewInScrollViewActivity extends Activity {

	private CornerListView mListView = null;
	ArrayList<HashMap<String, String>> map_list1 = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view_in_scroll_view);
		ListView listView = (ListView) findViewById(R.id.moreItemsListView);
		MyAdapter adapter = new MyAdapter(this, map());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemListSelectedListener()) ;
		setListViewHeightBasedOnChildren(listView);
		
		//第二个listView
//		getDataSource();
//		SimpleAdapter adapter1 = new SimpleAdapter(this, map_list1,
//				R.layout.simple_list_item_1, new String[] { "name","ip" },
//				new int[] { R.id.item_name,R.id.item_ip });
//		mListView = (CornerListView) findViewById(R.id.list1);
//		mListView.setAdapter(adapter1);
//		mListView.setOnItemClickListener(new OnItemListSelectedListener());
		
		
//----------------------------------		
		
		
		//checkIsRoot() ; 
		
		//checkTelnet("127.0.0.1" ,"8080") ;
		
		
	}


	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount() ; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount()));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}

	private List<HashMap<String, String>> map() {
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ip", "10.10.110.34");
		map.put("name", "乐富支付后台");
		data.add(map);
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("ip", "10.10.110.34");
		map2.put("name", "乐富支付移动后台");
		data.add(map2);
		
		return data;
	}
	
	public ArrayList<HashMap<String, String>> getDataSource() {

		map_list1 = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();

		map1.put("name", "乐富支付后台");
		map1.put("ip", "10.10.110.34");
		map2.put("name", "乐富支付移动后台");
		map2.put("ip", "10.10.110.34");

		map_list1.add(map1);
		map_list1.add(map2);

		return map_list1;
	}

	class OnItemListSelectedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch(position){
				case 0 :
					break;
				case 1 :
					break;
			}
		}
	}
	
	private boolean checkTelnet(String ip ,String port){
		
		return true ;
	}
}
