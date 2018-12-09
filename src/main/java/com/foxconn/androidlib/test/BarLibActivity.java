package com.foxconn.androidlib.test;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.base.BaseActivity;
import com.foxconn.androidlib.utils.LogUtil;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

public class BarLibActivity extends BaseActivity {

    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void setListener() {
        super.setListener();
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float alpha=  (float) scrollY/320.0f;
                LogUtil.d(TAG,scrollX+","+oldScrollX+","+scrollY+","+oldScrollY+",");
                if (alpha>1) alpha=1;
                if (alpha<0) alpha=0;
                    ImmersionBar.with(BarLibActivity.this)
                            .statusBarColorTransform(R.color.orange)
                            .navigationBarColorTransform(android.R.color.transparent)
                            .addViewSupportTransformColor(toolbar)
                            .barAlpha(alpha)
                            .init();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bar_lib;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).statusBarView(R.id.top_view)
                .navigationBarColor(R.color.colorPrimary)
//                .fullScreen(true)
                .addTag("PicAndColor")
                .init();
    }
}
