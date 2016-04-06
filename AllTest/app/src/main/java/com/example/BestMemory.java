package com.example;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by joe on 2016/4/6.
 */
public class BestMemory {
    //获得手机堆内存大小
    ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
    int heapSize = manager.getMemoryClass();
}
