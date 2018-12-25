package com.foxconn.androidlib.net.rxjava;

/**
 * description ： ${todo}类的作用
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/13 16:18
 */
public class ApiException extends Exception {
    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }
}