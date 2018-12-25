package com.foxconn.androidlib.test;

import android.Manifest;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.base.BaseActivity;
import com.foxconn.androidlib.pcmtest.AudioParam;
import com.foxconn.androidlib.pcmtest.AudioRecordManage;
import com.foxconn.androidlib.pcmtest.VoicePlayer;
import com.foxconn.androidlib.utils.LogUtil;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class AudioActivity3 extends BaseActivity {

    private String TAG;
    @BindView(R.id.btn_recode)
    Button btn_recode;

    private AudioRecordManage mAudioUtil;
    private ExecutorService mExecutorService;
    VoicePlayer voicePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persion();
        mExecutorService = Executors.newSingleThreadExecutor();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_audio;
    }

    public void startRecode(){
//        initVoicePlayer();
        mAudioUtil = new AudioRecordManage();
        mAudioUtil.startRecord();
    }

    public void stopRecode(){
        mAudioUtil.stopRecord();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudioUtil.convertWavFile();
                mAudioUtil.convertAmrFile();
                LogUtil.d("转换完成");
            }
        }).start();
    }


    public void initVoicePlayer(){
        voicePlayer=new VoicePlayer();
        AudioParam audioParam=new AudioParam();
        audioParam.mChannel= AudioFormat.CHANNEL_IN_STEREO;
        audioParam.mFrequency=44100;
        audioParam.mSampleBit=AudioFormat.ENCODING_PCM_16BIT;
        voicePlayer.setAudioParam(audioParam);
        voicePlayer.prepare();
        voicePlayer.play();
    }





    void persion(){
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

    public void btnclick(View v){
        switch (v.getId()){
            case R.id.btn_recode:
                startRecode();
                break;
            case R.id.btn_stop_recode:
                stopRecode();
                break;
            case R.id.btn_play_recode:
               File file= mAudioUtil.getAmrFile();
                playRecode(file);
                break;
        }
    }

    public static int playRecode(File file){
        MediaPlayer mediaPlayer = new MediaPlayer();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
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
