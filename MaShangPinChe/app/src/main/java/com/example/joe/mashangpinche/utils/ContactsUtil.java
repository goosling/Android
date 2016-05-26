package com.example.joe.mashangpinche.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by JOE on 2016/5/26.
 */
public class ContactsUtil extends Activity{

    /**
     * 1 读取用户日志需要权限android.permission.READ_LOGS

    2 在一个服务中开启logcat程序，然后读取*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread() {
            @Override
            public void run() {
                try{
                    File file = new File(Environment.getExternalStorageDirectory(), "log.txt");
                    FileOutputStream fos = new FileOutputStream(file);
                    Process process = Runtime.getRuntime().exec("logcat");
                    InputStream in = process.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while((line = br.readLine()) != null) {
                        if(line.contains("I/ActivityManager")) {
                            fos.write(line.getBytes());
                            fos.flush();
                        }
                    }
                    fos.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
