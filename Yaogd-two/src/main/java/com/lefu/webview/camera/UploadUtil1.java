package com.lefu.webview.camera;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.http.HttpStatus;

import com.lefu.A;

public class UploadUtil1 {

	public static final int SEND_DATA_ERROR  = -100 ;
    private static final int TIME_OUT = 10 * 1000; // 超时时间  
	private String boundary = null;
	private HttpURLConnection conn = null ;
	private Map<String, String> paramsMap = null ;
	private List<FormFile> formFieldList = null ;
	
	UploadUtil1(String urlParam){
    	this.boundary = UUID.randomUUID().toString(); // 边界标识 随机生成  
    	paramsMap = new HashMap<String, String >() ; 
    	formFieldList = new ArrayList<FormFile >() ; 
    	
		try {
			URL url = new URL(urlParam);  
			conn = (HttpURLConnection) url.openConnection();  
			conn.setDoInput(true);//允许输入  
			conn.setDoOutput(true);//允许输出  
			conn.setUseCaches(false);//不使用Cache  
			conn.setRequestMethod("POST");     
			conn.setConnectTimeout(TIME_OUT) ;
			conn.setRequestProperty("Connection", "Keep-Alive");  
			conn.setRequestProperty("Charset", "UTF-8"); 
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=###" + boundary);
			
		} catch (ProtocolException e) {
			// 由于数据传输协议不匹配而导致无法与远程方进行通信,可以忽略
			////A.e("http connect error:" ,e ) ;
			conn = null ;
		} catch (MalformedURLException e) {
			//URL协议、格式或者路径错误,可以忽略
			////A.e("http connect error:" ,e ) ;
			conn = null ;
		} catch (IOException e) {
			// 连接超时
			////A.e("http connect error:" ,e ) ;
			conn = null ;
		} 
    }
    
	/**
     * 设置上传参数
     * @param key
     * @param value
     */
    public void putParams(Map<String, String> parameters){
    	
    	paramsMap = parameters ;
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
            	FormFile formFile = new FormFile(file.getName(), file, key, "image/jpg");
            	formFieldList.add(formFile);
            }  
		}
    }
    
    /**
    * 拼接HTTP协议，提交表单
    */
    public ResponseBody post() {
    	if(conn == null){
    		return new ResponseBody(HttpStatus.SC_GATEWAY_TIMEOUT,"request-time-out") ;
    	}
        try {
            StringBuilder sb = new StringBuilder();  
            //上传的表单参数部分
            for (Entry<String, String> entry : paramsMap.entrySet()) {//构建表单字段内容  
                sb.append("–");  
                sb.append(boundary);  
                sb.append("\r\n");  
                sb.append("Content-Disposition: form-data; name=\"");  
                sb.append(entry.getKey());  
                sb.append("\"\r\n\r\n");  
                sb.append(entry.getValue());  
                sb.append("\r\n");  
            }  
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());  
            //发送表单字段数据  
            outStream.write(sb.toString().getBytes());
             
            //上传的文件部分
            for (FormFile file : formFieldList) {
            	StringBuilder split = new StringBuilder();  
                split.append("–");  
                split.append(boundary);  
                split.append("\r\n");  
                split.append("Content-Disposition: form-data;name=\"");  
                split.append(file.parameterName);  
                split.append("\";filename=\"");  
                split.append(file.fileName);  
                split.append("\"\r\n");  
                split.append("Content-Type: ");  
                split.append(file.contentType);  
                split.append("\r\n\r\n");  
                outStream.write(split.toString().getBytes());  
                outStream.write(file.data, 0, file.data.length);  
                outStream.write("\r\n".getBytes());  
            }  
            
            byte[] end_data = ("–" + boundary + "–\r\n").getBytes();//数据结束标志           
            outStream.write(end_data);  
            outStream.flush();  
            //接受返回数据
            int responseCode = conn.getResponseCode();  
            InputStream inStream = conn.getInputStream();  
            int ch;  
            StringBuilder b = new StringBuilder();  
            while( (ch = inStream.read()) != -1 ){  
                b.append((char)ch);  
            }  
            outStream.close();  
            inStream.close();
            return new ResponseBody(responseCode, b.toString()) ;
        } catch (Exception e) {  
        	////A.e("send data error:" ,e ) ;
        	return new ResponseBody(SEND_DATA_ERROR, "") ;
        } finally{
        	if(conn != null){
        		conn.disconnect();  
        	}
        }
    }  
    
    /**
     * 上传文件模型
     * @author: yaoguangdong
     * @data: 2014-1-28
     */
    public class FormFile {
        /** 上传文件的数据 */
    	public byte[] data;
        /** 文件名称 */
    	public String fileName ;
        /** 请求参数名称*/
    	public String parameterName = "file";
        /** 内容类型 */
    	public String contentType = "application/octet-stream";
        
        public FormFile(String fileName, byte[] data, String parameterName, String contentType) {
            this.data = data;
            this.fileName = fileName;
            this.parameterName = parameterName;
            if(contentType != null) this.contentType = contentType;
        }
        
        public FormFile(String fileName, File file, String parameterName, String contentType) {
            this.fileName = fileName;
            this.parameterName = parameterName;
            this.data = fileToByteArray(file.getAbsolutePath());
            if(contentType != null) this.contentType = contentType;
        }
        
        private byte[] fileToByteArray(String filename){  
            
            FileChannel fileChannel = null;  
            try{  
            	fileChannel = new RandomAccessFile(filename,"r").getChannel();  
                MappedByteBuffer byteBuffer = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size()).load();  
                byte[] result = new byte[(int)fileChannel.size()];  
                if (byteBuffer.remaining() > 0) {  
                    byteBuffer.get(result, 0, byteBuffer.remaining());  
                }  
                return result;  
            }catch (IOException e) {  
                ////A.e("file read error:" ,e ) ;
                return null ;
            }finally{  
                try{  
                	fileChannel.close();  
                }catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        
    }
    
    public class ResponseBody{
    	public int responseCode = HttpStatus.SC_OK;
    	public String responseMsg = null;
    	
    	public ResponseBody(int responseCode, String responseBody){
    		this.responseCode = responseCode ;
    		this.responseMsg = responseBody ;
    	}
    	public ResponseBody() {
    		
		}
    }
    
}
