package com.lefu.net;


/***
 * 用于异步加载登录页，此代码有价值，需保留
 */


//package com.lefu.pai.webUtils;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//
//import com.lefu.pai.A;
//
///**
// * @author guangdong.yao
// * 2013-12-02用于异步加载登录页，此代码有价值，需保留
// */
//public class Caller {
//	
//	public static final int TIME_OUT = 10000 ;
//	
//	public static String doGet1(String userAgent,String url) throws WSError{
//		return convertStreamToString(doGet2(userAgent,url)) ;
//	}
//	
//	/**
//	 * Performs HTTP GET using Apache HTTP Client 
//	 */
//	public static InputStream doGet2(String userAgent, String url) throws WSError{
//		
//		InputStream inputStream = null ;
//		URI encodedUri = null;
//		HttpGet httpGet = null;
//		
//		try {
//			encodedUri = new URI(url);
//			httpGet = new HttpGet(encodedUri);
//		} catch (URISyntaxException e1) {
//			String encodedUrl = url.replace(' ', '+');
//			httpGet = new HttpGet(encodedUrl);
//			e1.printStackTrace();
//		}
//		
//		HttpParams httpParams = new BasicHttpParams();
//		HttpProtocolParams.setUserAgent(httpParams, userAgent);
//        HttpConnectionParams.setConnectionTimeout(httpParams,TIME_OUT);  
//        HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);  
//        
//		HttpClient httpClient = new DefaultHttpClient(httpParams);
//		HttpResponse httpResponse;
//		
//		try {
//			httpGet.setHeader("Cookie", "");
//			httpResponse = httpClient.execute(httpGet);
//			
//			A.i("Caller.doGet response code:" + httpResponse.getStatusLine().getStatusCode()) ;
//			if(HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()){
//				// request data
//				HttpEntity httpEntity = httpResponse.getEntity();
//				if(httpEntity != null){
//					inputStream = httpEntity.getContent();
//				}
//			}else{
//				inputStream = null ;
//			}
//		} catch (Exception e) {
//			throw new WSError(e.getLocalizedMessage());
//		}
//		
//		A.i("Caller.doGet "+url);
//		return inputStream;
//	}
//
//	private static String convertStreamToString(InputStream is) {
//		if(is == null)return null ;
//		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//		StringBuilder sb = new StringBuilder();
//
//		String line = null;
//		try {
//			while ((line = reader.readLine()) != null) {
//				sb.append(line + "\n");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return sb.toString();
//	}
//
//}
