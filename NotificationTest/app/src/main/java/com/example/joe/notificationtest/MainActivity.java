package com.example.joe.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button sendNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendNotice = (Button)findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.send_notice:
                        NotificationManager manager = (NotificationManager)getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );
                        /*Notification notification = new Notification(R.mipmap.ic_launcher,
                                "This is a ticker text", System.currentTimeMillis());

                        notification.setLatestEventInfo(MainActivity.this, "This is content title",
                                "This is content text", pi);*/

                        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent,
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        //api13到api16这么写
                        /*@TargetApi(15)
                        Notification.Builder builder = new Notification.Builder(MainActivity.this)
                                .setAutoCancel(true)
                                .setContentTitle("This is content title")
                                .setContentText("This is content text")
                                .setContentIntent(pi)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setWhen(System.currentTimeMillis())
                                .setOngoing(true);
                        notification = builder.getNotification();*/
                        //api16朝上这么写
                        Notification notification = new Notification.Builder(MainActivity.this)
                                .setAutoCancel(true)
                                .setContentTitle("title")
                                .setContentText("describe")
                                .setContentIntent(pi)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setWhen(System.currentTimeMillis())
                                .build();

                        manager.notify(1, notification);
                        break;
                    default:
                        break;
                }
            }
        });

    }


}
