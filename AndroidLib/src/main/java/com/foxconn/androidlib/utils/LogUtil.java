package com.foxconn.androidlib.utils;

import com.orhanobut.logger.Logger;

/**
 * description ： 日志打印工具类
 * author : hailong.feng
 * email : hai-long.feng@mail.foxconn.com
 * date : 2018/12/13 15:53
 */
public class LogUtil {
    /**
     *
     * 注意：打包的时候记得设置isDebug为false
     */
    public static boolean isDebug= true;

    public static void e(String tag,String msg){
        if(isDebug){
            Logger.t(tag).e(msg+"");
        }
    }

    public static void d(String tag,String msg){
        if(isDebug){
            Logger.t(tag).d( msg+"");
        }
    }

    public static void i(String tag,String msg){
        if(isDebug){
            Logger.t(tag).i( msg+"");
        }
    }
    public static void v(String tag,String msg){
        if(isDebug){
            Logger.t(tag).v( msg+"");
        }
    }
    public static void w(String tag,String msg){
        if(isDebug){
            Logger.t(tag).w( msg+"");
        }
    }
    public static void e(String msg){
        if(isDebug){
            Logger.e(msg+"");
        }
    }

    public static void d(String msg){
        if(isDebug){
            Logger.d( msg+"");
        }
    }

    public static void i(String msg){
        if(isDebug){
            Logger.i( msg+"");
        }
    }
    public static void v(String msg){
        if(isDebug){
            Logger.v( msg+"");
        }
    }
    public static void w(String msg){
        if(isDebug){
            Logger.w( msg+"");
        }
    }
    public static void json(String json){
        if(isDebug){
            Logger.json( json);
        }
    }
    public static void xml(String xml){
        if(isDebug){
            Logger.xml(xml);
        }
    }
    public static void wtf(String wtf){
        if(isDebug){
            Logger.wtf(wtf);
        }
    }
}