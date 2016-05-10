package com.yaogd.eula;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class Eula {
 
	private static final String ASSET_EULA = "EULA" ;
	private static final String PREFERENCE_EULA_ACCEPTEN = "eula.accepted" ;
	private static final String PREFERENCE_EULA = "eula" ;
	
	static interface OnEulaAgreedTo{
		void onEulaAgreedTo();
	}
	
	static boolean show(final Activity activity){
		final SharedPreferences preferences = activity.getSharedPreferences(PREFERENCE_EULA, 
				Activity.MODE_PRIVATE) ;
		
		if(!preferences.getBoolean(PREFERENCE_EULA_ACCEPTEN, false)){
			final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("accept ?");
			builder.setCancelable(true) ;
			builder.setPositiveButton("", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					accept(preferences);
					if(activity instanceof OnEulaAgreedTo){
						((OnEulaAgreedTo)activity).onEulaAgreedTo() ;
					}
				}

			});
			builder.setNegativeButton("", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					refuse(activity);
					
				}

			}) ;
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					refuse(activity);
					
				}
			}) ;
			builder.setMessage(readEula(activity)) ;
			builder.create().show();
			return false ;
		}
		return true ;
	}
	

	private static CharSequence readEula(Activity activity) {
		BufferedReader in = null ;
		try{
			in = new BufferedReader(new InputStreamReader(activity.getAssets().open(ASSET_EULA)));
			String line ;
			StringBuffer buffer = new StringBuffer();
			while((line = in.readLine()) != null)
				buffer.append(line).append('\n') ;
			return buffer ;
			
		}catch(IOException e){
			return "" ;
			
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		
	}


	private static void accept(SharedPreferences p) {
		p.edit().putBoolean(PREFERENCE_EULA_ACCEPTEN, true).commit() ;
		
	}
	
	private static void refuse(Activity activity) {
		activity.finish();
		
	}
	
}
