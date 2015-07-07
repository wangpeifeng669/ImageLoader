package com.cancai.demo.imageloadlib.utils;

import android.util.Log;

/**
 * Log 打印配置信息
 *
 * @author peter_wang
 * @create-time 15/3/30 10:24
 */
public class L {
    private static final String TAG = "Image_Loader";

    // 是否打印log，不提供开关，默认是打印
    private static volatile boolean isWriteLog = true;
    // 是否打印debug类型的log，提供开关，release状态需关闭
    private static volatile boolean isWriteDebugLog = true;

    // 防止类被创建
    private L() {

    }

    public static void enableDebugLog() {
        isWriteDebugLog = true;
    }

    public static void disableDebugLog() {
        isWriteDebugLog = false;
    }

    public static void v(String message) {
        print(Log.VERBOSE, message);
    }

    public static void d(String message) {
        if (!isWriteDebugLog)
            return;
        print(Log.DEBUG, message);
    }

    public static void i(String message) {
        print(Log.INFO, message);
    }

    public static void w(String message) {
        print(Log.WARN, message);
    }

    public static void e(String message) {
        print(Log.ERROR, message);
    }

    public static void e(Throwable throwable) {
        print(Log.ERROR, Log.getStackTraceString(throwable));
    }

    public static void e(String message, Throwable throwable) {
        String logMessage = message == null ? throwable.getMessage() : message;
        // 参考Log源码，String的format能提供多种格式化输出
        print(Log.ERROR, String.format("%1$s\n%2$s", logMessage, Log.getStackTraceString(throwable)));
    }

    private static void print(int type, String message) {
        if (!isWriteLog)
            return;

        if (type == Log.VERBOSE) {
            Log.v(TAG, message);
        }
        else if (type == Log.DEBUG) {
            Log.d(TAG, message);
        }
        else if (type == Log.INFO) {
            Log.i(TAG, message);
        }
        else if (type == Log.WARN) {
            Log.w(TAG, message);
        }
        else if (type == Log.ERROR) {
            Log.e(TAG, message);
        }
    }
}
