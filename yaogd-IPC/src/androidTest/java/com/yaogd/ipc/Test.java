package com.yaogd.ipc;

import android.test.suitebuilder.annotation.SmallTest;

import com.yaogd.lib.A;
import com.yaogd.lib.TestFrame;

/**
 * 测试
 * @author yaoguangdong
 * 2014-10-27
 */
public class Test extends TestFrame {

	@Override
    public void setUp() throws Exception {
        super.setUp();
        new Thread(new ServerThread()).start();
    }
	
	/**
	 * 先运行一次，在SDcard中准备好原始图片
	 * 从assets中读取出来，放到yaogdimg中
	 */
	@SmallTest
	public void testSayHello(){
		ClientConnect client = new ClientConnect();  
        client.connect();  
        client.send("hello LocalServerSocket !");  
        String result = client.recv();  
        A.i("receive:" + result);
        client.close();       
	}
	
}
