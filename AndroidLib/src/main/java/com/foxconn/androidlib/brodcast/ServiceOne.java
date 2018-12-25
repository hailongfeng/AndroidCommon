package com.foxconn.androidlib.brodcast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.foxconn.androidlib.utils.LogUtil;


/**
 * description ： ${todo}类的作用
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/12 17:09
 */
public class ServiceOne extends Service {

    String TAG="ServiceOne";
    @Override
    public void onCreate() {
        super.onCreate();
        registerBroadcast();
        LogUtil.d(TAG,"ServiceOne onCreate");
    }
    void registerBroadcast(){
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("com.foxconn.androidlib.ServiceTwoKilled");
        registerReceiver(mReceiver, mFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent();
        intent.setAction("com.foxconn.androidlib.ServiceOneKilled");
        sendBroadcast(intent);
        unregisterReceiver(mReceiver);
        LogUtil.d(TAG,"ServiceOne onDestroy");
        super.onDestroy();
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d(TAG,"ServiceOne onReceive 执行");
            Intent service = new Intent("com.foxconn.androidlib.brodcast.ServiceTwo");
            String packageName = "com.foxconn.androidlib";//需要开启服务的app包名
            service.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.brodcast.ServiceTwo"));
            startService(service);
        }
    };
}
