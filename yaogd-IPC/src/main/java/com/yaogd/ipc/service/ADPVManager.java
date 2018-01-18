package com.yaogd.ipc.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.yaogd.ipc.TheApplication;

/**
 * Created by yaoguangdong on 2016/5/9.
 */
public class ADPVManager {

    private static ADPVManager mgr = new ADPVManager();
    private IADPVInterface binder;

    private ADPVManager(){

    }

    public static ADPVManager getInstance(){
        if(mgr == null){
            mgr = new ADPVManager();
        }
        return mgr;
    }

    private static boolean isConnected = false;

    public void push(){
        if(binder == null || ! isConnected){
            Context cxt = TheApplication.getContext();
            cxt.bindService(new Intent(cxt, ADPVPushService.class), new ServiceConnection(){

                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {
                            Log.d("videorecord", "onServiceConnected(" + name);
                            try{
                                binder = IADPVInterface.Stub.asInterface(service);
                                binder.push();
                                isConnected = true;
                            }catch (Exception e){
                                isConnected = false;
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {
                            isConnected = false;
                            Log.d("videorecord", "onServiceDisconnected(" + name);
                        }
                    }, Context.BIND_AUTO_CREATE);
        }

        try{
            binder.push();
        }catch (Exception e){
            isConnected = false;
            e.printStackTrace();
        }

    }


}
