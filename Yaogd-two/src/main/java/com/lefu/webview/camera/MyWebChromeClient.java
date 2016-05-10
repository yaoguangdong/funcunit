package com.lefu.webview.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.yaogd.R;
/**
 * 设置处理JavaScript的引擎
 * WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
 * @author: yaoguangdong
 * @data: 2014-1-24
 */
public class MyWebChromeClient extends WebChromeClient{

	private Handler mHandler ;
	private Activity activity ;
	
	MyWebChromeClient(Activity activity, Handler handler) {
		this.activity = activity ;	
		this.mHandler = handler ;
	}
	
	@Override
	//处理javascript中的alert(); 
	public boolean onJsAlert(WebView view, String url, String message,
			final JsResult result) 
	{
		//构建一个Builder来显示网页中的对话框
		Builder builder = new Builder(activity);
		builder.setTitle("提示您：");
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok,
			new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					//点击确定按钮之后,继续执行网页中的操作
					result.confirm();
				}
			});
		builder.setCancelable(false);
		builder.create();
		builder.show();
		return true;
	};
	
	@Override
	//处理javascript中的window.confirm("Are you srue?");  
	public boolean onJsConfirm(WebView view, String url, String message,
			final JsResult result) 
	{
		if("open_camera".equals(message)){
			Builder builder = new Builder(activity);
			builder.setTitle("请选择程序");
//			builder.setPositiveButton(android.R.string.ok,
//					new AlertDialog.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							result.confirm();
//						}
//					});
			builder.setSingleChoiceItems(R.array.single_chose_image, -1, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//which,0:照相机,1:图库
					Message msg = new Message();
					msg.what = which ;
					mHandler.sendMessage(msg);
					
					dialog.dismiss() ;
					
					result.confirm();
				}
				
			}); 
//			builder.setNegativeButton(android.R.string.cancel,
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							result.cancel();
//						}
//					});
			// 如果按了back键取消了对话框，没有执行result.confirm();就会无响应了。
			//因为js等着执行的返回。
			builder.setCancelable(true);
			builder.create();
			builder.show();
		}
		//所以需要执行result.confirm();
		result.confirm();
		return true;
	};
	
	@Override
	//处理javascript中的window.prompt("Please input some word";,"this is text");  
	//message为网页中对话框的提示内容
	//defaultValue在没有输入时，默认显示的内容
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, final JsPromptResult result) {
		//自定义一个带输入的对话框由TextView和EditText构成
		final LayoutInflater factory = LayoutInflater.from(activity);
		final View dialogview = factory.inflate(R.layout.wv_dialog_prom, null);
		//设置TextView对应网页中的提示信息
		((TextView) dialogview.findViewById(R.id.TextView_PROM)).setText(message);
		//设置EditText对应网页中的输入框
		((EditText) dialogview.findViewById(R.id.EditText_PROM)).setText(defaultValue);
		
		Builder builder = new Builder(activity);
		builder.setTitle("带输入的对话框");
		builder.setView(dialogview);
		builder.setPositiveButton(android.R.string.ok,
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//点击确定之后，取得输入的值，传给网页处理
						String value = ((EditText) dialogview.findViewById(R.id.EditText_PROM)).getText().toString();
						result.confirm(value);
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						result.cancel();
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						result.cancel();
					}
				});
		builder.show();
		return true;
	};
	
	@Override
	//设置网页加载的进度条
	public void onProgressChanged(WebView view, int newProgress) 
	{
		activity.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
		super.onProgressChanged(view, newProgress);
	}

	@Override
	//设置应用程序的标题title
	public void onReceivedTitle(WebView view, String title) 
	{
		activity.setTitle(title);
		super.onReceivedTitle(view, title);
	}
}
