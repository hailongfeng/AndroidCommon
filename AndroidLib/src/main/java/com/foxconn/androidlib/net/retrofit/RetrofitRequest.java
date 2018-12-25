package com.foxconn.androidlib.net.retrofit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author hailong.feng
 * @description ${todo}(用一句话描述该文件做什么)
 * @email hai-long.feng@mail.foxconn.com
 * @date 2018/12/7 14:32
 */
public class RetrofitRequest<T> {
    private Call<T> mCall;

    public RetrofitRequest(Call<T> call) {
        mCall = call;
    }

    /**
     * 异步请求
     *
     * @param callback
     */
    public void requestAsync(final HttpCallback<T> callback) {
        mCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                String path = mCall.request().url().toString();
                if (response.isSuccessful() && response.errorBody() == null) {
                    callback.onSuccess((T) response.body(), path);
                } else {
                    callback.onFailure(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    /**
     * 同步请求
     *
     * @return
     */
    public T requestSync() {
        Response<T> response = null;
        try {
            response = mCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T) response.body();
    }
}
