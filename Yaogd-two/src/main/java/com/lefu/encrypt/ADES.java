package com.lefu.encrypt;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import com.lefu.A;

/**
 * AES + DES
 * 永远不要尝试着自己写加密程序，除非你真正熟悉加密这个领域。
 * @author yaoguangdong 2014-2-17
 */
public class ADES {

	public static final String ALGORITHM = "AES"; 
	 
    public static byte[] decrypt(byte[] data, String key) throws Exception {  
        Key k = toKey(Base64.decode(key, Base64.DEFAULT));  
  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.DECRYPT_MODE, k);  
  
        return cipher.doFinal(data);  
    }  
  
    public static byte[] encrypt(byte[] data, String key) throws Exception {
    	//生成密钥对象
        Key k = toKey(Base64.encodeToString(key.getBytes(), Base64.DEFAULT).getBytes());  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        //初始化加密工具
        cipher.init(Cipher.ENCRYPT_MODE, k);  
        //进行加密
        return cipher.doFinal(data);  
    }  
  
    private static Key toKey(byte[] key) throws Exception {  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
  
        if("AES".equals(ALGORITHM)){
        	return new SecretKeySpec(key, ALGORITHM);  
        } else{
        	return keyFactory.generateSecret(new DESKeySpec(key)) ;  
        }
    }  
    
    public static String initKey() throws Exception {  
        return initKey(null);  
    }  
      
    public static String initKey(String seed) throws Exception {  
        SecureRandom secureRandom = null;  
  
        if (seed != null) {  
            secureRandom = new SecureRandom(Base64.decode(seed, Base64.DEFAULT));  
            //下面也行
            secureRandom = SecureRandom.getInstance("SHA1PRNG");  
            secureRandom.setSeed(Base64.decode(seed, Base64.DEFAULT)) ;
        } else {  
        	secureRandom = new SecureRandom() ;
        }  
  
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);  
        keyGen.init(256,secureRandom);  
  
        SecretKey secretKey = keyGen.generateKey();  
        String initKey = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT) ;
        ////A.i("initKey:" + initKey) ;
        
        return initKey;  
    }  
    
}