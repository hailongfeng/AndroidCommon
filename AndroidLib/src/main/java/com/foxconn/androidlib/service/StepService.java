package com.foxconn.androidlib.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.utils.LogUtil;

import java.util.Calendar;
import java.util.Date;


/**
 * description ：主进程 双进程通讯
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/12 13:15
 */

public class StepService extends Service {

    String TAG="StepService";
    private int NOTICE_ID=1000;
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG,"StepService 被创建:"+new Date().toLocaleString());
        startGuardService();
    }

    void startGuardService(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent service=new Intent("com.foxconn.androidlib.service.GuardService");
                String packageName = "com.foxconn.androidlib";//需要开启服务的app包名
                String serviceClassName = packageName + "com.foxconn.androidlib.service.GuardService";//服务的类名全限定名
                service.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.service.GuardService"));
                startService(service);
                bindService(service,mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
        };
    }

    private Notification getNotification(){
        String channelId = "notification_simple";
        Notification notification;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelId, "simple", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle("保持前台运行")
//                    .setContentText("This is content text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
//            manager.notify(1, notification);
        }else{
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle("保持前台运行")
//                    .setContentText("This is content text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
//            manager.notify(1, notification);
        }
        return notification;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startForeground(1, new Notification());
        //绑定建立链接
        return super.onStartCommand(intent,flags,startId);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //链接上
            Log.d(TAG, "StepService:建立链接");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.d(TAG,"StepService 断开连接:"+new Date().toLocaleString());
            //断开链接

//            startService(new Intent(StepService.this, GuardService.class));
            startGuardService();
        }
    };

    @Override
    public void onDestroy() {
//        releaseLock();
        LogUtil.d(TAG,"StepService 销毁:"+new Date().toLocaleString());
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    private PowerManager.WakeLock mWakeLock;
    /**
     * 同步方法 得到休眠锁
     *
     * @param context
     * @return
     */
    synchronized private void getLock(Context context) {
        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, StepService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis((System.currentTimeMillis()));
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 23 || hour <= 6) {
                mWakeLock.acquire(5000);
            } else {
                mWakeLock.acquire(300000);
            }
        }
        Log.v(TAG, "get lock");
    }

    synchronized private void releaseLock()
    {
        if(mWakeLock!=null){
            if(mWakeLock.isHeld()) {
                mWakeLock.release();
                Log.v(TAG,"release lock");
            }

            mWakeLock=null;
        }
    }


}
