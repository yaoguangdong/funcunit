package com.yaogd.lib;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * 测试公用上下文
 * @author yaoguangdong
 * 2015-7-21
 */
public class TestFrame extends AndroidTestCase {
	
	public Context appContext = null ;
	
    @Override
    public void setUp() throws Exception {
        super.setUp();
        appContext = getContext() ;
    }
    
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
    }
}
