package com.foxconn.androidlib.test;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.net.rxjava.ApiService;
import com.foxconn.androidlib.net.rxjava.RetrofitClient;
import com.foxconn.androidlib.utils.LogUtil;
import com.foxconn.androidlib.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.EasyPermissions;

public class RxjavaActivity extends AppCompatActivity {
    protected String TAG=this.getClass().getSimpleName();
    private String url="https://www.jianshu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ApiService apiService = RetrofitClient.getInstance(this).provideApiService();

        RetrofitClient.execute(apiService.getJianShu(), new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                ToastUtils.show(RxjavaActivity.this, s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
