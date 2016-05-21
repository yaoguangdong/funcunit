package com.yaogd.ipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.yaogd.lib.A;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 广告曝光上送服务
 * app启动时，启动
 * app结束时，停止
 * author yaoguangdong
 * 2015-12-11
 */
public class ADPVPushService extends Service {

    private static final String TAG = "ADPVPushService";
    private static final int PUSH_LIMIT = 100;//每次发送的最大数量

    private static final int COMMAND_THIRD_REPORT = 0x300;//第三方统计上报
    private static final int COMMAND_INSERT_ADPV = 0x310;//插入广告统计数据
    private static final int COMMAND_SEND_ADPV = 0x320;//发送广告统计数据

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case COMMAND_THIRD_REPORT:
                    handleThirdAdReport();
                    break;
                case COMMAND_INSERT_ADPV:
                    handleInsertPv();
                    break;
                case COMMAND_SEND_ADPV:
                    push();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 处理第三方上报
     */
    private void handleThirdAdReport(){

        for(int index = 0; index < urlsTemp.length; index++){
            try {
                String realUrl = URLDecoder.decode(urlsTemp[index]);
                Caller.GetRequest getRequest = new Caller.GetRequest(realUrl.trim(), true, null);
                Caller.get(getRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理统计数据入库
     */
    private void handleInsertPv(){
        try {
            //插入数据
            ADPVDB.getInstance(getBaseContext()).insertADPVData(pvEntityTemp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSending = false ;
    private int scheduleTimes = 0;

    /**
     * 590增加实时上报调度策略
     */
    private void scheduleNextPush(){
        if(scheduleTimes > 0){
            mHandler.sendEmptyMessageDelayed(COMMAND_SEND_ADPV, 200);
            scheduleTimes--;
        }
    }

    /**
     * 590增加实时上报调度策略
     */
    private void stopSchedule(){
        if(scheduleTimes > 0){
            mHandler.removeMessages(COMMAND_SEND_ADPV);
            scheduleTimes = 0;
        }
    }

    /**
     * 上送当前统计的pv
     */
    private void push(){
        try {
            if( ! isSending){
                if( ! Caller.netIsAvailable(getBaseContext())){
                    isSending = false ;
                    stopSchedule();
                    return ;
                }
                final List<ADPVEntity> pvEntitys = ADPVDB.getInstance(getBaseContext()).getADPVData(PUSH_LIMIT);

                if(pvEntitys == null || pvEntitys.isEmpty()){
                    isSending = false ;
                    stopSchedule();
                    return ;
                }
                isSending = true ;

                Map<String ,String> params = getPostParams(packJsonParam(pvEntitys));
                Caller.PostRequest post =  new Caller.PostRequest("http://rd.autohome.com.cn/adfront/realdeliver",
                        params, new Caller.ResponseListener() {
                    @Override
                    public void onReceiveData(int code, String data) {
                        boolean isSuccess = false;
                        if(data != null){
                            JSONObject jresult = new JSONObject();
                            try {
                                isSuccess = jresult.getInt("returncode") == 0;
                            } catch (JSONException e) {
                                isSuccess = false;
                                e.printStackTrace();
                            }
                        }
                        if(isSuccess){
                            if(pvEntitys != null && ! pvEntitys.isEmpty()){
                                int size = pvEntitys.size();
                                int [] ids = new int[size];

                                for(int i = 0; i < size; i++){
                                    ids[i] = pvEntitys.get(i)._id;
                                }
                                ADPVDB.getInstance(getBaseContext()).deleteADPVData(ids);
                            }
                        }

                        isSending = false ;
                        scheduleNextPush();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        A.d("onFailure:" + msg) ;
                        isSending = false ;
                        scheduleNextPush();
                    }
                });
                A.d("post,data:" + params.toString());
                Caller.post(post);
            }else{
                scheduleTimes++;
            }
        } catch (Exception e) {
            isSending = false ;
            e.printStackTrace() ;
        }
    }

    private String packJsonParam(List<ADPVEntity> list){
        if(list == null || list.isEmpty()){
            return "" ;
        }
        JSONArray jarray = new JSONArray() ;
        try {
            for(ADPVEntity pv : list){
                JSONObject jobj = new JSONObject();

                jobj.put("pvid", pv.pvid);
                jobj.put("tp", pv.type);
                jobj.put("t1", pv.beginTime) ;
                jobj.put("t2", pv.endTime) ;
                jobj.put("it", pv.imgType) ;

                jarray.put(jobj) ;
            }

            return jarray.toString() ;
        } catch (JSONException e) {
            e.printStackTrace();
            return "" ;
        }
    }

    /**
     * 封装post请求的参数
     */
    public Map<String, String> getPostParams(String pvData) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("v", "5.9.5");// app
        params.put("pm", "2");// android。这个字段广告那边用作他用，改用platform字段表示
        params.put("platform", "2");// 1-pc  2-app 3-m端
        params.put("a", "2");// 主软件
        params.put("_adpvtime", pvData);// 需要上传的数据集合
        params.put("_timestamp", String.valueOf(System.currentTimeMillis()));// 时间戳，用于签名校验

        params.put("_sign", getPostSignature(params, "@7U$aPOE@$"));

        return params;
    }

    /**
     * 将post请求的参数进行签名
     */
    public String getPostSignature(Map<String, String> params, String appKey) {

        Map<String, String> map = new TreeMap<String, String>();
        for (String key : params.keySet()) {
            map.put(key, params.get(key));
        }
        StringBuffer sb = new StringBuffer();
        sb.append(appKey);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append(entry.getValue());
        }
        sb.append(appKey);
        return md5s(sb.toString()).toUpperCase();
    }

    /**
     * 为字符加密为md5
     *
     * @param plainText
     *            要加密字符
     * @return md5值
     */
    public static String md5s(String plainText) {
        String str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    // 暂存接口数据，减少经过消息队里传输的耗时
    private ADPVEntity[] pvEntityTemp;
    private String[] urlsTemp;

	private IADPVInterface.Stub myBinder = new IADPVInterface.Stub(){

        @Override
        public void save(ADPVEntity[] pvEntity) throws RemoteException {
            pvEntityTemp = pvEntity;
            if(pvEntityTemp != null){
                Message msg = mHandler.obtainMessage();
                msg.what = COMMAND_INSERT_ADPV;
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void push() throws RemoteException {
            //delay 200ms :解决bug：上报速度快于插入数据时，数据延迟上报问题
            mHandler.sendEmptyMessageDelayed(COMMAND_SEND_ADPV, 200);
        }

        @Override
        public void thirdpush(String[] urls) throws RemoteException {
            urlsTemp = urls;
            if(urlsTemp != null && urlsTemp.length > 0){
                Message msg = mHandler.obtainMessage();
                msg.what = COMMAND_THIRD_REPORT;
                mHandler.sendMessage(msg);
            }
        }
    };

    @Override
    public IBinder onBind(Intent i) {
		return myBinder;
    }

    @Override
    public void onCreate() {
		A.d("Log,ADPVPushService onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
