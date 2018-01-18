package com.ygd.webunit;

import android.os.Bundle;

import com.yaogd.web.common.YBrowserActivity;

/**
 * 专用于测试静态网页。
 * @author yaoguangdong
 * 2014-8-15
 */
public class CustomBrowser extends YBrowserActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(initView(getLayoutInflater()));
		//测试静态网页
//		load("https://a4allroad.mp.allnewaudi.cn/2016v1/?smtid=484096070z1tl4zxkowz8cz0z&_ahrotate=1");
//		load("http://2016.autobaojun.com/560/other/baojun/m_index.html");
//		load("http://wechat.xudankeji.com/he/biyadi/phone.html");
//		load("http://www.sgmw.com.cn/m/20170108/index.html?_ahrotate=1&ProjectID=119&MediaID=35");
//		load("https://tg.ewan.cn/dtxyjzys.jsp?lid=17949");
//		load("https://clickc.admaster.com.cn/c/a77272,b1527253,c292,i0,m101,8a2,8b2,h");
//		load("https://www.baidu.com/");

		load("file:///android_asset/ygd/smartphone/index.html");

	}

}
