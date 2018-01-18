package com.yaogd.ipc.service;

import android.content.Context;

import com.yaogd.lib.A;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guangdong.yao
 * 2013-12-02
 */
public class Caller {

	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 1;

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};

	/**
	 * 初始化线程池
	 */
	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
			MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

	private static final int TIMEOUT = 1500;//ms

	/**
	 * 检查客户端网络状态
	 */
	public static boolean netIsAvailable(Context context) {
		android.net.ConnectivityManager cManager = (android.net.ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	public static void get(GetRequest getRequest){
		THREAD_POOL_EXECUTOR.execute(getRequest);
	}

	public static void post(PostRequest postRequest){
		THREAD_POOL_EXECUTOR.execute(postRequest);
	}

	public interface ResponseListener{
		void onReceiveData(int code, String data);
		void onFailure(int code, String msg);
	}

	public static class PostRequest implements Runnable {

		private ResponseListener listener;
		private String url;
		private Map<String, String> params;

		public PostRequest(String url, Map<String, String> params, ResponseListener listener) {
			this.url = url;
			this.params = params;
			this.listener = listener;
		}

		@Override
		public void run() {
			try{
				String response = doPost(params, "", url);
				if(listener != null){
					listener.onReceiveData(200, response);
				}
			}catch (Exception e){
				if(listener != null){
					listener.onFailure(-1, "");
				}
				e.printStackTrace();
			}
		}

		/**
		 * Performs HTTP GET using Apache HTTP Client
		 */
		public static String doPost(Map<String, String> params, String userAgent, String url){

			InputStream inputStream = null ;
			ArrayList<NameValuePair> sendParams = new ArrayList<NameValuePair>();
			if(params != null){
				for(String key : params.keySet()){
					sendParams.add(new BasicNameValuePair(key, params.get(key)));
				}
			}
			HttpEntity entity = null;
			try {
				entity = new UrlEncodedFormEntity(sendParams, "UTF-8");
			} catch (final UnsupportedEncodingException e) {
				// this should never happen.
				throw new AssertionError(e);
			}

			HttpPost post = new HttpPost(url);
			post.addHeader(entity.getContentType());
			post.setHeader("User-Agent", userAgent);
			post.setEntity(entity);

			HttpParams httpParams = new BasicHttpParams();
			HttpProtocolParams.setUserAgent(httpParams, userAgent);
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);

			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse httpResponse;

			try {
				httpResponse = httpClient.execute(post);

				A.i("Caller.doPost response code:" + httpResponse.getStatusLine().getStatusCode()) ;
				if(HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()){
					// request data
					HttpEntity httpEntity = httpResponse.getEntity();
					if(httpEntity != null){
						inputStream = httpEntity.getContent();
					}
				}else{
					inputStream = null ;
				}
			} catch (Exception e) {
				A.e("Caller.doPost err," ,e);
			}
			return convertStreamToString(inputStream);
		}
	}

	public static class GetRequest implements Runnable {

		private ResponseListener listener;
		private String url;
		private boolean ignoreResponse;

		public GetRequest(String url, boolean ignoreResponse, ResponseListener listener) {
			this.url = url;
			this.listener = listener;
			this.ignoreResponse = ignoreResponse;
		}

		@Override
		public void run() {
			try{
				String response = doGet(url, "", ignoreResponse);
				if(listener != null){
					listener.onReceiveData(200, response);
				}
			}catch (Exception e){
				if(listener != null){
					listener.onFailure(-1, "");
				}
				e.printStackTrace();
			}
		}

		/**
		 * Performs HTTP GET using Apache HTTP Client
		 */
		public static String doGet(String url, String userAgent, boolean ignoreResponse){

			InputStream inputStream = null ;
			URI encodedUri = null;
			HttpGet httpGet = null;

			try {
				encodedUri = new URI(url);
				httpGet = new HttpGet(encodedUri);
			} catch (URISyntaxException e1) {
				String encodedUrl = url.replace(' ', '+');
				httpGet = new HttpGet(encodedUrl);
				e1.printStackTrace();
			}

			HttpParams httpParams = new BasicHttpParams();
			HttpProtocolParams.setUserAgent(httpParams, userAgent);
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);

			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse httpResponse;

			try {
				httpGet.setHeader("Cookie", "");
				httpResponse = httpClient.execute(httpGet);

				A.i("Caller.doGet response code:" + httpResponse.getStatusLine().getStatusCode()) ;

				if( ! ignoreResponse){
					if(HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()){
						// request data
						HttpEntity httpEntity = httpResponse.getEntity();
						if(httpEntity != null){
							inputStream = httpEntity.getContent();
						}
					}else{
						inputStream = null ;
					}
				}else {
					inputStream = null ;
				}
			} catch (Exception e) {
				A.e("Caller.doGet err," ,e);
			}

			return convertStreamToString(inputStream);
		}

	}

	private static String convertStreamToString(InputStream is) {
		if(is == null)return null ;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

}
