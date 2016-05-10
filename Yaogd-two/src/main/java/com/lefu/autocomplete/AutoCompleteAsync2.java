package com.lefu.autocomplete;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yaogd.R;
/**
 * 异步从后台获取适配的数据，动态展示适配的结果
 * @author yaoguangdong
 * 2014-2-23
 */
public class AutoCompleteAsync2 extends Activity {
	
	private AutoCompleteTextView historyTips;  
    private Cursor cursor = null ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_complete);  
        historyTips = (AutoCompleteTextView) findViewById(R.id.auto_complete_edit_text);  
        historyTips.setAdapter(new MatchAdapter(this,null,true)) ;
        startManagingCursor(cursor) ;
    }
    private class MatchAdapter extends CursorAdapter {  
          
    	private LayoutInflater layoutInflater;  
    	  
        @Override  
        public CharSequence convertToString(Cursor cursor)  
        {  
            return cursor == null ? "" : cursor.getString(1);  
        }  
  
        private void setView(View view, Cursor cursor)  
        {  
            TextView tvWordItem = (TextView) view;  
            tvWordItem.setText(cursor.getString(1));  
        }  
  
        @Override  
        public void bindView(View view, Context context, Cursor cursor)  
        {  
            setView(view, cursor);  
        }  
  
        @Override  
        public View newView(Context context, Cursor cursor, ViewGroup parent)  
        {  
            View view = layoutInflater.inflate(R.layout.auto_complete_dropdown, null);  
            setView(view, cursor);  
            return view;  
        }  
  
        public MatchAdapter(Context context, Cursor c, boolean autoRequery)  
        {  
            super(context, c, autoRequery);  
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        }  
        
        @Override  
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {  
        	
            //判断字符串是否为空典型写法    
            if ( ! TextUtils.isEmpty(constraint))
            {  
            	try {
            		//模拟异步请求远程数据
					Thread.sleep(2000) ;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	//模拟远程匹配
            	if(constraint.toString().startsWith("1")){
            		return getOneTest() ;
            	} else if(constraint.toString().startsWith("2")){
            		return getTwoTest() ;
            	}
            } 
            return null ;
        }  
    }
    /**
     * 当用户输入1时的匹配测试用例
     * @return
     */
    private MatrixCursor getOneTest(){
    	MatrixCursor cursor = null ;
    	String[] tableCursor = new String[] { "_id","name"};
    	cursor = new MatrixCursor(tableCursor);
    	cursor.addRow(new Object[] { 1,"1111"});
    	cursor.addRow(new Object[] { 2,"1112"});
    	return cursor;
    }
    /**
     * 当用户输入2时的匹配测试用例
     * @return
     */
    private MatrixCursor getTwoTest(){
    	MatrixCursor cursor = null ;
    	String[] tableCursor = new String[] { "_id","name"};
    	cursor = new MatrixCursor(tableCursor);
    	cursor.addRow(new Object[] { 1,"2222"});
    	cursor.addRow(new Object[] { 2,"2223"});
    	return cursor;
    }
    
}
