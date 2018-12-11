package com.foxconn.androidlib.net.rxjava;

import android.content.Context;

import com.foxconn.androidlib.test.http.User;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitClient {


    private static RetrofitClient instance;

    private OkHttpClient mOkHttpClient;


    private Context mContext;

    private Retrofit mRetrofit;

    private ApiService mApiService;

    private RetrofitClient(Context context) {
        this.mContext = context;
    }

    public static RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }


    private OkHttpClient provideOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
//                    .addInterceptor(addQueryParameterInterceptor)  //公共参数的封装
                    .cookieJar(new CookieJarImpl(mContext)) //cookie 保存方案
                    .build();
        }

        return mOkHttpClient;
    }

    /**
     *公共参数
     */
    Interceptor addQueryParameterInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request request;
            String method = originalRequest.method();
            HttpUrl.Builder builderQueryParameter = originalRequest.url().newBuilder();
            Map<String,String> commonQueryParameter= null;
            for (Map.Entry<String,String> entry:commonQueryParameter.entrySet()){
                builderQueryParameter.addQueryParameter(entry.getKey(),entry.getValue());
            }
            request = originalRequest.newBuilder().url(builderQueryParameter.build()).build();
            return chain.proceed(request);
        }
    };

    private Retrofit provideRetrofit() {

        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://www.jianshu.com/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   //RxJava2 的CallAdapter
                    .addConverterFactory(FastJsonConverterFactory.create())       // FastJsonConvertFactory
                    .client(provideOkHttpClient()).build();
        }

        return mRetrofit;
    }

    public ApiService provideApiService() {
        if (mApiService == null) {
            mApiService = provideRetrofit().create(ApiService.class);
        }
        return mApiService;
    }
    public static <T> T execute(Observable<T> observable , Consumer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return null;
    }

}
