package com.foxconn.androidlib.test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.foxconn.androidlib.base.BaseActivity;
import com.foxconn.androidlib.service.FrontService;
import com.foxconn.androidlib.widget.LoadDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.foxconn.androidlib.R;
import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.status_bar_view)
    View statusBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        ImmersionBar.with(this)
                .statusBarView(statusBarView)
                .init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    public void test1(View view) {
        LoadDialog.newInstance("加载中").show(getSupportFragmentManager(), "EditNameDialog");
//        LoadDialog editNameDialog = new LoadDialog();
//        editNameDialog.show(getSupportFragmentManager(), "EditNameDialog");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startService(View view) {
        startForegroundService(new Intent(this, FrontService.class));
    }
}
