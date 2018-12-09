package com.foxconn.androidlib.net.okhttputils;

/**
 * Created by dzc on 15/12/13.
 */
public interface UploadListener extends okhttp3.Callback{
    void onProgress(long totalBytes, long remainingBytes);
}
