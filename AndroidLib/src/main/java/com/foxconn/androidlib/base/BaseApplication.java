package com.foxconn.androidlib.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.foxconn.androidlib.utils.LogUtil;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * @author hailong.feng
 * @description
 * @email hai-long.feng@mail.foxconn.com
 * @date 2018/12/6 13:35
 */
public class BaseApplication extends Application {
    protected String TAG=this.getClass().getSimpleName();
    private static Map<String,Object> map;
    private static Application instance;
    // 此处采用 LinkedList作为容器，增删速度快
    public static LinkedList<Activity> activityLinkedList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        map=new HashMap<>();
        activityLinkedList = new LinkedList<>();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        Logger.init("LttAndroid");
    }

    public String getBaseUrl(){
        return null;
    }

    //监听Activity生命周期的回调
    ActivityLifecycleCallbacks activityLifecycleCallbacks=new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            LogUtil.d(TAG, "onActivityCreated: " + activity.getLocalClassName());
            activityLinkedList.add(activity);
            // 在Activity启动时（onCreate()） 写入Activity实例到容器内
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            LogUtil.d(TAG, "onActivityDestroyed: " + activity.getLocalClassName());
            activityLinkedList.remove(activity);
            // 在Activity结束时（Destroyed（）） 写出Activity实例
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }
    };

    public  void exitApp() {

        // 先打印当前容器内的Activity列表
        for (Activity activity : activityLinkedList) {
            LogUtil.d(TAG, activity.getLocalClassName());
        }
        // 逐个退出Activity
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }
        //  结束进程
         System.exit(0);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public static Application getInstance(){
        return instance;
    }


    public void saveObject(String key, Object object){
        map.put(key,object);
    }
    public Object removeObject(String key,Object object){
       return map.remove(key);
    }
}
