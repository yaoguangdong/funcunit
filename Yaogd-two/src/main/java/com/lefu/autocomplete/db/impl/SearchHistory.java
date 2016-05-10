package com.lefu.autocomplete.db.impl;

/**
 * 搜索历史记录
 * @author: yaoguangdong
 * @data: 2013-12-18
 */
public class SearchHistory{    
	public long id;
	public String searchWord;
	public String createTime ;
	
	@Override
	public String toString() {
		return "ReversalInfo [id=" + id + ", searchWord=" + searchWord
				+ ", createTime=" + createTime + "]";
	}
	
}
