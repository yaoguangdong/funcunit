package com.lefu.autocomplete.db.base;

/**
 * 搜索历史记录表
 * @author: yaoguangdong
 * @data: 2013-12-18
 */
public class TableSearchHistory {

	/**搜索历史记录表*/
	public static final String tableSearchHistory 	= "T_SEARCH_HISTORY";
	/**主键*/
	public static final String id           		= "_id"; 
	/**搜索过的内容*/
	public static final String searchWord 			= "SEARCH_WORD";   
	/**创建时间*/
	public static final String createTime 			= "CREATE_TIME";
	
	/**搜索历史记录表sql*/
	public static String createSQL(){
		StringBuilder script = new StringBuilder();
		
		script.append(" CREATE TABLE IF NOT EXISTS ");
		script.append(tableSearchHistory);
		script.append(" ( ");
		script.append(id);
		script.append(" integer PRIMARY KEY AUTOINCREMENT NOT NULL, ");
		script.append(searchWord);
		script.append("	varchar(30) NOT NULL, ");
		script.append(createTime);
		script.append("	datetime NOT NULL );");

		return script.toString();
	}
	
	public static String [] getColumns(){
		return new String[] { id, searchWord, createTime, createTime } ;
	}
	
}
