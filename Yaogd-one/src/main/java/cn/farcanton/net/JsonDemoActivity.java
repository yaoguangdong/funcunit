package cn.farcanton.net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class JsonDemoActivity extends ListActivity
{
	/*
	 * demo of http and json
	 * 
	 * @author zengbin
	 * 
	 * @date 20120605
	 */

	/*
	 * 获取默认HttpClient DefaultHttpClient() DefaultHttpClient(HttpParams params)
	 * DefaultHttpClient(ClientConnectionManager conman,HttpParams params)
	 * ClientConnectionManager连接管理器，实现类有SingleClientConnManager(默认，同一时间仅维护一个连接),
	 * ThreadSafeClientConnManager(可维护多个连接) HttpParams为请求头参数
	 */
	private HttpClient client = new DefaultHttpClient();
	/*
	 * HttpResponse接口的实现类BasicHttpResponse同时实现了HttpMessage接口。包括很多对Message的操作方法
	 * HttpMessage包括request消息和response消息
	 * ，request由请求行、头信息和消息体组成，response由状态行、头信息和消息体组成
	 * 推荐一篇文章http://www.cnblogs.com/hyddd/archive/2009/04/19/1438971.html
	 */
	private HttpResponse response = null;
	private String mGetRequestURL;
	private String mPostRequestURL;
	private String mPostFileUploadURL;
	private ListView listView;
	private List<String> items = new ArrayList<String>();
	private JSONArray jsonArray = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*
		 * get请求地址URL get方法一般用于向服务器请求数据
		 * get方法URL可带参数，如http://10.0.2.2:8080/Iask/httpget
		 * .jsp?username=zengbin&userpassword=123
		 * 地址与参数以?隔开，参数为键值对形式，各个参数以&隔开，URL长度因浏览器限定而异
		 */
		mGetRequestURL = "http://192.168.1.110:8080/Iask/httpgetjson.jsp";
		//mGetRequestURL = "http://10.0.2.2:8080/Iask/httpgetjson.jsp";
		/*
		 * post请求地址URL post方法一般用于向服务器传递较大数据 post只能通过实体传递参数
		 */
		mPostRequestURL = "http://192.168.1.110:8080/Iask/httpPost.jsp";
		mPostFileUploadURL = "http://192.168.1.110:8080/upload_file_service/UploadServlet";
		doGet(client, mGetRequestURL);
		doPost(client, mPostRequestURL);
		doPost(client, mPostFileUploadURL, Environment
				.getExternalStorageDirectory().toString()
				+ File.separator + "test.jpg");
	}

	// 从服务器获取json格式数据并解析
	public void doGet(HttpClient httpClient, String uriString)
	{
		/*
		 * 构造HttpGet,作为HttpClient的execute方法的参数
		 * 基类HttpRequestBase,8个直接或间接子类分别对应http请求的8种方法 直接子类HttpDelete,
		 * HttpEntityEnclosingRequestBase, HttpGet, HttpHead, HttpOptions,
		 * HttpTrace 间接子类HttpPost, HttpPut
		 */

		HttpGet httpGet = new HttpGet(uriString);
		try
		{

			/*
			 * 执行HttpClient的execute方法，返回HttpResponse对象
			 * 简单的重载execute(HttpUriRequest request) 复杂的重载execute(HttpHost
			 * target, HttpRequest request, ResponseHandler<? extends T>
			 * responseHandler, HttpContext context) HttpHost：请求的主机地址
			 * HttpRequest：为一接口，具体实现类如上面的HttpGet
			 * ResponseHandler的实现类实现了handleResponse(HttpResponse
			 * response)方法，用户可在此方法中预处理response
			 * 当使用ResponseHandler时，HttpClient会自动关注和释放连接至连接管理器ClientConnectionManager
			 * ，不管请求成功或者异常
			 * HttpContext：Http上下文，通过它可以获得诸如HttpConnection、HttpHost、HttpRequest等对象
			 * ，在一定程度上弥补了Http无状态的缺点
			 */

			response = client.execute(httpGet);

			/* StatusLine：服务器返回的状态信息，包括Http的版本及Status-Code（状态码） */
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() == HttpStatus.SC_OK)
			{

				/*
				 * HttpEntity实体内容 实现类：BufferedHttpEntity, ByteArrayEntity,
				 * FileEntity, InputStreamEntity, StringEntity
				 * 主要方法：getContentType
				 * ()、getContentLength()、getContent()、writeTo()
				 */

				StringBuilder builder = new StringBuilder();
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));// 获取实体内容
				for (String s = reader.readLine(); s != null; s = reader
						.readLine())
				{
					builder.append(s);
				}
				// 确保实体内容被消耗完以便释放连接资源
				if (entity != null)
				{
					entity.consumeContent();
				}
				// 释放连接资源
				// client.getConnectionManager().shutdown();
				// 获取body标签内容
				Document doc = Jsoup.parse(builder.toString());
				Element body = doc.body();
				String jsonString = body.ownText();
				Log.i("zeng", "Get result:" + jsonString);
				JSONArray jsonObjs = new JSONObject(jsonString)
						.getJSONArray("persons");
				for (int i = 0; i < jsonObjs.length(); ++i)
				{
					items.add(jsonObjs.getJSONObject(i).toString());
				}
				listView.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, items));

			}
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 向服务器提交参数
	public void doPost(HttpClient httpClient, String uriString)
	{
		/* 构造HttpPost,作为HttpClient的execute方法的参数 */

		HttpPost httpPost = new HttpPost(uriString);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", "zengbin"));
		params.add(new BasicNameValuePair("userpassword", "123"));
		UrlEncodedFormEntity urlEncodedFormEntity = null;
		try
		{
			urlEncodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpPost.setEntity(urlEncodedFormEntity);
		try
		{
			response = client.execute(httpPost);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() == HttpStatus.SC_OK)
			{
				StringBuilder builder = new StringBuilder();
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));
				for (String s = reader.readLine(); s != null; s = reader
						.readLine())
				{
					builder.append(s);
				}
				if (entity != null)
				{
					entity.consumeContent();
				}
				// client.getConnectionManager().shutdown();
				Document doc = Jsoup.parse(builder.toString());
				Element body = doc.body();
				String jsonString = body.ownText();
			}
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 向服务器提交文件
	public void doPost(HttpClient httpClient, String uriString, String fileName)
	{
		File file = new File(fileName);
		org.apache.commons.httpclient.HttpClient httpClient2 = new org.apache.commons.httpclient.HttpClient();
		PostMethod filePost = new PostMethod(uriString);

		/*
		 * FileEntity fileEntity=new FileEntity(file,"image/jpeg");
		 * fileEntity.setChunked(true); HttpPost httpPost = new
		 * HttpPost(uriString); httpPost.setEntity(fileEntity);
		 */

		try
		{
			Part[] parts =
			{ new FilePart(file.getName(),

			file) };

			filePost.setRequestEntity(new MultipartRequestEntity(parts,

			filePost.getParams()));
			int status = httpClient2.executeMethod(filePost);
			if (status == HttpStatus.SC_OK)
			{
				Toast.makeText(JsonDemoActivity.this, "upLaod Succeed!", 200)
						.show();
				// 上传成功

			} else
			{
				Toast.makeText(JsonDemoActivity.this, "upLaod failed!", 200)
						.show();
				// 上传失败
			}

			/*
			 * response = httpClient.execute(httpPost); StatusLine status =
			 * response.getStatusLine(); if (status.getStatusCode() ==
			 * HttpStatus.SC_OK) { Toast.makeText(JsonDemoActivity.this,
			 * "upLaod Succeed!", 200).show(); // 上传成功
			 * 
			 * } else { Toast.makeText(JsonDemoActivity.this, "upLaod failed!",
			 * 200).show(); // 上传失败 }
			 */
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex)

		{

			ex.printStackTrace();

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Bitmap myBitmap = null;
		try
		{
			super.onActivityResult(requestCode, resultCode, data);
			Bundle extras = data.getExtras();
			myBitmap = (Bitmap) extras.get("data");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		File captureFile = new File(Environment.getExternalStorageDirectory()
				.toString() + File.separator + "test.jpg");
		BufferedOutputStream bos = null;
		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(captureFile));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		try
		{
			bos.flush();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			bos.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}