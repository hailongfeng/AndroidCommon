package com.foxconn.androidlib.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.utils.LogUtil;
import com.foxconn.androidlib.utils.ToastUtils;

import java.util.HashMap;

import io.reactivex.functions.Consumer;

public class RxjavaActivity extends AppCompatActivity {

    private String url="https://www.jianshu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava3);
        RetrofitClient client=RetrofitClient.getInstance(this,url);
        client.createBaseApi();
        client.get("https://www.jianshu.com/p/4005bc4a20f2?from=jiantop.com", new HashMap(), new Consumer<BaseResponse<String>>() {
            @Override
            public void accept(BaseResponse<String> response) throws Exception {
                ToastUtils.show(RxjavaActivity.this,response.getData());
                LogUtil.d("RxjavaActivity",response.getData());
            }
        });
    }
}
