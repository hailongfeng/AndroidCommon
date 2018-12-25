package com.foxconn.androidlib.test;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.base.BaseActivity;
import com.foxconn.androidlib.net.retrofit.HttpCallback;
import com.foxconn.androidlib.test.http.RetrofitModel;
import com.foxconn.androidlib.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;


public class PermissionsActivity extends BaseActivity {

    @BindView(R.id.txt_content)
    TextView txtContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_permissions;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.CALL_PHONE

        };
        if (EasyPermissions.hasPermissions(this, perms)) {
//            accessNet();
            LogUtil.d(TAG,"已获取权限");
        } else {
            LogUtil.d(TAG,"没有权限，现在去获取");
            EasyPermissions.requestPermissions(this, "申请4个权限",
                    1, perms);

        }

    }

    private void accessNet(){
        RetrofitModel.getInstance().getCsdnBlog(new HttpCallback<String>() {
            @Override
            public void onSuccess(final String response, String path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtContent.setText(response);
                    }
                });
            }

            @Override
            public void onFailure(String errorResponse) {
                log(errorResponse);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, permissionCallbacks);
    }

    EasyPermissions.PermissionCallbacks permissionCallbacks=new EasyPermissions.PermissionCallbacks(){
        @Override
        public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {
                    log("onRequestPermissionsResult");
                    log(i+"");
                    for (String s:strings){
                        log(s);
                    }
                    for (int i1:ints){
                        log(""+i1);
                    }
        }

        @Override
        public void onPermissionsGranted(int requestCode, List<String> perms) {
            log("onPermissionsGranted");
            for (String s:perms){
                log(s);
            }
        }

        @Override
        public void onPermissionsDenied(int requestCode, List<String> perms) {
            log("onPermissionsDenied");
             for (String s:perms){
                log(s);
            }
        }
    };

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }


}
