package com.lefu.autocomplete;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.yaogd.R;

/**
 * 异步从后台获取适配的数据，动态展示适配的结果
 * @author yaoguangdong 2014-2-23
 */
public class AutoCompleteAsync4 extends Activity {
	public String data;
	public String [] suggest = {"1111","1123","1234","2222"};
	public AutoCompleteTextView autoComplete;
	public ArrayAdapter<String> aAdapter;
	private Handler mHandler = new MyHandler();
	
	private class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData() ;
			switch (msg.what) {
				case 1:
					aAdapter = new ArrayAdapter<String>(AutoCompleteAsync4.this,
							R.layout.auto_complete_dropdown, suggest);
					autoComplete.setAdapter(aAdapter);
					aAdapter.notifyDataSetChanged();
					break;
				case 2:
					aAdapter = new ArrayAdapter<String>(AutoCompleteAsync4.this,
							R.layout.auto_complete_dropdown, suggest);
					autoComplete.setAdapter(aAdapter);
					aAdapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}

	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_complete);
		autoComplete = (AutoCompleteTextView) findViewById(R.id.auto_complete_edit_text);

		aAdapter = new ArrayAdapter<String>(AutoCompleteAsync4.this,
				R.layout.auto_complete_dropdown, suggest);
		autoComplete.setAdapter(aAdapter);
		
		autoComplete.addTextChangedListener(new TextWatcher() {
			
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i1, int i2) {
				// no need to do anything
			}

			public void onTextChanged(CharSequence charSequence, int i, int i1,
					int i2) {
				if (autoComplete.isPerformingCompletion()) {
					return;
				}
				String query = charSequence.toString();
				
				new GetData(query).start();
			}

			public void afterTextChanged(Editable editable) {
			}
		});

	}
	
	class GetData extends Thread{
		private String matchText = null ;
		
		GetData(String matchText){
			this.matchText = matchText ;
		}
		public void setSearchText(){
			
		}
		//需要防止连续启动线程、请求数据、清空消息队列
		@Override
		public void run() {
			super.run();
			try {
        		//模拟异步请求远程数据
				Thread.sleep(2000) ;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
        	//模拟远程匹配
        	if(matchText.startsWith("1")){
        		getOneTest() ;
        		
        		Message msg = new Message();
        		msg.what = 1 ;
        		mHandler.sendMessage(msg ) ;
        	} else if(matchText.startsWith("2")){
        		getTwoTest() ;
        		Message msg = new Message();
        		msg.what = 2 ;
        		mHandler.sendMessage(msg ) ;
        	}
        	
		}
	}
	
	
	/**
     * 当用户输入1时的匹配测试用例
     * @return
     */
	private void getOneTest(){
		suggest = new String [4] ;
		suggest[0] = "1111";
		suggest[1] = "1123";
		suggest[2] = "1234";
		suggest[3] = "2222";
	}
	/**
	 * 当用户输入2时的匹配测试用例
	 * @return
	 */
	private void getTwoTest(){
		suggest = new String [4] ;
		suggest[0] = "2222";
		suggest[1] = "2234";
		suggest[2] = "2345";
		suggest[3] = "3333";
	}
	
}
