package com.foxconn.androidlib.test.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author hailong.feng
 * @description ${todo}(用一句话描述该文件做什么)
 * @email hai-long.feng@mail.foxconn.com
 * @date 2018/12/7 14:31
 */
public interface RetrofitService {
    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);
    @POST("android/login.in")
    Call<String> login(@Query("loginName") String loginName, @Query("password") String password);

    @GET("http://www.baidu.com/index.php?tn=monline_3_dg")
    Call<String> getCsdnBlog();

}
