package com.yaogd.uniqueId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.lefu.A;

/**
 * @author yaoguangdong
 * 2014-8-15
 */
public class GenerateDeviceUniqueID {

	public void uniqueId(Context cxt){

		String uniqueID = "" ;
		//使用系统唯一ID代替
		TelephonyManager tm = (TelephonyManager)cxt.getSystemService(Context.TELEPHONY_SERVICE);
		if(tm != null){
			uniqueID = tm.getDeviceId();
		}
		StringBuilder sb = new StringBuilder(); 
		sb.append(Build.BOARD) ;
		sb.append(Build.BRAND) ;
		sb.append(Build.CPU_ABI) ;
		sb.append(Build.DEVICE) ;
		sb.append(Build.DISPLAY) ;
		sb.append(Build.FINGERPRINT) ;
		sb.append(Build.HOST) ;
		sb.append(Build.ID) ;
		sb.append(Build.MANUFACTURER) ;
		sb.append(Build.MODEL) ;
		sb.append(Build.PRODUCT) ;
		sb.append(Build.TAGS) ;
		sb.append(Build.TIME) ;
		sb.append(Build.TYPE) ;
		sb.append(Build.USER) ;
		sb.append(Build.TYPE) ;
		sb.append(Build.BOOTLOADER) ;
		sb.append(Build.CPU_ABI2) ;
		sb.append(Build.HARDWARE) ;
		sb.append(Build.SERIAL) ;
		sb.append(Build.VERSION.CODENAME) ;
		sb.append(Build.VERSION.INCREMENTAL) ;
		sb.append(Build.VERSION.RELEASE) ;
		sb.append(Build.VERSION.SDK_INT) ;
		sb.append(uniqueID) ;
		
		A.i("md5:" + getMd5Str(sb.toString()));
	}
	
	
	public static String getMd5Str(String source) {

		byte b[] = getMd5Bytes(source);
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
//		return buf.toString().substring(8, 24));// 16位的加密
		return buf.toString();// 32位的加密
	}

	public static byte[] getMd5Bytes(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			byte b[] = md.digest();
			return b;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
    
}
