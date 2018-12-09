package com.foxconn.androidlib.net;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author hailong.feng
 * @description ${todo}(用一句话描述该文件做什么)
 * @email hai-long.feng@mail.foxconn.com
 * @date 2018/12/7 14:32
 */
public abstract class HttpCallback<T> {
    public Class<?> clazz;
    public HttpCallback() {
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = null;
        if (superclass instanceof ParameterizedType) {// 参数化的泛型
            parameterizedType = (ParameterizedType) superclass;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            if (typeArray != null && typeArray.length > 0) {
                clazz = (Class<?>) typeArray[0];
            }
        }
    }
    public abstract void onSuccess(T response, String path);
    public abstract void onFailure(String errorResponse);
}
