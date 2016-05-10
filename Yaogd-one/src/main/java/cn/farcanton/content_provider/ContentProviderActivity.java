package cn.farcanton.content_provider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.farcanton.R;

public class ContentProviderActivity extends Activity{

	private Button cpBtn = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_provider);
		
		cpBtn = (Button)findViewById(R.id.cp_btn1);
		
		
		cpBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Cursor c = null;
				Uri uri = null;
				Toast mToast = null;
				
		        try {
		        	uri = Uri.parse("content://cn.farcanton.provider");
		        	//系统联系人：ContactsContract.Contacts.CONTENT_URI
		            c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[] { BaseColumns._ID ,ContactsContract.Contacts.DISPLAY_NAME},null, null, null);
//		            c = getContentResolver().query(uri, new String[] { BaseColumns._ID },null, null, null);
		            if (c != null && c.moveToFirst()) {
		                int id = c.getInt(0);
		                String txt = "search data uri:\n" + uri + "\nid: " + id;
		                Intent intent = new Intent();
		                intent.setAction("my_sendBreadcast");
		                intent.putExtra("getContacts", txt);
		                sendBroadcast(intent);
		            }
		        } finally {
		            if (c != null) {
		                c.close();
		            }
		        }
				
			}
		});
		
		
        
        
        
	}

	
}
