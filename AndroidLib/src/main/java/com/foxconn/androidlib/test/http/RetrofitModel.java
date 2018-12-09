package com.foxconn.androidlib.test.http;

import com.foxconn.androidlib.net.HttpCallback;
import com.foxconn.androidlib.net.RetrofitRequest;
import com.foxconn.androidlib.net.RetrofitWrapper;

import retrofit2.Call;


/**
 * @author hailong.feng
 * @description 接口调用类
 * @email hai-long.feng@mail.foxconn.com
 * @date 2018/12/7 14:31
 */

public class RetrofitModel {
    private static RetrofitModel retrofitModel;
    private RetrofitService retrofitService;

    public static RetrofitModel getInstance() {
        if (retrofitModel == null) {
            retrofitModel = new RetrofitModel();
        }
        return retrofitModel;
    }

    private RetrofitModel() {
        RetrofitWrapper.Config config=  RetrofitWrapper.Config.newConfig()
                .baseUrl("http://gitbook.liuhui998.com/");
        RetrofitWrapper.init(config);
        retrofitService = RetrofitWrapper.getInstance().create(RetrofitService.class);
    }

    /**
     * 获取用户信息(异步)
     * @param username
     * @param callback
     */
    public void getUser(String username, HttpCallback<User> callback) {
        Call<User> call = retrofitService.getUser(username);
        RetrofitRequest<User> request = new RetrofitRequest<User>(call);
        request.requestAsync(callback);
    }

    /**
     * 获取用户信息(同步)
     * @param username
     * @return
     */
    public User getUserSync(String username){
        Call<User> call = retrofitService.getUser(username);
        RetrofitRequest<User> request = new RetrofitRequest<User>(call);
        return request.requestSync();
    }

    public void getCsdnBlog(HttpCallback<String> callback){
        Call<String> call = retrofitService.getCsdnBlog();
        RetrofitRequest<String> request = new RetrofitRequest<String>(call);
        request.requestAsync(callback);
    }


}

