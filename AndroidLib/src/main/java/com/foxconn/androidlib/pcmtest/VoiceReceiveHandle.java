package com.foxconn.androidlib.pcmtest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.ZipInputStream;

/**
 * description ： 语音片段处理
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/18 10:52
 */
public class VoiceReceiveHandle {
    private Map<String, LinkedList<VoiceSendHandle.VoiceInfo>> voices;
    private ConcurrentLinkedQueue<String> inComeQueue;
    private Set<String> jidSet ;
    private static VoiceReceiveHandle instance;
    private VoiceReceiveHandle(){
        voices=new HashMap<String, LinkedList<VoiceSendHandle.VoiceInfo>>();
        inComeQueue=new ConcurrentLinkedQueue<String>();
        jidSet = new HashSet<String>();
    }

    public static synchronized VoiceReceiveHandle getInstance(){
        if (instance==null){
            instance = new VoiceReceiveHandle();
        }
        return instance;
    }

    public void addVoicePart(String jid, VoiceSendHandle.VoiceInfo voiceInfo){
            if (jidSet.contains(jid)){
                LinkedList<VoiceSendHandle.VoiceInfo> voiceInfos= voices.get(jid);
                if (voiceInfos!=null) {
                    voiceInfos.add(voiceInfo);
                }
            }else {
                jidSet.add(jid);
                inComeQueue.offer(jid);
                LinkedList<VoiceSendHandle.VoiceInfo> voiceInfos= new LinkedList<>();
                voiceInfos.add(voiceInfo);
                voices.put(jid,voiceInfos);
            }
    }

    private int currentPlayIndex=0;
    class PlayThread extends Thread{

        @Override
        public void run() {
            super.run();
            while (true){
               String jid= inComeQueue.peek();
                LinkedList<VoiceSendHandle.VoiceInfo> voiceInfos= voices.get(jid);
                int size=voiceInfos.size();
                if (currentPlayIndex<size) {
                    VoiceSendHandle.VoiceInfo voiceInfo = voiceInfos.get(currentPlayIndex);
                    currentPlayIndex++;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
