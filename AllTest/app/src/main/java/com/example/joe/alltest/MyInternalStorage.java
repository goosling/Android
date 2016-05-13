package com.example.joe.alltest;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by JOE on 2016/2/28.
 * 专门内部存储操作类，实现增删改查
 */
public class MyInternalStorage {
    private Context context;

    public MyInternalStorage(Context context) {
        this.context = context;
    }

    public void save(String filename, String content) throws IOException {
        File file = new File(context.getFilesDir(), filename);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.close();
    }

    public String get(String filename) throws IOException{
        FileInputStream fis = new FileInputStream(filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = -1;
        if((len = fis.read(data)) != -1) {
            baos.write(data, 0, len);
        }
        return new String(baos.toByteArray());
    }

    public void append(String filename, String content) throws IOException{
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND);
        fos.write(content.getBytes());
        fos.close();
    }

    public boolean delete(String filename) {
        return context.deleteFile(filename);
    }

    public String[] queryAllFile() {
        return context.fileList();
    }

    public void save2SDCard(String filename, String content) throws IOException {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("main", "本设备有存储卡");
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
        }
    }
}
