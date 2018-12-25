package com.foxconn.test.mediacodec2;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * description ： ${todo}类的作用
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/25 14:49
 */
public class AudioDecoderHandle {
    private static String TAG=AudioDecoderHandle.class.getSimpleName();
    private long timeoutUs = 100000;
    private MediaCodec mMediaDecode;
    private File targetFile;
    //类型
    private String mime = MediaFormat.MIMETYPE_AUDIO_AAC;
    private int bitRate = 64000;
    //输入缓存组
    private ByteBuffer[] inputBuffers;
    //输出缓存组
    private ByteBuffer[] outputBuffers;
    private MediaCodec.BufferInfo bufferInfo;
    private LinkedBlockingQueue<VoiceBuffer> mVoiceBufferDataQueue = new LinkedBlockingQueue<>();
    private AudioTrack mAudioTrack = null;
    private AudioEncoderHandle audioEncoderHandle;
    public AudioDecoderHandle(AudioEncoderHandle audioEncoderHandle) {
        this.audioEncoderHandle = audioEncoderHandle;
    }

    private void configAndStart(){
        try {
            //获取含有音频的MediaFormat
            MediaFormat mediaFormat = new MediaFormat();
            mediaFormat.setString(MediaFormat.KEY_MIME, mime);
            mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
            mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
            mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 1024 * 1024);
            mediaFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, 44100);

            mMediaDecode = MediaCodec.createDecoderByType(mime);
            mMediaDecode.configure(mediaFormat, null, null, 0);//当解压的时候最后一个参数为0
            mMediaDecode.start();//开始，进入runnable状态
            //只有MediaCodec进入到Runnable状态后，才能过去缓存组
            inputBuffers = mMediaDecode.getInputBuffers();
            outputBuffers = mMediaDecode.getOutputBuffers();
            bufferInfo = new MediaCodec.BufferInfo();
        } catch (IOException e) {
            Log.e("tag_ioException",e.getMessage()+"");
            e.printStackTrace();
        }
    }

    public void decoding() {
        boolean inputSawEos = false;
        boolean outputSawEos = false;
        while (!outputSawEos||!mVoiceBufferDataQueue.isEmpty()) {
            if (!inputSawEos) {
                //每5000毫秒查询一次
                int inputBufferIndex = mMediaDecode.dequeueInputBuffer(timeoutUs);
                //输入缓存index可用
                if (inputBufferIndex >= 0) {
                    //获取可用的输入缓存
                    ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                    //从MediaExtractor读取数据到输入缓存中，返回读取长度
                    VoiceBuffer voiceBuffer=null;
                    try {
                        voiceBuffer=mVoiceBufferDataQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int bufferSize = voiceBuffer.data.length;
                    inputBuffer.put(voiceBuffer.data,0,bufferSize);
                    if (bufferSize <= 0) {//已经读取完
                        //标志输入完毕
                        inputSawEos = true;
                        //做标识
                        mMediaDecode.queueInputBuffer(inputBufferIndex, 0, 0, timeoutUs, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    } else {
                        long time = voiceBuffer.pts;
                        //将输入缓存放入MediaCodec中
                        mMediaDecode.queueInputBuffer(inputBufferIndex, 0, bufferSize, time, 0);
                    }
                }
            }
            //获取输出缓存，需要传入MediaCodec.BufferInfo 用于存储ByteBuffer信息
            int outputBufferIndex = mMediaDecode.dequeueOutputBuffer(bufferInfo, timeoutUs);
            if (outputBufferIndex >= 0) {
                int id = outputBufferIndex;
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    mMediaDecode.releaseOutputBuffer(id, false);
                    continue;
                }
                //有输出数据
                if (bufferInfo.size > 0) {
                    //获取输出缓存
                    ByteBuffer outputBuffer = outputBuffers[id];
                    //设置ByteBuffer的position位置
                    outputBuffer.position(bufferInfo.offset);
                    //设置ByteBuffer访问的结点
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    byte[] targetData = new byte[bufferInfo.size];
                    //将数据填充到数组中
                    outputBuffer.get(targetData);
                    if (decodedListener!=null){
                        VoiceBuffer buffer=new VoiceBuffer();
                        buffer.data= targetData;
                        decodedListener.OnDataReady(buffer);
                    }

//                    mAudioTrack.write(targetData,bufferInfo.offset,bufferInfo.offset+bufferInfo.size);
                }
                try {
                    //释放输出缓存
                    mMediaDecode.releaseOutputBuffer(id, false);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //判断缓存是否完结
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    outputSawEos = true;
                }
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                outputBuffers = mMediaDecode.getOutputBuffers();

            }else if(outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){
                MediaFormat  mediaFormat = mMediaDecode.getOutputFormat();
            }
        }
    }

    public void start(){
        configAndStart();
//        createAudioTrack();
        audioEncoderHandle.setListener(encodedListener);
//        mAudioTrack.play();
        new Thread(new Runnable() {
            @Override
            public void run() {
                decoding();
            }
        }).start();
    }

    public void stop(){
        //释放资源
        sendEmptyData();
        mMediaDecode.stop();
        mMediaDecode.release();
        audioEncoderHandle.setListener(null);
//        mAudioTrack.stop();
//        mAudioTrack.release();
    }


   private void createAudioTrack() {

//音频采样率 (MediaRecoder的采样率通常是8000Hz AAC的通常是44100Hz.设置采样率为44100目前为常用的采样率，官方文档表示这个值可以兼容所有的设置）
        int mSampleRateInHz = 44100;    //声道
        int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int mChannelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO; //单声道
//数据格式  (指定采样的数据的格式和每次采样的大小)    //指定音频量化位数 ,在AudioFormaat类中指定了以下各种可能的常量。通常我们选择ENCODING_PCM_16BIT和ENCODING_PCM_8BIT PCM代表的是脉冲编码调制，它实际上是原始音频样本。    //因此可以设置每个样本的分辨率为16位或者8位，16位将占用更多的空间和处理能力,表示的音频也更加接近真实。
//先估算最小缓冲区大小
        int mBufferSizeInBytes = AudioRecord.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);
//创建AudioTrack
        mAudioTrack = new AudioTrack(
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                new AudioFormat.Builder()
                        .setSampleRate(mSampleRateInHz)
                        .setEncoding(mAudioFormat)
                        .setChannelMask(mChannelConfig)
                        .build(),
                mBufferSizeInBytes,
                AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE);
    }

    OnDataReadyListener encodedListener=new OnDataReadyListener() {
        @Override
        public void OnDataReady(VoiceBuffer buffer) {
            log("接收到已经编码过的数据");
            mVoiceBufferDataQueue.offer(buffer);
        }
    };

    private OnDataReadyListener decodedListener;
    public void setListener(OnDataReadyListener listener) {
        this.decodedListener = listener;
    }

    private void sendEmptyData(){
        VoiceBuffer buffer=new VoiceBuffer(0);
        mVoiceBufferDataQueue.offer(buffer);
    }


    private void log(String msg){
        Log.d(TAG,msg);
    }
}
