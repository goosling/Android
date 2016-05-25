package com.example.joe.mashangpinche.utils;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

/**
 * Created by JOE on 2016/5/24.
 */
public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "MyExceptionHandler";

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try{
            Field[] fields = Build.class.getDeclaredFields();
            StringBuffer sb = new StringBuffer();
            for(Field field : fields) {
                String info = field.getName() + ":" + field.get(null) +"\n";
                sb.append(info);
            }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String errorlog = sw.toString();
            File file = new File(Environment.getExternalStorageDirectory(), "error.log");
            FileOutputStream fos = new FileOutputStream(file);
            sb.append(errorlog);
            fos.write(sb.toString().getBytes());
            fos.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
