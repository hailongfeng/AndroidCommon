package com.foxconn.androidlib.pcmtest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.foxconn.androidlib.utils.LogUtil;

/**
 * Created by zhangtao on 17/8/27.
 */

public class VoicePlayer
{
    private AudioTrack mAudioTrack;      //AudioTrack对象
    private AudioPlayThread mAudioPlayThread = null;
    private AudioParam mAudioParam;      //音频参数
    private int mPrimePlaySize = 0;      //较优播放块大小

    //设置音频参数
    public void setAudioParam(AudioParam audioParam)
    {
        mAudioParam = audioParam;
    }

    private void createAudioTrack()
    {

//        int streamType = AudioManager.STREAM_MUSIC;
//        int simpleRate = 44100;
//        int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//        int mode = AudioTrack.MODE_STREAM;
        if (mAudioTrack == null)
        {
            // 获得构建对象的最小缓冲区大小
            int minBufSize = AudioTrack.getMinBufferSize(mAudioParam.mFrequency ,
                    mAudioParam.mChannel , mAudioParam.mSampleBit);
            mPrimePlaySize = minBufSize;
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC , mAudioParam.mFrequency ,
                    mAudioParam.mChannel , mAudioParam.mSampleBit , mPrimePlaySize  , AudioTrack.MODE_STREAM);
        }
    }

    private void releaseAudioTrack()
    {
        if (mAudioTrack != null)
        {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    //就绪播放源
    public void prepare()
    {
        if (mAudioParam != null)
        {
            createAudioTrack();
        }
    }

    public void play()
    {
        if (mAudioPlayThread == null)
        {
            mAudioPlayThread = new AudioPlayThread();
            mAudioPlayThread.start();
            if (mAudioPlayThread.mPlayHandler == null)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            LogUtil.d("mAudioPlayThread.mPlayHandler 执行完成");
        }
    }

    private void stop()
    {
        if (mAudioPlayThread != null)
        {
            mAudioPlayThread = null;
        }
    }

    public void release()
    {
        stop();
        releaseAudioTrack();
    }

    //设置音频源
    public void setDataSource(byte[] data)
    {
        if (mAudioPlayThread.mPlayHandler != null)
        {
            Message message = mAudioPlayThread.mPlayHandler.obtainMessage();
            message.what = 0x123;
            message.obj = data;
            mAudioPlayThread.mPlayHandler.sendMessage(message);
        }
    }

    class AudioPlayThread extends Thread
    {
        private Handler mPlayHandler;
        @Override
        public void run()
        {
            mAudioTrack.play();
            Looper.prepare();
            mPlayHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    if (msg.what == 0x123)
                    {
                        mAudioTrack.write((byte[]) msg.obj, 0, ((byte[]) msg.obj).length);
                    }
                }
            };
            Looper.loop();
        }
    }
}
