package cn.farcanton.advancedListView;

import android.content.Context;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class ListScrollListener implements OnScrollListener{

	private static final String TAG="ListScrollListener";
	private int mLastItem = 0;  
	private int mCount;
	private int scrollState;// 全局变量，用来记录ScrollView的滚动状态，1表示开始滚动
	private ListView mListView;
	
	public ListScrollListener(Context context,ListView listView,VideoListAdapter listViewAdapter){
		this.mListView = listView;
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mLastItem = firstVisibleItem + visibleItemCount - 1;  
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}
	
	public int getmLastItem() {
		return mLastItem;
	}
	public void setmLastItem(int mLastItem) {
		this.mLastItem = mLastItem;
	}
	public int getmCount() {
		return mCount;
	}
	public void setmCount(int mCount) {
		this.mCount = mCount;
	}
	public int getScrollState() {
		return scrollState;
	}
	public void setScrollState(int scrollState) {
		this.scrollState = scrollState;
	}
	public ListView getmListView() {
		return mListView;
	}
	public void setmListView(ListView mListView) {
		this.mListView = mListView;
	}

}
