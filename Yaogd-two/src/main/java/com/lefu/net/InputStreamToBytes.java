package com.lefu.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 从流中读取数据
 * @author yaoguangdong
 * 2014-8-12
 */
public class InputStreamToBytes {

	public static byte[] read(InputStream inStream){
		byte [] result = null ;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024] ;
			int len = 0 ;
			while((len = inStream.read(buffer)) != -1){
				outputStream.write(buffer, 0 ,len);
			}
			inStream.close();
			result = outputStream.toByteArray() ; 
		} catch (Exception e) {
			result = null ;
		}
		return result ;
	}
	
}
