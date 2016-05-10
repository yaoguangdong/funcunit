package cn.farcanton.cache;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.util.LruCache;

class TestLruCache{
	public void a(){
		//limit available memory maxSize 10M
		LruCache<String,Drawable> lc = new LruCache<String,Drawable>(10 * 1024 * 1024);
		Drawable drawable1 = Drawable.createFromPath("pathName");
		
		lc.put("photo1", drawable1);
		
		Drawable dd = lc.get("photo1");
		//回收垃圾
		lc.evictAll();
	}
}
/**
 * 使用软引用能防止内存泄露，增强程序的健壮性.
 * 软引用也叫对象的弱引用，就是说延迟垃圾回收，能不释放就不释放，但是快到OutOfMemory时，会自动释放掉。
 * References that are cleared too early cause unnecessary work; those that are cleared too late waste memory. 
 * 软引用被清除的太早导致不必要的工作，相反，会浪费内存。
 * Most applications should use an android.util.LruCache instead of soft references.
 * 多数的应用应该使用LruCache代替软引用。
 * LruCache has an effective eviction policy and lets the user tune how much memory is allotted. 
 * LruCache 有一个有效的回收策略，让用户可以协调分配多少内存。
 * @author Administrator
 * 实现图片缓存，如果内存紧缺，就把一部分数据暂存到SDCard上。这个DataCache好像LruCache。
 * @param <T>can be Drawable,Bitmap,MP3,JPEG,and so on.
 */
public class DataCache<T> {
	private DataCache<T> cache;
	private Hashtable<Integer,MySoftRef>hashRefs ;
	private ReferenceQueue<T> q ;
	
	private class MySoftRef extends SoftReference<T>{
		private Integer _key = 0 ;
		public MySoftRef(T t, ReferenceQueue<T> q, int key){
			super(t, q);
			_key = key;
		}
	}
	private DataCache(){
		hashRefs = new Hashtable<Integer,MySoftRef>();
		q = new ReferenceQueue<T>();
	}
	public DataCache<T> getInstance(){
		if(cache == null){
			cache = new DataCache<T>();
		}
		return cache ;
	}
	private void addCacheData(Context context, T data, Integer key){
		//如果内存紧缺，要在SDCard上暂存一下。
		if (willSavedToSDCard(context)) {
			FileOutputStream outStream = null;
			String filePath = Environment.getExternalStorageDirectory()+"/temp/";
			try {
				outStream = new FileOutputStream(filePath);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
				objectOutputStream.writeObject(data);
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

		}
		cleanCache();
		MySoftRef ref = new MySoftRef(data, q, key);
		hashRefs.put(key, ref);
	}
	/**
	 * 先在缓存里找。
	 * @param context
	 * @param resld 在程序资源里找
	 * @param sdCardPath 在sdcard上找，节省流量
	 * @param netURLpath 从网络获取，最后的选择
	 * @return
	 */
	public T getData(Context context, int resId, String sdCardPath, String netURLpath){
		T data = null ;
		if(hashRefs.containsKey(resId)){
			MySoftRef ref = (MySoftRef)hashRefs.get(resId);
			data = (T)ref.get();
		}
		if(data == null){
			data = fetchDataFromResource(context, resId);
			if(data != null){
				addCacheData(context,data, resId);
			}else {
				data = fetchDataFromSDCard(sdCardPath);
				if(data != null){
					addCacheData(context,data, resId);
				}else {
					data = fetchDataFromNET(netURLpath);
					if(data != null){
						addCacheData(context,data, resId);
					}
				}
			}
		}
		
		return data ;
	}
	
	private T fetchDataFromResource(Context context, int resId){
		//例如
		return (T)BitmapFactory.decodeStream(context.getResources().openRawResource(resId));
	}
	private T fetchDataFromSDCard(String sdCardPath){
		T object = null;
		String filePath = Environment.getExternalStorageDirectory()+"/temp/";
		FileInputStream fileInputStream = null;
		try{
			fileInputStream = new FileInputStream(filePath);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);  
			object = (T)objectInputStream.readObject();  
			
		}catch(Exception e){
			
		}
		return object;
	}
	
	private T fetchDataFromNET(String nerURLPath){
		
		return null;
	}
	/**
	 * 如果内存紧缺，就把数据暂存到SDCard
	 * @return
	 */
	private boolean willSavedToSDCard(Context context){
		
		String[] arrayOfString;
		long initial_memory = 0;
		//可用内存
		long availableMemary = 0 ;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		availableMemary = mi.availMem;
		try {
			FileReader localFileReader = new FileReader("/proc/meminfo");// 系统内存信息文件 
			BufferedReader localBufferedReader = new BufferedReader(
			localFileReader, 8192);
			String str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小 
			arrayOfString = str2.split("\\s+");
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte 
			localBufferedReader.close();
		} catch (IOException e) {}
		
		if(availableMemary / initial_memory < 0.9){
			return true;
		}else return false;
	}
	
	private void cleanCache(){
		MySoftRef ref = null ;
		while((ref = (MySoftRef)q.poll()) != null){
			hashRefs.remove(ref._key);
		}
	}
	public void clearCache(){
		cleanCache();
		hashRefs.clear();
		System.gc();
		System.runFinalization();
	}
}
