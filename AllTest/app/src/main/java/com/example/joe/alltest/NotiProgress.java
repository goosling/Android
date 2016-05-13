package com.example.joe.alltest;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

/**
 * Created by JOE on 2016/2/29.
 */
public class NotiProgress extends Activity implements View.OnClickListener{

    private Button btnProgress, btnProgressCircle;
    NotificationManager manager;
    NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instore_layout);
        btnProgress = (Button)findViewById(R.id.append);
        btnProgressCircle = (Button)findViewById(R.id.query);

        btnProgress.setOnClickListener(this);
        btnProgressCircle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //设置有进度的进度条
            case R.id.append:
                manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                builder = new NotificationCompat.Builder(NotiProgress.this)
                        .setSmallIcon(R.mipmap.bmp1)
                        .setAutoCancel(true)
                        .setContentText("picture download in a progress")
                        .setContentTitle("picture downloading");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int increment=0; increment<=100; increment+=5) {
                            builder.setProgress(100, increment, false);
                            manager.notify(0, builder.build());
                            try{
                                Thread.sleep(3000);
                            }catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        builder.setContentText("download complete")
                                .setProgress(0, 0, false);
                        manager.notify(0, builder.build());
                    }
                }).start();
                break;
            //设置循环使用的进度条
            case R.id.query:
                manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                builder = new NotificationCompat.Builder(NotiProgress.this)
                        .setSmallIcon(R.mipmap.bmp1)
                        .setAutoCancel(true)
                        .setContentText("picture download in a progress")
                        .setContentTitle("picture downloading");
                builder.setProgress(0, 0, false);
                manager.notify(0, builder.build());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(5000);
                        }catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        builder.setProgress(100, 100, false);
                        manager.notify(0, builder.build());
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}
