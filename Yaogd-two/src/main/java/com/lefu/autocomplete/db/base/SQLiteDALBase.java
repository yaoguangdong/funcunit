package com.lefu.autocomplete.db.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDALBase {
	
	private Context mContext;
	private SQLiteDatabase mDataBase;
	
	public SQLiteDALBase(Context context)
	{
		this.mContext = context;
	}
	
	protected Context getContext()
	{
		return this.mContext;
	}
	
	public SQLiteDatabase getDataBase()
	{
		if(mDataBase == null)
		{
			mDataBase = SQLiteHelper.getInstance(mContext).getWritableDatabase();
		}
		return mDataBase;
	}
	
	protected Boolean insert(String tableName, ContentValues values)
	{
		return getDataBase().insert(tableName, null, values) >= 0;
	}
	
	protected Boolean delete(String tableName, String condition)
	{
		return getDataBase().delete(tableName, " 1=1 " + condition, null) >= 0;
	}
	
	protected Cursor find(String table, String[] columns, String selection, String orderBy)
	{
		return getDataBase().query(table, columns, selection, null, null, null, orderBy);
	}
	
	protected Cursor execSql(String sql)
	{
		return getDataBase().rawQuery(sql, null);
	}
}
