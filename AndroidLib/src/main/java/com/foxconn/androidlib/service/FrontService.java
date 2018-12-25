package com.foxconn.androidlib.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.utils.LogUtil;


/**
 * description ： ${todo}类的作用
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/13 14:40
 */
public class FrontService extends Service {
    String TAG="FrontService";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG,"FrontService onCreate");
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.icon_nt_max);
        startForeground(1, getNotification());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification(){
        String channelId = "notification_simple";
        Notification notification;
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelId, "simple", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle("保持前台运行")
//                    .setContentText("This is content text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_notifi_test)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_nt_max))
                    .build();
        return notification;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
