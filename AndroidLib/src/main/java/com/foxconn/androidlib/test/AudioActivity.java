package com.foxconn.androidlib.test;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import android.view.View;
import android.widget.Button;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.audio.MyAudio;
import com.foxconn.androidlib.audio.MyAudioPlayer;
import com.foxconn.androidlib.base.BaseActivity;
import com.foxconn.androidlib.utils.LogUtil;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class AudioActivity extends BaseActivity {

    private String TAG;
    @BindView(R.id.btn_recode)
    Button btn_recode;
    PipedInputStream in;
    boolean isRrcord;
    MyAudio myAudio;
    MyAudioPlayer myAudioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRrcord = false;
        String[] perms = {
                Manifest.permission.RECORD_AUDIO
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
//            accessNet();
            LogUtil.d(TAG,"已获取权限");
        } else {
            LogUtil.d(TAG,"没有权限，现在去获取");
            EasyPermissions.requestPermissions(this, "申请录音权限",
                    1, perms);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_audio;
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

    public void btnclick(View v){
        if (isRrcord){
            isRrcord = false;
            myAudio.stopRecord();
            myAudioPlayer.stopPlay();
            btn_recode.setText("开始录音");
        }else{
            isRrcord = true;
            btn_recode.setText("停止录音");
            startRecord();
        }
    }
    private void startRecord(){
        in = new PipedInputStream();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    log("开始录音1111");
                    myAudio = new MyAudio(AudioActivity.this, in);
                    myAudio.StartAudioData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                PipedOutputStream pout = new PipedOutputStream();
                myAudioPlayer = new MyAudioPlayer();
                try {
                    myAudioPlayer.setOutputStream(pout);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            myAudioPlayer.startPlayAudio();
                        }
                    }).start();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                int size = 0 ;
                try {
                    while (true){
                        LogUtil.d("在读取录音数据，写入到接受管道。。。");
                        while (in.available()>0){
                            size = in.read(buffer);
                            pout.write(buffer, 0, size);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
