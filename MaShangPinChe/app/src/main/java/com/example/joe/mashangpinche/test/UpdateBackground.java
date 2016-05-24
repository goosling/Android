package com.example.joe.mashangpinche.test;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.List;

/**
 * Created by JOE on 2016/5/24.
 */
public class UpdateBackground extends Activity{

    /**
     * 判断当前的应用程序是否在后台运行,使用该程序需要声明权限android.permission.GET_TASKS
     * @param context Context
     * @return true表示当前应用程序在后台运行。false为在前台运行
     */
    public static boolean isAppBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if(tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if(!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    //应用安装
    File file = new File(Environment.getExternalStorageDirectory(), "test.apk");
    Intent intent = new Intent();
    //设置意图动作
    intent.setAction(Intent.ACTION_VIEW);
    //设置意图数据和类型
    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
    //启动安装程序的Activity
    startActivity(intent);
}



