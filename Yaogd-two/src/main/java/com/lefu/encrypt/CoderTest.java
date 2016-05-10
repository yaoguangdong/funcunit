package com.lefu.encrypt;
import java.math.BigInteger;

/**
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class CoderTest {

	public void test() throws Exception {
		String inputStr = "简单加密";
		System.out.println("原文:\n" + inputStr);

		String code = Base64Coder.encodeString(inputStr) ;

		System.out.println("BASE64加密后:\n" + code);

		byte[] output = Base64Coder.decode(code);

		String outputStr = new String(output);

		System.out.println("BASE64解密后:\n" + outputStr);

		System.out.println("验证MD5对于同一内容加密是否一致:");
		System.out.println(new String(Coder.encryptMD5(output))) ;
		System.out.println(new String(Coder.encryptMD5(output))) ;

		
		System.out.println("验证SHA对于同一内容加密是否一致:");
		System.out.println(new String(Coder.encryptSHA(output))) ;
		System.out.println(new String(Coder.encryptSHA(output))) ;
		
		String key = Coder.initMacKey();
		System.err.println("Mac密钥:\n" + key);

		System.out.println("验证HMAC对于同一内容，同一密钥加密是否一致:");
		System.out.println(new String(Coder.encryptHMAC(output,key))) ;
		System.out.println(new String(Coder.encryptHMAC(output,key))) ;
		
		
		BigInteger md5 = new BigInteger(Coder.encryptMD5(output));
		System.err.println("MD5:\n" + md5.toString(16));

		BigInteger sha = new BigInteger(Coder.encryptSHA(output));
		System.err.println("SHA:\n" + sha.toString(32));

		BigInteger mac = new BigInteger(Coder.encryptHMAC(output, inputStr));
		System.err.println("HMAC:\n" + mac.toString(16));
	}
}
