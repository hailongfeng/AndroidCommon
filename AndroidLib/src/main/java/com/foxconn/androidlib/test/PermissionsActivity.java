package com.foxconn.androidlib.test;

import android.Manifest;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.base.BaseActivity;
import com.foxconn.androidlib.net.HttpCallback;
import com.foxconn.androidlib.service.GuardService;
import com.foxconn.androidlib.service.JobWakeUpService;
import com.foxconn.androidlib.service.StepService;
import com.foxconn.androidlib.test.http.RetrofitModel;
import com.foxconn.androidlib.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
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
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE };
        if (EasyPermissions.hasPermissions(this, perms)) {
//            accessNet();
            LogUtil.d(TAG,"已获取权限");
        } else {
            LogUtil.d(TAG,"没有权限，现在去获取");
            EasyPermissions.requestPermissions(this, "申请内存权限",
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
                LogUtil.d(TAG,errorResponse);
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

        }

        @Override
        public void onPermissionsGranted(int requestCode, List<String> perms) {
            switch (requestCode){
                case 1:
                    accessNet();
                    toastLong("已获取网络权限和读写SD卡权限");
                    break;
            }
        }

        @Override
        public void onPermissionsDenied(int requestCode, List<String> perms) {
//            if (EasyPermissions.somePermissionPermanentlyDenied(PermissionsActivity.this, perms)) {
                toastLong("拒绝");
////                new AppSettingsDialog.Builder(PermissionsActivity.this).build().show();
//            }
        }
    };

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }


}
