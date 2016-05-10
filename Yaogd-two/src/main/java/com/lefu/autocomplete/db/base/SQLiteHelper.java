package com.lefu.autocomplete.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	/**数据库的名称*/
	public final static String DB_NAME = "search-history.db";
	/**数据库的版本*/
	public final static int DB_VERSION = 1;
	
	private static SQLiteHelper instance;
	
	private SQLiteHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public static SQLiteHelper getInstance(Context context)
	{
		if (instance == null) {
			instance = new SQLiteHelper(context);
		}
		return instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		////A.i("debug", "start create table");
		db.execSQL(TableSearchHistory.createSQL());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TableSearchHistory.tableSearchHistory);
			db.execSQL(TableSearchHistory.createSQL());
		}
	}

}
