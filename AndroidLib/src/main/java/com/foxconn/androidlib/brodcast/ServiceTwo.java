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

import java.util.Random;


/**
 * description ： ${todo}类的作用
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/12 17:09
 */
public class ServiceTwo extends Service {
    String TAG="ServiceTwo";
    @Override
    public void onCreate() {
        super.onCreate();
        registerBroadcast();
        LogUtil.d(TAG,"ServiceTwo onCreate");
        count();

    }

    void count(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    LogUtil.d(TAG,"一直打印"+new Random().nextLong());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    void registerBroadcast(){
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("com.foxconn.androidlib.ServiceOneKilled");
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
        intent.setAction("com.foxconn.androidlib.ServiceTwoKilled");
        sendBroadcast(intent);
        unregisterReceiver(mReceiver);
        LogUtil.d(TAG,"ServiceTwo onDestroy");
        super.onDestroy();
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d(TAG,"ServiceTwo onReceive 执行");
            Intent service = new Intent("com.foxconn.androidlib.brodcast.ServiceOne");
            String packageName = "com.foxconn.androidlib";//需要开启服务的app包名
            service.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.brodcast.ServiceOne"));
            startService(service);
        }
    };
}
