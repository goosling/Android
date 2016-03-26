package com.deseweather.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by JOE on 2016/3/11.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static Thread.UncaughtExceptionHandler defaultHandler = null;

    private Context context;

    private static final String TAG = CrashHandler.class.getSimpleName();

    public CrashHandler(Context context) {
        this.context = context;
    }

    public static void init(CrashHandler crashHandler) {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println(ex.toString());

        // 调用系统错误机制
        defaultHandler.uncaughtException(thread, ex);
    }

    //得到程序崩溃信息
    public String getCrashInfo(Throwable ex) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.setStackTrace(ex.getStackTrace());
        ex.printStackTrace(printWriter);
        return result.toString();
    }

    //收集程序崩溃的设备信息
    public String collectCrashDeviceInfo() {
        String versionName = getVersionName();
        String model = Build.MODEL;
        String androidVersion = Build.VERSION.RELEASE;
        String manufacturer = Build.MANUFACTURER;
        return versionName+" "+model+" "+androidVersion+" "+manufacturer;
    }

    //获取当前应用版本号
    public String getVersionName() {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try{
            info = pm.getPackageInfo(context.getPackageName(), 0);
        }catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0.0.0";
        }

        String version = info.versionName;

        return version;
    }




}
