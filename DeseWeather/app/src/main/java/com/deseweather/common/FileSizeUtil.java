package com.deseweather.common;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * Created by JOE on 2016/3/11.
 */
public class FileSizeUtil {
    public static final int SIZETYPE_B = 1;
    public static final int SIZETYPE_KB = 2;
    public static final int SIZETYPE_MB = 3;
    public static final int SIZETYPE_GB = 4;

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try{
            if(file.isDirectory()) {
                blockSize = getFilesSize(file);
            }else {
                blockSize = getFileSize(file);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFilesOrFileSize(String filepath) {
        File file = new File(filepath);
        long blockSize = 0;
        try{
            if(file.isDirectory()) {
                blockSize = getFilesSize(file);
            }else {
                blockSize = getFileSize(file);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize);
    }

    private static long getFileSize(File file) throws Exception {
        long size = 0;
        FileInputStream fis = null;
        if(file.exists()) {
            fis = new FileInputStream(file);
            size = fis.available();
        }else {
            file.createNewFile();
        }
        return size;
    }

    private static long getFilesSize(File file) throws Exception {
        long size = 0;
        FileInputStream fis = null;
        File[] fileList = file.listFiles();
        int length = fileList.length;
        for(int i=0; i<length ;i++) {
            if(fileList[i].isDirectory()) {
                    size = size + getFilesSize(fileList[i]);
            }else {
                size = size + getFileSize(fileList[i]);
            }
        }
        return size;
    }

    private static String formatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if(fileSize == 0) {
            return wrongSize;
        }
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        }
        else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        }
        else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        }
        else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    private static double formatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
}
