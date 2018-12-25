package com.foxconn.androidlib.audio;

/**
 * description ： ${todo}类的作用
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/14 13:46
 */

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.foxconn.androidlib.utils.LogUtil;

/*
 * To getaudio or play audio
 * */
public class MyAudio {
    private AudioRecord audioRecord;
    private Context context;
    private boolean isRecording = false ;
    private PipedOutputStream outstream ;//利用管道传输数据
    public MyAudio(Context context , PipedInputStream instream) throws IOException {
        this.context  = context;
        //初始化管道流 用于向外传输数据
        outstream = new PipedOutputStream();
        outstream.connect(instream);
    }
    public void StartAudioData(){//得到录音数据

        LogUtil.d("开始录音");
        int frequency = 11025;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        int buffersize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                frequency, channelConfiguration, audioEncoding, buffersize);
        int bufferReadSize = 1024;
        byte[]buffer  = new byte[bufferReadSize];
        audioRecord.startRecording();//开始录音
        isRecording = true;
        while (isRecording){
            LogUtil.d("在录音。。。");
            audioRecord.read(buffer, 0, bufferReadSize);
            try {
                outstream.write(buffer, 0, bufferReadSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stopRecord(){//停止录音
        isRecording = false;
        audioRecord.stop();
        try {
            outstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
