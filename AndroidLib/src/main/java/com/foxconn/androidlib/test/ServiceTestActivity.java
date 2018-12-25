package com.foxconn.androidlib.test;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.utils.LogUtil;
import com.foxconn.androidlib.utils.ToastUtils;

import java.util.List;

public class ServiceTestActivity extends AppCompatActivity {
String TAG="ServiceTestActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onclick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btnStartStepService: {
                LogUtil.d(TAG,"btnStartStepService 执行");
                Intent service = new Intent();
                String packageName = "com.foxconn.androidlib";//需要开启服务的app包名
                service.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.brodcast.ServiceOne"));
                startService(service);
                Intent service2 = new Intent();
                service2.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.brodcast.ServiceTwo"));
                startService(service2);
            }
            break;
            case R.id.btnCloseStepService:{
                LogUtil.d(TAG,"btnCloseStepService 执行");
                Intent service = new Intent("com.foxconn.androidlib.service.StepService");
                String packageName = "com.foxconn.androidlib";//需要开启服务的app包名
                String serviceClassName = packageName + ".StepService";//服务的类名全限定名
                service.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.service.StepService"));
                stopService(service);
            }
            break;
            case R.id.btnCheckStepService:
                LogUtil.d(TAG,"btnCheckStepService 执行");
               boolean b= serviceAlive("com.foxconn.androidlib.brodcast.ServiceOne");
               if (b){
                   ToastUtils.show(this,"保活成功");
               }else {
                   ToastUtils.show(this,"保活失败");
               }
                break;

        }
    }

    public void onclick2(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btnStartStepService: {
                LogUtil.d(TAG,"btnStartStepService 执行");
                Intent service = new Intent("com.foxconn.androidlib.service.StepService");
                String packageName = "com.foxconn.androidlib";//需要开启服务的app包名
                String serviceClassName = packageName + ".StepService";//服务的类名全限定名
                service.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.service.StepService"));
                startService(service);
                Intent service2 = new Intent("com.foxconn.androidlib.service.GuardService");
                service2.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.service.GuardService"));
                startService(service2);

            }
            break;
            case R.id.btnCloseStepService:{
                LogUtil.d(TAG,"btnCloseStepService 执行");
                Intent service = new Intent("com.foxconn.androidlib.service.StepService");
                String packageName = "com.foxconn.androidlib";//需要开启服务的app包名
                String serviceClassName = packageName + ".StepService";//服务的类名全限定名
                service.setComponent(new ComponentName(packageName, "com.foxconn.androidlib.service.StepService"));
                stopService(service);
            }
            break;
            case R.id.btnCheckStepService:
                LogUtil.d(TAG,"btnCheckStepService 执行");
                boolean b= serviceAlive("com.foxconn.androidlib.service.StepService");
                if (b){
                    ToastUtils.show(this,"保活成功");
                }else {
                    ToastUtils.show(this,"保活失败");
                }
                break;

        }
    }
    /**
     * 判断某个服务是否正在运行的方法
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    private boolean serviceAlive(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
