package cn.farcanton.advancedListView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
/**
 * 列表内容异步加载器
 * @author Administrator
 *
 */
public class AsyncVideoInfoLoader {

	private HashMap<String,SoftReference<Drawable>> imageCache;
	
	public AsyncVideoInfoLoader(){
		imageCache = new HashMap<String,SoftReference<Drawable>>();
	}
	
	public void loadDrawable(final String imageUrl,final ImageCallback imageCallback){
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Drawable> softReference = imageCache.get(imageUrl) ;
			Drawable drawable = softReference.get();
			imageCallback.iamgeLoaded(drawable,imageUrl);
		}
		final Handler handler = new Handler(){
			public void handleMessage(Message message){
				//原来接口的回调，就是其他的代码（包括系统）作为主体，调用实现类。
				if(message.what == 0){
					imageCallback.iamgeLoaded((Drawable)message.obj,imageUrl);
				}else if(message.what == 1){
					//如果下载失败，回调，使用null参数。
					imageCallback.iamgeLoaded(null,imageUrl);
				}
			}
		};
		new Thread(){
			public void run(){
				Drawable drawable = loadImageFromUrl(imageUrl);
				Message message = null;
				if(drawable != null){
					//把下载的数据存储在本地缓存中。
					imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
					message = handler.obtainMessage(0,drawable);
				}else{
					message = handler.obtainMessage(1,null);
				}
				handler.sendMessage(message);
			}
		}.start();
		
	}
	public static Drawable loadImageFromUrl(String url){
		URL mm;
		InputStream i = null;
		Drawable d = null;
		try {
			mm = new URL(url) ;
			i = (InputStream)mm.getContent() ;
			//src是debug用的，没有实际意义
			d = Drawable.createFromStream(i, "src") ;
		} catch (Exception e) {
			d = null;
			e.printStackTrace();
		}
		
		return d;
	}
	
	public interface ImageCallback{
		public void iamgeLoaded(Drawable imDrawable,String imageUrl) ;
	}
}
