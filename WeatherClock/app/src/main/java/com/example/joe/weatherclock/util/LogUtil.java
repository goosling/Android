package com.example.joe.weatherclock.util;

import android.util.Log;

/**
 * Created by JOE on 2016/6/20.
 */
public class LogUtil {

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;

    public static void v(String tag, String msg) {
        if(NOTHING > VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if(NOTHING > DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if(NOTHING > INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if(NOTHING > WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if(NOTHING > ERROR) {
            Log.e(tag, msg);
        }
    }


}
