package com.foxconn.androidlib.net;

import com.foxconn.androidlib.BuildConfig;
import com.foxconn.androidlib.base.BaseApplication;
import com.foxconn.androidlib.utils.NetWorkUtils;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * author hailong.feng
 * description ${todo}(用一句话描述该文件做什么)
 * email hai-long.feng@mail.foxconn.com
 * date 2018/12/7 14:28
 */
public class RetrofitWrapper {
    private static RetrofitWrapper instance;
    private Retrofit retrofit;
    private RetrofitWrapper(final Config config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /**
         *设置缓存
         */
        final File chachefile = new File("CacheFilePath");
        final Cache cache = new Cache(chachefile, 1024 * 1024 * 50);//缓存文件
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetWorkUtils.isNetworkReachable(BaseApplication.getInstance())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetWorkUtils.isNetworkReachable(BaseApplication.getInstance())) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    //无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
//        builder.cache(cache).addInterceptor(cacheInterceptor);

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
                Map<String,String>  commonQueryParameter= config.commonQueryParameter;
                for (Map.Entry<String,String> entry:commonQueryParameter.entrySet()){
                    builderQueryParameter.addQueryParameter(entry.getKey(),entry.getValue());
                }
                request = originalRequest.newBuilder().url(builderQueryParameter.build()).build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(addQueryParameterInterceptor);

        /**
         * 设置请求头
         */
        Interceptor headerInterceptor = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Headers headers = originalRequest.headers();

                Request.Builder builderHeaders=originalRequest.newBuilder();
                Map<String,String>  commonHeaders= config.commonHeads;
                for (Map.Entry<String,String> entry:commonHeaders.entrySet()){
                    if (headers.get(entry.getKey())==null){
                        builderHeaders.header(entry.getKey(),entry.getValue());
                    }
                }

                Request request = builderHeaders
                        .method(originalRequest.method(), originalRequest.body())
                        .build();

                return chain.proceed(request);
            }
        };
        builder.addInterceptor(headerInterceptor);

        /**
         * Log信息拦截器
         */
        //compile 'com.squareup.okhttp3:logging-interceptor:3.4.1'
        if (BuildConfig.DEBUG) {
            //log信息拦截器
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置Debug Log模式
            builder.addInterceptor(httpLoggingInterceptor);
        }

        /**
         * 设置cookie
         */
        //compile 'com.squareup.okhttp3:okhttp-urlconnection:3.4.1'
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
        /**
         * 设置超时和重连
         */
        //设置超时
        builder.connectTimeout(config.connectTimeout, TimeUnit.SECONDS);
        builder.readTimeout(config.readTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(config.writeTimeout, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(config.retryOnConnectionFailure);

        //以上设置结束，才能build(),不然设置白搭 （上面的设置都可以省略）
        OkHttpClient okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(config.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) // 解析方法
                .client(okHttpClient)
                .build();
    }
    public static class Config{
        private Map<String,String> commonHeads=new HashMap<>();
        private Map<String,String> commonQueryParameter=new HashMap<>();
        private String baseUrl;
        private  long connectTimeout=30;
        private  long readTimeout=30;
        private  long writeTimeout=60;
        private  boolean retryOnConnectionFailure=false;

        public static Config newConfig(){
           return new Config();
        }

        public Config baseUrl(String baseUrl){
            this.baseUrl=baseUrl;
            return this;
        }
        public Config connectTimeout(long connectTimeout){
            this.connectTimeout=connectTimeout;
            return this;
        }
        public Config readTimeout(long readTimeout){
            this.readTimeout=readTimeout;
            return this;
        }
        public Config writeTimeout(long writeTimeout){
            this.writeTimeout=writeTimeout;
            return this;
        }
        public Config commonHeads(Map<String,String> commonHeads){
            this.commonHeads=commonHeads;
            return this;
        }
        public Config commonQueryParameter(Map<String,String>  commonQueryParameter){
            this.commonQueryParameter=commonQueryParameter;
            return this;
        }
        public Config retryOnConnectionFailure(boolean retryOnConnectionFailure){
            this.retryOnConnectionFailure=retryOnConnectionFailure;
            return this;
        }


    }
   public static void init(Config config){
       if (instance == null) {
           synchronized (RetrofitWrapper.class) {
               if (instance == null) {
                   instance = new RetrofitWrapper(config);
               }
           }
       }
   }
    /**
     * 单例模式
     *
     * @return
     */
    public static RetrofitWrapper getInstance() {
        return instance;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}

