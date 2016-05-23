package com.example.joe.mashangpinche.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JOE on 2016/5/22.
 */
public class MultiThreadDownloader {
    //目标地址
    private URL url;

    //本地文件
    private File file;

    //每个线程下载多少内容
    private long threadLen;

    //线程数量
    private static final int THREAD_AMOUNT = 3;

    //下载目录
    private static final String DIR_PATH = "D:/sth";

    public MultiThreadDownloader(String address) throws IOException {
        url = new URL(address);
        //截取地址中的文件名
        file = new File(DIR_PATH, address.substring(address.lastIndexOf("/")+1));
    }

    public void download() throws IOException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(3000);

        //获取文件总长度
        long totalLen = conn.getContentLength();
        threadLen = (totalLen + THREAD_AMOUNT - 1)/THREAD_AMOUNT;

        // 总长度 如果能整除 线程数, 每条线程下载的长度就是 总长度 / 线程数
        // 总长度 如果不能整除 线程数, 那么每条线程下载长度就是 总长度 / 线程数 + 1

        // 在本地创建一个和服务端大小相同的文件
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(totalLen);
        raf.close();

        for(int i=0; i < THREAD_AMOUNT; i++) {
            new DownloadThread(i).start();
        }
    }

    private class DownloadThread extends Thread {
        //用来标记当前下载为第几个线程
        private int id;

        public DownloadThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            //从临时文件读取当前线程已经完成的状况
            //起始位置
            long start = id * threadLen;
            //结束位置
            long end = id * threadLen + threadLen - 1;
            //System.out.print("线程" + id + ": " + start + "-" + end);
            try{
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestProperty("Range", "bytes=" + start + "-" + "end");

                InputStream in = conn.getInputStream();
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(start);

                //每次拷贝100kb
                byte[] buffer = new byte[1024 * 100];
                int len;
                while((len=in.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);
                }
                raf.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
