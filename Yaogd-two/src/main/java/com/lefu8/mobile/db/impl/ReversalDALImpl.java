package com.lefu8.mobile.db.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lefu.A;
import com.lefu8.mobile.db.base.SQLiteDALBase;
import com.lefu8.mobile.db.base.TableRevereRecord;
import com.lefu8.mobile.db.impl.ReversalInfo.ReverseStatus;

public class ReversalDALImpl extends SQLiteDALBase{

	public ReversalDALImpl(Context context) {
		super(context);
	}
	
	/**
	 * 查询需要冲正的记录
	 */
	public ReversalInfo queryReversal(ReverseStatus status, String terminalCode, String shopNo) {
		//List<ReversalInfo> reversalList = null;
		ReversalInfo reversalInfo = null ;
		Cursor cursor = null;
		try {
			
			cursor = find( TableRevereRecord.tableReversalRecord, TableRevereRecord.getColumns(), 
					makeFindCond(status, terminalCode, shopNo),TableRevereRecord.id + " DESC" ) ;
			if (cursor != null) {
				//reversalList = new ArrayList<ReversalInfo>();
				if(cursor.moveToNext()) {
					reversalInfo = new ReversalInfo() ;
					reversalInfo.id = cursor.getInt(0);
					reversalInfo.systemtTrackNo = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.systemTrackNo));
					reversalInfo.batchNo = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.batchNo));
					reversalInfo.amount = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.amount));
					reversalInfo.terminalCode = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.terminalCode));
					reversalInfo.shopNo = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.shopNo));
					reversalInfo.posEntryModeCode = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.posEntryModeCode)) ;
					reversalInfo.reverseCode = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.reverseCode));
					reversalInfo.createTime = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.createTime));
					int reverseStatus = cursor.getInt(
							cursor.getColumnIndex(TableRevereRecord.reverseStatus));
					reversalInfo.reverseStatus = ReverseStatus.getByValue(reverseStatus) ;
				}
			} 
		} catch (Exception e) {
			////A.e("##queryReversal error ##", e) ;
			return null ;
		} finally {
			if (cursor != null){
				cursor.close();
			}
		}
		return reversalInfo;
	}
	
	/**
	 * 查询冲正失败的记录集合
	 */
	public List<ReversalInfo> queryReversals(ReverseStatus status) {
		List<ReversalInfo> reversalList = null;
		ReversalInfo reversalInfo = null ;
		Cursor cursor = null;
		try {
			String condition = TableRevereRecord.reverseStatus + 
					" = " + status.getValue();
			cursor = find( TableRevereRecord.tableReversalRecord, TableRevereRecord.getColumns(), 
					condition,TableRevereRecord.id + " DESC" ) ;
			if (cursor != null) {
				reversalList = new ArrayList<ReversalInfo>();
				while(cursor.moveToNext()) {
					reversalInfo = new ReversalInfo() ;
					reversalInfo.id = cursor.getInt(0);
					reversalInfo.systemtTrackNo = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.systemTrackNo));
					reversalInfo.batchNo = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.batchNo));
					reversalInfo.amount = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.amount));
					reversalInfo.terminalCode = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.terminalCode));
					reversalInfo.shopNo = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.shopNo));
					reversalInfo.posEntryModeCode = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.posEntryModeCode)) ;
					reversalInfo.reverseCode = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.reverseCode));
					reversalInfo.createTime = cursor.getString(
							cursor.getColumnIndex(TableRevereRecord.createTime));
					int reverseStatus = cursor.getInt(
							cursor.getColumnIndex(TableRevereRecord.reverseStatus));
					reversalInfo.reverseStatus = ReverseStatus.getByValue(reverseStatus) ;
					
					reversalList.add(reversalInfo) ;
				}
			} 
		} catch (Exception e) {
			////A.e("##queryReversal error ##", e) ;
			return null ;
		} finally {
			if (cursor != null){
				cursor.close();
			}
		}
		return reversalList;
	}
	
	/**
	 * 删除冲正记录
	 * 批次号、流水号和终端号作为查询条件
	 */
	public boolean deleteReversal(String batchNo,String systemTrackNo,String terminalCode) {
		try {
			return delete( TableRevereRecord.tableReversalRecord,
					makeDelCond(batchNo, systemTrackNo, terminalCode));
		} catch (Exception e) {
			////A.e("##deleteReversalById error ##", e) ;
			return false;
		} 
	}
	
	/**
	 * 添加一条新的冲正记录
	 */
	public boolean insertReversal(ReversalInfo reversalInfo) {
		ContentValues values = new ContentValues();
		try {
			values.put(TableRevereRecord.systemTrackNo, reversalInfo.systemtTrackNo );
			values.put(TableRevereRecord.batchNo, reversalInfo.batchNo);
			values.put(TableRevereRecord.amount, reversalInfo.amount);
			values.put(TableRevereRecord.terminalCode, reversalInfo.terminalCode);
			values.put(TableRevereRecord.shopNo, reversalInfo.shopNo);
			values.put(TableRevereRecord.posEntryModeCode, reversalInfo.posEntryModeCode);
			values.put(TableRevereRecord.reverseCode, reversalInfo.reverseCode);
			values.put(TableRevereRecord.reverseStatus, reversalInfo.reverseStatus.getValue());
			values.put(TableRevereRecord.createTime, reversalInfo.createTime);
			
			return insert(TableRevereRecord.tableReversalRecord, values);
		} catch (Exception e) {
			////A.e("##insertReversal error##", e) ;
			return false ;
		} 
	}

	/**
	 * 更新冲正记录
	 * @param ri
	 * @return
	 */
	public boolean updateReversal(long id, String reverseCode, ReverseStatus reverseStatus) {
		try {
			ContentValues values = new ContentValues();
			values.put(TableRevereRecord.reverseCode, reverseCode);
			values.put(TableRevereRecord.reverseStatus, reverseStatus.getValue());
			
			String[] args = { String.valueOf(id) };
			return getDataBase().update( TableRevereRecord.tableReversalRecord, values, "_id=?", args) >= 0;
		} catch (Exception e) {
			////A.e("##insertReversal error##", e) ;
			return false ;
		}
	}

	private String makeFindCond(ReverseStatus status, String terminalCode, String shopNo){
		StringBuffer condition = new StringBuffer();	
		condition.append(TableRevereRecord.reverseStatus);
		condition.append(" = ");
		condition.append(status.getValue());
		condition.append(" and ");
		condition.append(TableRevereRecord.terminalCode);
		condition.append(" ='");
		condition.append(terminalCode);
		condition.append("' and ");
		condition.append(TableRevereRecord.shopNo);
		condition.append(" ='");
		condition.append(shopNo);
		condition.append("'");
		
		return condition.toString() ;
	}
	
	private String makeDelCond(String batchNo,String systemTrackNo,String terminalCode){
		
		StringBuffer condition = new StringBuffer();	
		condition.append(TableRevereRecord.batchNo);
		condition.append(" ='");
		condition.append(batchNo);
		condition.append("' and ");
		condition.append(TableRevereRecord.systemTrackNo);
		condition.append("='");
		condition.append(systemTrackNo);
		condition.append("' and ");
		condition.append(TableRevereRecord.terminalCode);
		condition.append("='");
		condition.append(terminalCode);
		condition.append("'");
		
		return condition.toString() ;
	}

}
