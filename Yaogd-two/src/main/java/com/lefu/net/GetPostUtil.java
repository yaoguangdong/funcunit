package com.lefu.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a> 
 * <br/>Copyright (C), 2001-2010, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */

public class GetPostUtil
{
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String url = "http://10.62.1.226:7070/";

		// 1.获取从指定时间开始失效的设备号
		// from: 开始时间，格式如：2012-04-12 15:04:55
		// online: 0表示获取失效用户; 1表示获取在线用户
		// 返回结果为字符串，如：{"data":["9ab043eb976c414cb371743fbe9a1900", "ab9e9846279c4269bf5a0c75849d77fb", "acdc92733a444a53a4c9131c16f18be2"]}
		String response = GetPostUtil.sendGet(
				url + "user_api.do",
				"from=2012-04-13%2016:33:34&online=0");

		System.out.println("GET:" + response);
		
		// 2.发送推送消息（建议用GET方式.限制发送的字符长度256字节,防止消息推送耗费用户流量,同时和iPhone消息推送的机制保持一致）。注意:发送中文可能会有乱码,方法3可解决
		// action=send 发送推送消息
		// broadcast: Y表示群发,N表示发送给指定用户
		// username: 指定用户的username,如:feddfa7ca6f14d649a36c74e5e7062b4
		// title: 推送消息的标题
		// message: 推送消息的内容
		// uri: 隐式传递的参数，如推送消息的id
		String response2 = GetPostUtil.sendGet(
						url + "notification_api.do",
						"action=send&broadcast=N&username=82e4c4dffc394761b1af1230803969ed&title=FE协同办公&message=GET世界，你好&uri=");

		System.out.println("GET:" + response2);

		
		// 3.用POST方式发送
		/*--拼接POST字符串--*/
		final StringBuilder parameter = new StringBuilder();
		parameter.append("action=send&broadcast=N&username="); // 单条发送这里要设成N,若设成Y则广播,全部收到,后面参数无效
		parameter.append("82e4c4dffc394761b1af1230803969ed");
		parameter.append("&title=FE协作平台&message=");
		parameter.append("POST世界，你好");
		parameter.append("&uri=");
		parameter.append(""); // 和推送给iPhone格式保持一致
		/*--End--*/
		 String resp = GetPostUtil.send("POST", url + "notification_api.do", parameter);
		 System.out.println("response:" + resp);

	}
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String sendGet(String url, String params)
	{
		String result = "";
		BufferedReader in = null;
		try
		{
			String urlName = url + "?" + params;
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet())
			{
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += "\n" + line;
			}
		}
		catch (Exception e)
		{
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String sendPost(String url, String params)
	{
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try
		{
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(params); // params.toString().getBytes("UTF-8");
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += "\n" + line;
			}
		}
		catch (Exception e)
		{
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	public static String send(String method, String url, StringBuilder parameter) {
		URL urlObj;
		HttpURLConnection httpConn;
		BufferedReader in = null;
		String result = "";
		try {
			urlObj = new URL(url);
			httpConn = (HttpURLConnection) urlObj.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod(method); // POST/GET

			// 发送请求参数
			httpConn.getOutputStream().write(
					parameter.toString().getBytes("UTF-8"));
			httpConn.getOutputStream().flush();
			httpConn.getOutputStream().close();
			httpConn.getResponseCode();
			// httpConn.getInputStream();// 仅执行但不获取返回内容

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
}
