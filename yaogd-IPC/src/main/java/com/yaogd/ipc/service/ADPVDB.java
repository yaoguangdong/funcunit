package com.yaogd.ipc.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yaogd.lib.A;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告曝光数据库
 * author yaoguangdong
 * 2015-12-7
 */
public class ADPVDB extends SQLiteOpenHelper {

	/** 广告专用数据库名称 */
	public final static String DB_NAME = "ad.db";
	/** 数据库的版本 */
	public final static int DB_VERSION = 1;

	private static ADPVDB instance;

	private ADPVDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public static ADPVDB getInstance(Context context) {
		if (instance == null) {
			instance = new ADPVDB(context);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// 广告报告记录缓存
		db.execSQL("create table IF NOT EXISTS t_adpv (\n"
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
				+ "pvid VARCHAR(50), \n"
				+ "begintime DOUABLE DEFAULT(0), \n"
				+ "endtime DOUABLE DEFAULT(0), \n"
				+ "type CHAR(1), \n"
				+ "imagetype VARCHAR(10) "
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			//
		}
	}

	/**
	 * 获取要发送的数据 从数据库剪切
	 *
	 * @return
	 */
	public List<ADPVEntity> getADPVData(int limit) {

		List<ADPVEntity> data = new ArrayList<ADPVEntity>();

		SQLiteDatabase db = null;
		try {
			db = instance.getReadableDatabase();
			Cursor cursor = db.rawQuery("select _id,pvid,begintime,endtime,type,imagetype from t_adpv limit ? offset 0;", new String[]{String.valueOf(limit)});

			while (cursor.moveToNext()) {
				ADPVEntity entity = new ADPVEntity();
				entity._id = cursor.getInt(0);
				entity.pvid = cursor.getString(1);// 请求广告的唯一标识（F5刷新）
				entity.beginTime = cursor.getLong(2);// 开始时间戳,单位毫秒
				entity.endTime = cursor.getLong(3);// 结束时间戳,单位毫秒
				entity.type = cursor.getInt(4);// 统计类型;0:曝光;1:可见
				entity.imgType = cursor.getString(5);// 开屏统计使用的图片素材类型jpg/gif

				data.add(entity);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db != null){
				db.close();
			}

		}
		return data;
	}

	public void insertADPVData(ADPVEntity[] adverts) {
		if(adverts != null && adverts.length > 0){
			SQLiteDatabase db = null;
			try {
				db = instance.getWritableDatabase();
				for(ADPVEntity p : adverts){
					//插入数据
					db.execSQL(
							"insert into t_adpv(pvid,begintime,endtime,type,imagetype) values(?,?,?,?,?)",
							new Object[] {p.pvid, p.beginTime, p.endTime, p.type , p.imgType}
					);
					A.d("insert pvid:" + p.pvid);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(db != null){
					db.close();
				}
			}
		}
	}

	public void deleteADPVData(int [] _ids) {
		if(_ids != null && _ids.length > 0){
			SQLiteDatabase db = null;
			try {
				db = instance.getWritableDatabase();
				StringBuilder sql = new StringBuilder();
				sql.append("delete from t_adpv where id in (");
				for(int i = 0; i < _ids.length; i++){
					sql.append(_ids[1]);
					if(i < _ids.length - 1){
						sql.append(",");
					}
				}
				sql.append(");");
				A.d("delete sql:" + sql.toString());
				db.execSQL(sql.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(db != null){
					db.close();
				}
			}
		}
	}

}