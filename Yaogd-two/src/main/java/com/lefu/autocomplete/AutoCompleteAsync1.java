package com.lefu.autocomplete;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.lefu.autocomplete.db.base.TableSearchHistory;
import com.lefu.autocomplete.db.impl.SQLiteDALImpl;
import com.lefu.autocomplete.db.impl.SearchHistory;
import com.yaogd.R;
/**
 * 模仿百度搜索框记录的自动提示
 * @author yaoguangdong
 * 2014-2-23
 */
public class AutoCompleteAsync1 extends Activity implements TextWatcher, OnClickListener  {

    private AutoCompleteTextView historyTips;  
    private Button keepWord;  
    private SQLiteDALImpl sQLiteDALImpl ;
    private Cursor cursor = null ;
    
    @Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
  
        setContentView(R.layout.auto_complete);  
        historyTips = (AutoCompleteTextView) findViewById(R.id.auto_complete_edit_text);  
        historyTips.addTextChangedListener(this);  
        keepWord = (Button) findViewById(R.id.keep_word);  
        keepWord.setOnClickListener(this);  
        sQLiteDALImpl = new SQLiteDALImpl(this) ;
        startManagingCursor(cursor) ;
    }

    public class MatchAdapter extends CursorAdapter  
    {  
        private LayoutInflater layoutInflater;  
  
        @Override  
        public CharSequence convertToString(Cursor cursor)  
        {  
            return cursor == null ? "" : cursor.getString(cursor.getColumnIndex(TableSearchHistory.searchWord));  
        }  
  
        private void setView(View view, Cursor cursor)  
        {  
            TextView tvWordItem = (TextView) view;  
            tvWordItem.setText(cursor.getString(cursor.getColumnIndex(TableSearchHistory.searchWord)));  
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
    }  
  
    //输入为空则使得确定按钮失效,只有输入了数据才使得按钮处于活动状态  
    @Override  
    public void afterTextChanged(Editable s)  
    {  
        String content = s.toString();  
        //判断字符串是否为空典型写法    
        if (TextUtils.isEmpty(content))
        {  
            keepWord.setEnabled(false);
        } else  
        {  
        	keepWord.setEnabled(true);  
            cursor = sQLiteDALImpl.queryReversal(content);  
  
            MatchAdapter dictionaryAdapter = new MatchAdapter(this, cursor, true);  
            historyTips.setAdapter(dictionaryAdapter);  
        }  
    }  
  
    @Override  
    public void beforeTextChanged(CharSequence s, int start, int count, int after)  
    {  
  
    }  
  
    @Override  
    public void onTextChanged(CharSequence s, int start, int before, int count)  
    {  
  
    }  
  
    @Override  
    public void onClick(View v)  
    {  
        sQLiteDALImpl.queryReversal(historyTips.getText().toString());  
        if (cursor.getCount() == 0)//没有同名记录,则插入数据  
        {  
        	SearchHistory searchHistory = new SearchHistory();
        	searchHistory.searchWord = historyTips.getText().toString() ;
        	searchHistory.createTime = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss").format(new Date()) ;
			sQLiteDALImpl.insertReversal(searchHistory );  
        } 
        cursor.moveToFirst();  
    }  
    
}
