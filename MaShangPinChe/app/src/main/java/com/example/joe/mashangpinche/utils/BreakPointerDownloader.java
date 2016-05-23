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
public class BreakPointerDownloader {
    //线程数量
    private static final int THREAD_AMOUNT = 3;

    //下载目录
    private static final String DIR_PATH = "D:/sth";

    private URL url;            // 目标下载地址
    private File dataFile;        // 本地文件
    private File tempFile;        // 用来存储每个线程下载的进度的临时文件
    private long threadLen;        // 每个线程要下载的长度
    private long totalFinish;    // 总共完成了多少
    private long totalLen;        // 服务端文件总长度
    private long begin;            // 用来记录开始下载时的时间

    public BreakPointerDownloader(String address) throws IOException{
        url = new URL(address);
        dataFile = new File(DIR_PATH, address.substring(address.lastIndexOf("/")+1));
        tempFile = new File(dataFile.getAbsolutePath()+".temp");
    }

    public void download() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3000);

        totalLen = conn.getContentLength();                                    // 获取服务端发送过来的文件长度
        threadLen = (totalLen + THREAD_AMOUNT - 1) / THREAD_AMOUNT;            // 计算每个线程要下载的长度

        if(!dataFile.exists()) {
            RandomAccessFile raf = new RandomAccessFile(dataFile, "rws");
            raf.setLength(totalLen);
            raf.close();
        }

        if(!tempFile.exists()) {
            RandomAccessFile raf = new RandomAccessFile(tempFile, "rws");
            for(int i=0; i < THREAD_AMOUNT; i++) {
                raf.writeLong(0);
            }
            raf.close();
        }

        for(int i=0; i < THREAD_AMOUNT; i++) {
            new DownloadThread(i).start();
        }

        begin = System.currentTimeMillis();
    }

    private class DownloadThread extends Thread {
        //用来标记当前线程是下载的第几个线程
        private int id;

        public DownloadThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try{
                RandomAccessFile tempRaf = new RandomAccessFile(tempFile, "rws");
                tempRaf.seek(id * 8);
                //读取当前线程已经完成了多少
                long threadFinish = tempRaf.readLong();
                synchronized (BreakPointerDownloader.this) {
                    totalFinish += threadFinish;
                }

                //计算当前线程的起始位置
                long start = id * threadLen + threadFinish;
                //计算当前线程的结束位置
                long end = id * threadLen + threadLen - 1;

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestProperty("Range", "bytes=" + start + "-" + end);        // 设置当前线程下载的范围

                InputStream in = conn.getInputStream();
                RandomAccessFile dataRaf = new RandomAccessFile(dataFile, "rws");
                // 设置当前线程保存数据的位置
                dataRaf.seek(start);

                //每次下载100kb
                byte[] buffer = new byte[1024 * 100];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    dataRaf.write(buffer, 0, len);                // 从服务端读取数据, 写到本地文件
                    threadFinish += len;                        // 每次写入数据之后, 统计当前线程完成了多少
                    tempRaf.seek(id * 8);                        // 将临时文件的指针指向当前线程的位置
                    tempRaf.writeLong(threadFinish);            // 将当前线程完成了多少写入到临时文件
                    synchronized(BreakPointerDownloader.this) {    // 多个下载线程之间同步
                        totalFinish += len;                        // 统计所有线程总共完成了多少
                    }
                }
                dataRaf.close();
                tempRaf.close();

                System.out.println("线程" + id + "下载完毕");
                if (totalFinish == totalLen) {                    // 如果已完成长度等于服务端文件长度(代表下载完成)
                    System.out.println("下载完成, 耗时: " + (System.currentTimeMillis() - begin));
                    tempFile.delete();                            // 删除临时文件
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new BreakPointerDownloader("http://192.168.1.240:8080/14.Web/android-sdk_r17-windows.zip").download();
    }
}
