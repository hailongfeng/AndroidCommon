package com.foxconn.androidlib.test;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.net.rxjava.ApiService;
import com.foxconn.androidlib.net.rxjava.RetrofitClient;
import com.foxconn.androidlib.test.http.User;
import com.foxconn.androidlib.utils.LogUtil;
import com.foxconn.androidlib.utils.ToastUtils;

import java.util.HashMap;

import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.EasyPermissions;

public class RxjavaActivity extends AppCompatActivity {
    protected String TAG=this.getClass().getSimpleName();
    private String url="https://www.jianshu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava3);
        String[] perms = {Manifest.permission.INTERNET };
        if (EasyPermissions.hasPermissions(this, perms)) {
//            accessNet();
            LogUtil.d(TAG,"已获取权限");
            ApiService apiService=RetrofitClient.getInstance(this).provideApiService();

            RetrofitClient.execute(apiService.getJianShu(), new Consumer<String>() {
                @Override
                public void accept(String user) throws Exception {
                    ToastUtils.show(RxjavaActivity.this,user);
                }
            });
        } else {
            LogUtil.d(TAG,"没有权限，现在去获取");
            EasyPermissions.requestPermissions(this, "申请内存权限",
                    1, perms);

        }
    }
}
