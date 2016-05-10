package cn.farcanton.advancedListView;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.farcanton.R;

public class AdvancedListViewActivity extends Activity {

	private ListView mlistView ;
	private View listViewFooter_view;
	private Button mlistViewFooter_btn;
	private LinearLayout mlistViewFooter_progressBarLayout;
	private ListScrollListener mlistScrollListener;
	private VideoListAdapter mvideoListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_listview) ;
		mlistView = (ListView)findViewById(R.id.listView) ;
		initFooterView();
		mlistView.addFooterView(listViewFooter_view);
		List<VideoInfo> VInfos = new ArrayList<VideoInfo>();
		for(int i=0;i< 7;i++){
			VideoInfo vinfo = new VideoInfo("001", "视频"+i, 245, "http://adb.rmvb"+i, "http://image.jpg"+i);
			VInfos.add(vinfo);
		}
		
		mvideoListAdapter = new VideoListAdapter(this, VInfos, mlistView) ;
		mlistView.setAdapter(mvideoListAdapter) ;
		mlistScrollListener = new ListScrollListener(this,mlistView,mvideoListAdapter) ;
	
		mlistViewFooter_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mlistScrollListener.getmLastItem() == mvideoListAdapter.getCount()
                        && mlistScrollListener.getScrollState() == OnScrollListener.SCROLL_STATE_IDLE) {
                	mlistViewFooter_btn.setVisibility(View.GONE);
                	mlistViewFooter_progressBarLayout.setVisibility(View.VISIBLE);
                	
                	//动态加载新数据
                	
                	//更新视图
                	mvideoListAdapter.notifyDataSetChanged();
                    mlistViewFooter_btn.setVisibility(View.VISIBLE);
                    mlistViewFooter_progressBarLayout.setVisibility(View.GONE);

                }
            }
        });
		
		mlistView.setOnScrollListener(mlistScrollListener);
	}

	private void initFooterView()
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		listViewFooter_view = inflater.inflate(R.layout.advanced_listview_footer, null);
        mlistViewFooter_btn = (Button) listViewFooter_view.findViewById(R.id.btn_more);
        mlistViewFooter_progressBarLayout = (LinearLayout) listViewFooter_view.findViewById(R.id.linearlayout);
        mlistViewFooter_progressBarLayout.setVisibility(View.GONE);

	}
}
