package com.deseweather.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * Created by JOE on 2016/3/11.
 */
public class BaseApplication extends Application {

    private static String cacheDir = "";

    private static Context mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = getApplicationContext();
        //初始化Retrofit
        RetrofitSingleton.init(getApplicationContext());
        CrashHandler.init(new CrashHandler(getApplicationContext()));

        //如果存在sd卡则写入sd卡，否则写入内存
        if(getApplicationContext().getExternalCacheDir() != null && existSDCard()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        }else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private boolean existSDCard() {
        if(android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }
}
