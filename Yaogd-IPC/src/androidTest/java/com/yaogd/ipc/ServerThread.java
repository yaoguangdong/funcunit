package com.yaogd.ipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.net.Credentials;
import android.net.LocalServerSocket;
import android.net.LocalSocket;

import com.yaogd.lib.A;

class ServerThread implements Runnable {  
  
    @Override  
    public void run() {  
        LocalServerSocket server = null;  
        BufferedReader mBufferedReader = null;  
        PrintWriter os = null;  
        LocalSocket connect = null;  
        String readString =null;  
        try {  
            server = new LocalServerSocket("com.yaogd.ipc.localsocket");        
            while (true) {  
                connect = server.accept();  
                Credentials cre = connect.getPeerCredentials();  
                A.i("accept socket uid:"+cre.getUid());  
                mBufferedReader = new BufferedReader(new InputStreamReader  
                        (connect.getInputStream()));  
                while((readString=mBufferedReader.readLine())!=null){  
                    if(readString.equals("finish")) break;  
                    A.d(readString);  
                }  
                os = new PrintWriter(connect.getOutputStream());  
                os.println("nice to meet you !");  
                os.flush();  
                A.d("responsed");  
            }     
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        finally{  
            try {  
                mBufferedReader.close();  
                os.close();  
                connect.close();  
                server.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
}  