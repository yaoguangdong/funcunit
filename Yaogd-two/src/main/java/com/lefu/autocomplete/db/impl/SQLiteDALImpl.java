package com.lefu.autocomplete.db.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lefu.A;
import com.lefu.autocomplete.db.base.SQLiteDALBase;
import com.lefu.autocomplete.db.base.TableSearchHistory;

public class SQLiteDALImpl extends SQLiteDALBase{

	public SQLiteDALImpl(Context context) {
		super(context);
	}
	
	/**
	 * 查询记录
	 */
	public Cursor queryReversal(String searchWord) {
		Cursor cursor = null;
		try {
			
			cursor = find( TableSearchHistory.tableSearchHistory, TableSearchHistory.getColumns(), 
					makeFindCond(searchWord),TableSearchHistory.id + " DESC" ) ;
		} catch (Exception e) {
			////A.e("##queryReversal error ##", e) ;
			return null ;
		} 
		return cursor;
	}
	
	private String makeFindCond(String searchWord){
		StringBuffer condition = new StringBuffer();	
		condition.append(TableSearchHistory.searchWord);
		condition.append(" like '");
		condition.append(searchWord);
		condition.append("%' ");
		
		return condition.toString() ;
	}
	
	/**
	 * 添加一条新的搜索记录
	 */
	public boolean insertReversal(SearchHistory searchHistory) {
		ContentValues values = new ContentValues();
		try {
			values.put(TableSearchHistory.searchWord, searchHistory.searchWord );
			values.put(TableSearchHistory.createTime, searchHistory.createTime);
			
			return insert(TableSearchHistory.tableSearchHistory, values);
		} catch (Exception e) {
			////A.e("##insertReversal error##", e) ;
			return false ;
		} 
	}

}
