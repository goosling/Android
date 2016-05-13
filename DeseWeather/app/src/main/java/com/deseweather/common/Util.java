package com.deseweather.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by JOE on 2016/3/11.
 */
public class Util {
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        }catch(Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }

    public static int getVersionCode(Context context) {
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    //判断网络是否连接
    public static boolean isNetworkConnected(Context context) {
        if(context != null) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }
}
