package com.lefu8.mobile.db.impl;

/**
 * 消费冲正记录
 * Description :
 * @author: yaoguangdong
 * @data: 2013-12-18
 */
public class ReversalInfo{    
	public long id;
	public String batchNo;
	public String systemtTrackNo;
	/**000000000001格式的金额*/
	public String amount;
	public String terminalCode;
	public String shopNo;
	public String posEntryModeCode ;
	public String reverseCode ;
	public String createTime ;
	public ReverseStatus reverseStatus;
	
	public enum ReverseStatus{
		//可冲正状态
		ALIVE(0),
		//不可冲正状态
		DEAD(1);
		private int status ;
		ReverseStatus(int status){
			this.status = status ;
		}
		public int getValue(){
			return this.status ;
		}
		public static ReverseStatus getByValue(int status){
			if(status == 0){
				return ALIVE ;
			}else if(status == 1){
				return DEAD ;
			}else{
				return null ;
			}
		}
	}
	
	@Override
	public String toString() {
		return "ReversalInfo [id=" + id + ", batchNo=" + batchNo
				+ ", systemtTrackNo=" + systemtTrackNo + ", amount=" + amount
				+ ", terminalCode=" + terminalCode + ", merchantCode="
				+ shopNo + ", posEntryModeCode=" + posEntryModeCode
				+ ", reversaCode=" + reverseCode + ", createTime=" + createTime
				+ ", reverseStatus=" + reverseStatus + "]";
	}
}
