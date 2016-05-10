package cn.farcanton.json;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * google 的Gson.jar 的使用
 * @author Administrator
 *
 */
public class MainActivity {
	
	private static final String JSON_ARY = "[{'code': 1, 'name': 'jeckson'}, {'code': 2, 'name': 'Lily'}]";
	private static final String JSON_OBJ = "{'code': 1, 'name': 'mechael'}";
	private static final String JSON_MIXTURE = "{'totalProperty': 2, 'root': [{'code': 1, 'name': 'meimei'}, {'code': 2, 'name': 'Lily'}]}";

	public static void main(String args[])throws Exception{
			
		JSONObject jsonObject = null;
		// JSON数组
		Type type = new TypeToken<LinkedList<User>>() {}.getType();
		LinkedList<User> users = new Gson().fromJson(JSON_ARY, type);
		for (Iterator<User> it = users.iterator(); it.hasNext();) {
			User user = it.next();
			Log.d("TAG", user.code + ":" + user.name);
		}
		// JSON对象
		User user = new Gson().fromJson(JSON_OBJ, User.class);
		Log.d("TAG", user.code + ":" + user.name);
		try {
			jsonObject = new JSONObject(JSON_OBJ);
			Log.d("TAG", jsonObject.getInt("code") + ":" + jsonObject.getString("name"));
		} catch (JSONException e1) {
			e1.printStackTrace();
			Log.d("TAG", e1.toString());
		}
		//json 里的数组对象
		jsonObject = new JSONObject(JSON_MIXTURE);
		System.out.println(jsonObject.getInt("totalProperty"));
		JSONArray jsonArray2 = jsonObject.getJSONArray("root");
		for (int i = 0; i < jsonArray2.length(); i++) {
			JSONObject object = (JSONObject) jsonArray2.get(i);
			System.out.println(object.getInt("code") + ":" + object.getString("name"));
			
			JSONObject object2 = (JSONObject) jsonArray2.opt(i);
			Log.d("TAG", object2.getInt("code") + ":" + object2.getString("name"));
		}
					
	}
	
	class User {
		int totalProperty;
		int code;
		String name;
	}

}