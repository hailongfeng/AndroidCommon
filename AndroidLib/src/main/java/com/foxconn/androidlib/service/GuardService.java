package com.foxconn.androidlib.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.foxconn.androidlib.utils.LogUtil;

import java.util.Date;


/**
 * description ： ${todo}类的作用
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/12 13:15
 */

/**
 * 守护进程 双进程通讯
 * Created by db on 2018/1/11.
 */

public class GuardService extends Service {
    String TAG="GuardService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG,"GuardService 创建:"+new Date().toLocaleString());
        startStepService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startForeground(1, new Notification());
        //绑定建立链接

        return super.onStartCommand(intent,flags,startId);
    }

    void startStepService(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent service=new Intent("com.foxconn.androidlib.service.StepService");
                String packageName = "com.foxconn.androidlib";//需要开启服务的app包名
                String serviceClassName = packageName + ".StepService";//服务的类名全限定名
                service.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.service.StepService"));
                startService(service);
                bindService(service,mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        }).start();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //链接上
            Log.d("test", "GuardService:建立链接");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.d(TAG,"GuardService 断开链接:"+new Date().toLocaleString());
            //断开链接
            startStepService();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"GuardService 销毁:"+new Date().toLocaleString());
        unbindService(mServiceConnection);
    }
}
 
