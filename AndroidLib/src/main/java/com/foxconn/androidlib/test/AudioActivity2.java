package com.foxconn.androidlib.test;

import android.Manifest;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.audio.MyAudio;
import com.foxconn.androidlib.audio.MyAudioPlayer;
import com.foxconn.androidlib.base.BaseActivity;
import com.foxconn.androidlib.pcmtest.AudioParam;
import com.foxconn.androidlib.pcmtest.AudioUtil;
import com.foxconn.androidlib.pcmtest.VoicePlayer;
import com.foxconn.androidlib.utils.LogUtil;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class AudioActivity2 extends BaseActivity {

    private String TAG;
    @BindView(R.id.btn_recode)
    Button btn_recode;

    private AudioUtil mAudioUtil;
    private static final int BUFFER_SIZE = 1024 * 2;
    private byte[] mBuffer;
    private File mAudioFile;
    private ExecutorService mExecutorService;
    boolean isRrcord;
    VoicePlayer voicePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persion();
        mBuffer = new byte[BUFFER_SIZE];
        mAudioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/record/encode.pcm");
        mExecutorService = Executors.newSingleThreadExecutor();
        mAudioUtil = AudioUtil.getInstance();

        voicePlayer=new VoicePlayer();
        AudioParam audioParam=new AudioParam();
        audioParam.mChannel= AudioFormat.CHANNEL_IN_STEREO;
        audioParam.mFrequency=44100;
        audioParam.mSampleBit=AudioFormat.ENCODING_PCM_16BIT;
        voicePlayer.setAudioParam(audioParam);
        voicePlayer.prepare();
        mAudioUtil.setVoicePlayer(voicePlayer);
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
        if (isRrcord){
            isRrcord = false;
            btn_recode.setText("开始录音");
            mAudioUtil.stopRecord();
            mAudioUtil.convertWavFile();

        }else{
            isRrcord = true;
            btn_recode.setText("停止录音");
            mAudioUtil.startRecord();
            mAudioUtil.recordData();
            voicePlayer.play();
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

}
