package com.yaogd.customView;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yaogd.R;

public class MyTableActivity extends Activity implements OnClickListener{

	private TableLayout appendTable;
	private LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custome_table);
		initView();
		
		refresh(initData()) ;
		
	}

	private void initView() {
		
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		appendTable = (TableLayout) findViewById(R.id.table_02) ;
		((TextView) findViewById(R.id.hold_day_tv)).setText("持有日");
		((TextView) findViewById(R.id.hold_amount_tv)).setText("持有量");
		((TextView) findViewById(R.id.settle_status_tv)).setText("结算状态");
		((TextView) findViewById(R.id.profit_tv)).setText("分润金额");
	}

	private List<SampleDataBean> initData(){
		List<SampleDataBean> list = new ArrayList<SampleDataBean>() ;
		for(int i= 0; i< 40 ; i++){
			SampleDataBean bean = new SampleDataBean() ;
			bean.holdAmount = i + "2万" ;
			bean.holdDay = "2014-12-01" ;
			bean.profit = "2" + i ;
			bean.settleStatus = "已结算" ;
			list.add(bean) ;
		}
		
		return list; 
	}
	
	private void refresh(List<SampleDataBean> list){
		if(list != null){
			for(SampleDataBean bean : list){
				TableRow row = (TableRow)(inflater.inflate(R.layout.custom_row, null));
				((TextView) row.findViewById(R.id.hold_day_row)).setText(bean.holdDay);
				((TextView) row.findViewById(R.id.hold_amount_row)).setText(bean.holdAmount);
				((TextView) row.findViewById(R.id.settle_status_row)).setText(bean.settleStatus);
				((TextView) row.findViewById(R.id.profit_row)).setText(bean.profit);
				appendTable.addView(row) ;
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		
	}

}
