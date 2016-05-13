package com.example.joe.notificationtest;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

/**
 * Created by JOE on 2016/2/17.
 */
public class NotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        //消除通知图标
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);//1即为通知的id
    }
}
