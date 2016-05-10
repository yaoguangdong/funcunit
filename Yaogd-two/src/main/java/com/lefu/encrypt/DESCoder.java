package com.lefu.encrypt;
import java.security.Key;  
import java.security.SecureRandom;  
  
import javax.crypto.Cipher;  
import javax.crypto.KeyGenerator;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.DESKeySpec;  

/**
 * 大体上分为双向加密和单向加密，而双向加密又分为对称加密（加密解密密钥相同）
 * 和非对称加密（公钥加密私钥解密）
 * 复杂的对称加密（DES、PBE）、非对称加密算法：
    DES(Data Encryption Standard，数据加密算法)
    PBE(Password-based encryption，基于密码验证)
    RSA(算法的名字以发明者的名字命名：Ron Rivest, AdiShamir 和Leonard Adleman)
    DH(Diffie-Hellman算法，密钥一致协议)
    DSA(Digital Signature Algorithm，数字签名)
    ECC(Elliptic Curves Cryptography，椭圆曲线密码编码学) 
 * @author yaoguangdong
 * 2014-2-17
 */
public abstract class DESCoder extends Coder {  
      
    public static final String ALGORITHM = "DES";  
  
    private static Key toKey(byte[] key) throws Exception {  
        DESKeySpec dks = new DESKeySpec(key);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
        SecretKey secretKey = keyFactory.generateSecret(dks);  
  
        // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码  
        // SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);  
  
        return secretKey;  
    }  
  
      
    public static byte[] decrypt(byte[] data, String key) throws Exception {  
        Key k = toKey(Base64Coder.decode(key));  
  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.DECRYPT_MODE, k);  
  
        return cipher.doFinal(data);  
    }  
  
      
    public static byte[] encrypt(byte[] data, String key) throws Exception {  
        Key k = toKey(Base64Coder.encodeString(key).getBytes());  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.ENCRYPT_MODE, k);  
  
        return cipher.doFinal(data);  
    }  
  
      
    public static String initKey() throws Exception {  
        return initKey(null);  
    }  
  
      
    public static String initKey(String seed) throws Exception {  
        SecureRandom secureRandom = null;  
  
        if (seed != null) {  
            secureRandom = new SecureRandom(Base64Coder.decode(seed));  
        } else {  
            secureRandom = new SecureRandom();  
        }  
  
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);  
        kg.init(secureRandom);  
  
        SecretKey secretKey = kg.generateKey();  
  
        return Base64Coder.encodeString(new String(secretKey.getEncoded()));  
    }  
}  