package com.example.joe.lightsensortest;

import android.app.Application;
import android.content.Context;

/**
 * Created by JOE on 2016/2/20.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
