package com.yaogd.nativ.ui;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.yaogd.nativ.LoadNative;
import com.yaogd.nativ.R;

/**
 * 测试native 代码
 * author yaoguangdong
 * 2015-8-16
 */
public class Native01 extends Activity {

    private TextView txt01,txt02 ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.native_01);
		
		txt01 = (TextView)findViewById(R.id.text_01) ;
		txt02 = (TextView)findViewById(R.id.text_02) ;
		
		long time = System.currentTimeMillis() ;
		Log.v("yaogd", "start call jni") ;
		
		String k = getSecretKey() ;
		
        Log.v("yaogd", k) ;
        Log.v("yaogd", "k:" + k +"\nend call jni:" + (System.currentTimeMillis() - time) + "毫秒" ) ;
		
		txt01.setText(k) ;
		txt02.setText(LoadNative.get()) ;
		
		getSignature() ;
		
	}

	//获取签名的C函数，需要依赖一个存活的继承于ContextWrapper的对象（四大组件+Application），普通的静态类不能调用。
	private native String getSecretKey() ;
	
	static{
	    long time = System.currentTimeMillis() ;
        Log.v("yaogd", "srart load so") ;
        
        System.loadLibrary("sk");
        
        Log.v("yaogd", "end load so:" + (System.currentTimeMillis() - time) + "毫秒" ) ;
	}
	
	private void getSignature(){
	    try {
	        PackageInfo pis = getPackageManager().getPackageInfo("com.yaogd.nativ", PackageManager.GET_SIGNATURES);  //获取包信息
	        Signature sigs = pis.signatures[0];        //获取签名
	        int hash = sigs.hashCode();                //获取签名的哈希码
	        byte [] bytes = sigs.toByteArray() ;
	        
	        Log.v("yaogd", "signature hash:" + hash) ;
	        Log.v("yaogd", "signature data:" + new String(bytes)) ;
	        
	    } catch(Exception e) {
	        e.printStackTrace() ;
	    }
	}
	
	    
}