package com.example.joe.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by JOE on 2016/2/18.
 */
public class MyIntentService extends IntentService {
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //打印当前线程的ID
        Log.d("MyIntentService", "Thread id is "+Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyIntentService", "onDestroy executed");
    }
}
