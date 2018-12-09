package com.foxconn.androidlib.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.base.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

public class ScroollActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scrooll;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean isUserButterKnife() {
        return false;
    }
}
