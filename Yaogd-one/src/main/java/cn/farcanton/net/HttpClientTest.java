package cn.farcanton.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
/**
 * 这个类不能运行，只能看一下代码
 * @author Administrator
 *
 */
public class HttpClientTest {
	private static final String PARAM_USERNAME = "username";
	private static final String PARAM_PASSWORD = "password";
	private static URI AUTH_URI;
	private static HttpClient mHttpClient;
	private static String TAG;



	/** 
     * Connects to the Voiper server, authenticates the provided username and 
     * password. 
     *  
     * @param username The user's username 
     * @param password The user's password 
     * @param handler The hander instance from the calling UI thread. 
     * @param context The context of the calling Activity. 
     * @return boolean The boolean result indicating whether the user was 
     *         successfully authenticated. 
     */  
	public static boolean authenticate(String username, String password,  
	        Handler handler, final Context context) {  
	        final HttpResponse resp;  
	  
	        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();  
	        params.add(new BasicNameValuePair(PARAM_USERNAME, username));  
	        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));  
	        HttpEntity entity = null;  
	        try {  
	            entity = new UrlEncodedFormEntity(params);  
	        } catch (final UnsupportedEncodingException e) {  
	            // this should never happen.  
	            throw new AssertionError(e);  
	        }  
	        final HttpPost post = new HttpPost(AUTH_URI);  
	        post.addHeader(entity.getContentType());  
	        post.setEntity(entity);  
	        //maybeCreateHttpClient();  
	  
	        try {  
	            resp = mHttpClient.execute(post);  
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
	                if (Log.isLoggable(TAG, Log.VERBOSE)) {  
	                    Log.v(TAG, "Successful authentication");  
	                }  
	                sendResult(true, handler, context);  
	                return true;  
	            } else {  
	                if (Log.isLoggable(TAG, Log.VERBOSE)) {  
	                    Log.v(TAG, "Error authenticating" + resp.getStatusLine());  
	                }  
	                sendResult(false, handler, context);  
	                return false;  
	            }  
	        } catch (final IOException e) {  
	            if (Log.isLoggable(TAG, Log.VERBOSE)) {  
	                Log.v(TAG, "IOException when getting authtoken", e);  
	            }  
	            sendResult(false, handler, context);  
	            return false;  
	        } finally {  
	            if (Log.isLoggable(TAG, Log.VERBOSE)) {  
	                Log.v(TAG, "getAuthtoken completing");  
	            }  
	        }  
	    }  
	
	
	
	/** 
     * Sends the authentication response from server back to the caller main UI 
     * thread through its handler. 
     *  
     * @param result The boolean holding authentication result 
     * @param handler The main UI thread's handler instance. 
     * @param context The caller Activity's context. 
     */  
    private static void sendResult(final Boolean result, final Handler handler,  
        final Context context) {  
        if (handler == null || context == null) {  
            return;  
        }  
        handler.post(new Runnable() {  
            public void run() {  
               // ((AuthenticatorActivity) context).onAuthenticationResult(result);  
            }  
        });  
    }  
}
