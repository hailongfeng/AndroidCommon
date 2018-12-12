package com.foxconn.androidlib.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.foxconn.androidlib.utils.LogUtil;


/**
 * description ： ${todo}类的作用
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/12 13:24
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    String TAG="BootCompleteReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG, "开机启动");
        Intent mIntent = new Intent(context, StepService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(mIntent);
        } else {
            context.startService(mIntent);
        }
    }
}
