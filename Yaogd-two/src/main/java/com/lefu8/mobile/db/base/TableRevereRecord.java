package com.lefu8.mobile.db.base;

/**
 * 冲正记录表
 * Description :
 * @author: yaoguangdong
 * @data: 2013-12-18
 */
public class TableRevereRecord {

	/**消费冲正记录表*/
	public static final String tableReversalRecord 	= "T_REVERSAL_RECORD";
	/**主键*/
	public static final String id           		= "_id"; 
	/**FLD 11  	系统跟踪号 ，流水号*/
	public static final String systemTrackNo 		= "SYSTEM_TTRACK_NO";   
	/**FLD 61.1	批处理号*/
	public static final String batchNo       		= "BATCH_NO";
	/**FLD 41  	受卡机终端标识码*/
	public static final String terminalCode  		= "TERMINAL_CODE";
	/**FLD 42  	受卡方标识码，店铺号*/
	public static final String shopNo      			= "SHOP_NO";
	/**FLD 04  	交易金额 （分）*/
	public static final String amount 				= "AMOUNT";
	/**FLD 22  	服务点输入方式码, PIN or NO_PIN*/
	public static final String posEntryModeCode 	= "POS_ENTRYMODE_CODE";
	/**FLD 39  	冲正原因码*/
	public static final String reverseCode 			= "REVERSE_CODE";
	/**冲正状态，0：需要冲正；1：冲正失败*/
	public static final String reverseStatus 		= "REVERSE_STATUS";
	/**创建时间*/
	public static final String createTime 			= "CREATE_TIME";
	
	/**消费冲正记录表sql*/
	public static String createSQL(){
		StringBuilder script = new StringBuilder();
		
		script.append(" CREATE TABLE IF NOT EXISTS ");
		script.append(tableReversalRecord);
		script.append(" ( ");
		script.append(id);
		script.append(" integer PRIMARY KEY AUTOINCREMENT NOT NULL, ");
		script.append(systemTrackNo);
		script.append("	varchar(6) NOT NULL, ");
		script.append(batchNo);
		script.append("	varchar(6) NOT NULL, ");
		script.append(terminalCode);
		script.append("	varchar(8) NOT NULL, ");
		script.append(shopNo);
		script.append("	varchar(15) NOT NULL, ");
		script.append(amount);
		script.append("	varchar(12) NOT NULL, ");
		script.append(posEntryModeCode);
		script.append("	varchar(4) NOT NULL, ");
		script.append(reverseCode);
		script.append(" varchar(2) NOT NULL DEFAULT '98', ");
		script.append(reverseStatus);
		script.append(" integer NOT NULL DEFAULT 0,");
		script.append(createTime);
		script.append("	datetime NOT NULL );");

		return script.toString();
	}
	
	public static String [] getColumns(){
		return new String[] { id, systemTrackNo, batchNo, terminalCode, shopNo, 
				amount, posEntryModeCode, reverseCode, reverseStatus, createTime } ;
	}
	
}
