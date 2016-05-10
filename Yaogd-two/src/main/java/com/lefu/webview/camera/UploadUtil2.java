package com.lefu.webview.camera;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lefu.A;
/**
 * http表单数据提交，包含文件上传
 * @author: yaoguangdong
 * @data: 2014-1-27
 */
public class UploadUtil2 {  
	//"http://10.10.110.34:28003/agentmobile/addCustomerDo"
	public static final int SEND_DATA_ERROR  = -100 ;
    private static final int TIME_OUT = 10 * 1000; // 超时时间  
    private List<NameValuePair> params = null ;
	private List<FormFile> formFieldList = null ;
	private HttpClient httpClient = null;  
	private HttpPost post = null;  
	
    UploadUtil2(String urlParam){
    	httpClient = new DefaultHttpClient();   
    	params = new ArrayList<NameValuePair>();  
    	formFieldList = new ArrayList<FormFile >() ; 
        post = new HttpPost(urlParam);
        
//        HttpParams httpParams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
//        HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
//        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
//        HttpClientParams.setRedirecting(httpParams, true);
//        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
//        HttpProtocolParams.setUserAgent(httpParams, userAgent);
//		post.setParams(httpParams ) ;
    }
    
    /**
     * 设置上传参数
     * @param key
     * @param value
     */
    public void putParams(Map<String, String> parameters){
    	
    	if(params == null)
    		params = new ArrayList<NameValuePair>();  
    	else 
    		params.clear();
    	//封装上传参数
//		for(String key : parameters.keySet()){
//			params.add(new BasicNameValuePair(key , parameters.get(key)));  
//		}
		params.add(new BasicNameValuePair("customer.fullName" , "nihao好"));
    }
    
    /**
     * 设置上传文件参数
     * @param key
     * @param value
     */
    public void putFormFiles(Map<String, String> files){
    	if(formFieldList == null)
    		formFieldList = new ArrayList<FormFile >() ;
    	else 
    		formFieldList.clear();
    	//封装上传文件
		for(String key : files.keySet()){
			String filePath = files.get(key);
			File file = new File(filePath );  
            if (file != null) {  
            	FormFile formFile = new FormFile(file.getName(), file, key);
            	formFieldList.add(formFile);
            }  
		}
    }
    
    /** 
     * postRequest
     */  
    public ResponseBody postParams()  
    {  
        try{  
            post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));  
            HttpResponse httpResponse = httpClient.execute(post);  
            return new ResponseBody(httpResponse.getStatusLine().getStatusCode(), 
            		EntityUtils.toString(httpResponse.getEntity())) ;
        }catch(IOException e){  
        	////A.e("http connect error:") ;
        	return new ResponseBody(HttpStatus.SC_GATEWAY_TIMEOUT, "request-time-out") ;
        }  
    }  

    public void endSend(){
    	if(httpClient != null){
    		httpClient.getConnectionManager().shutdown();  
    	}
    }
    
    public ResponseBody postFile(){
    	ResponseBody responseBody = new ResponseBody()  ;
    	String [] sendFailedFileNames = new String[formFieldList.size()] ;
    	for (int i = 0; i < formFieldList.size(); i++) {
            try {
            	FormFile formFile = formFieldList.get(i) ;
                FileEntity entity = new FileEntity(formFile.file, formFile.contentType);
                
                URI url = post.getURI() ;
                StringBuffer _url = new StringBuffer();
                _url.append(url.toString());
                _url.append("?");
                _url.append(formFile.parameterName);
                _url.append("=");
                _url.append(formFile.fileName);
                
                HttpPost httppost = new HttpPost(_url.toString());
                httppost.setEntity(entity);
 
                ////A.i("发送文件：" + formFile.parameterName) ;
                HttpResponse httpResponse = httpClient.execute(httppost);
                int responseCode = httpResponse.getStatusLine().getStatusCode() ;
                if(responseCode != HttpStatus.SC_OK){
                	sendFailedFileNames[i] = formFile.parameterName ;
                	responseBody.responseCode = SEND_DATA_ERROR ;
                }
                
            } catch(IOException e){
            	////A.e("http connect error:") ;
            	return new ResponseBody(HttpStatus.SC_GATEWAY_TIMEOUT,"request-time-out") ;
            }
        }
    	responseBody.sendFailedFileNames = sendFailedFileNames;
    	return responseBody;
    }
    
    /**
     * 上传文件模型
     * @author: yaoguangdong
     * @data: 2014-1-28
     */
    public class FormFile {
        /** 上传文件的数据 */
    	public File file;
        /** 文件名称 */
    	public String fileName ;
        /** 请求参数名称*/
    	public String parameterName = "file";
        /** 内容类型 */
    	public String contentType = "binary/octet-stream";
        
        public FormFile(String fileName, File file, String parameterName) {
            this.fileName = fileName;
            this.parameterName = parameterName;
            this.file = file;
        }
        
    }
    
    public class ResponseBody implements Serializable{
    	public int responseCode = HttpStatus.SC_OK;
    	public String responseMsg = null;
    	public String [] sendFailedFileNames = null ;
    	
    	public ResponseBody(int responseCode, String responseBody){
    		this.responseCode = responseCode ;
    		this.responseMsg = responseBody ;
    	}
    	
    	public ResponseBody(int responseCode, String [] sendFailedFileNames){
    		this.responseCode = responseCode ;
    		this.sendFailedFileNames = sendFailedFileNames ;
    	}
    	
    	public ResponseBody() {
		}

		@Override
		public String toString() {
			return "ResponseBody [responseCode=" + responseCode
					+ ", responseMsg=" + responseMsg + ", sendFailedFileNames="
					+ Arrays.toString(sendFailedFileNames) + "]";
		}
    	
    }
    
}  