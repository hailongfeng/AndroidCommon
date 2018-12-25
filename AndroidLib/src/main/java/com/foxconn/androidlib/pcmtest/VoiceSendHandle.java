package com.foxconn.androidlib.pcmtest;

import com.foxconn.androidlib.utils.LogUtil;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * description ： 录音内容分段发送
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/18 10:19
 */
public class VoiceSendHandle {
    private BlockingQueue<VoiceInfo> queue=new LinkedBlockingQueue();
    private static VoiceSendHandle instance;
    private boolean isRunning=true;
    private VoiceSendHandle(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        VoiceInfo voiceInfo = queue.take();
                        LogUtil.d("发送数据index="+voiceInfo.index+",data="+ Arrays.toString(voiceInfo.data));
                        //TODO:发送代码
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static synchronized VoiceSendHandle getInstance(){
        if (instance==null){
            instance = new VoiceSendHandle();
        }
        return instance;
    }

    public void addVoiceInfo(VoiceInfo voiceInfo){
        queue.add(voiceInfo);
    }

   static class VoiceInfo{
       public int index;
       public  byte[] data;

        public VoiceInfo(int index, byte[] data) {
            this.index = index;
            this.data = data;
        }
    }
}
