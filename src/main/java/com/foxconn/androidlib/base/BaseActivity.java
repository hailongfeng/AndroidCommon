package com.foxconn.androidlib.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.utils.LogUtil;
import com.foxconn.androidlib.utils.ToastUtils;
import com.foxconn.androidlib.widget.LoadDialog;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG=this.getClass().getSimpleName();
    private LoadDialog loadDialog;
    private InputMethodManager mInputMethodManager;
    protected Context mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mActivity=this;
        //绑定控件
        if (isUserButterKnife()) {
            ButterKnife.bind(this);
        }
        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        //初始化数据
        initData();
        //view与数据绑定
        initView();
        //设置监听
        setListener();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isImmersionBarEnabled()) {
            ImmersionBar.with(this).init();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInputMethodManager = null;
        if (isImmersionBarEnabled()) {
            ImmersionBar.with(this).destroy();
        }
    }
    /**
     * 子类设置布局Id
     *
     * @return the layout id
     */
    protected abstract int getLayoutId();
    protected void initData() {
    }

    protected void initView() {
    }
    protected void setListener() {
    }
   protected boolean isUserButterKnife(){
        return true;
   }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }
    protected void initImmersionBar() {
        //在BaseActivity里初始化
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary).init();
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.mInputMethodManager == null) {
            this.mInputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.mInputMethodManager != null)) {
            this.mInputMethodManager.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

   protected void  log(String msg){
       LogUtil.d(TAG,msg);
   }
    protected void  showProgressDialog(String msg){
       if (loadDialog!=null){
           loadDialog.dismiss();
           loadDialog=null;
       }
        loadDialog=LoadDialog.newInstance(msg);
        loadDialog.show(getSupportFragmentManager(), "Dialog");
    }
    protected void  hideProgressDialog(String msg){
        loadDialog.dismiss();
        loadDialog=null;
    }

    /**
     * 显示长toast
     * @param msg
     */
    public void toastLong(String msg){
        ToastUtils.show(mActivity,msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示短toast
     * @param msg
     */
    public void toastShort(String msg){
        ToastUtils.show(mActivity,msg, Toast.LENGTH_SHORT);
    }

}
