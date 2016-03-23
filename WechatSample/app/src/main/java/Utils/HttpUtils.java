package Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import db.DiskLruCache;

/**
 * Created by JOE on 2016/3/23.
 */
public class HttpUtils {
    DiskLruCache mDiskLruCache = null;

    //获取缓存地址
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath+ File.separator + uniqueName);
    }


    //获取当前应用的版本号
    public int getAppVersion(Context context) {
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        }catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    //DiskLrucache标准的Open方法
    public DiskLruCache openDiskLrucache(Context context) {

        try {
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if(!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10*1024*1024);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return mDiskLruCache;
    }

    //写入缓存
    private boolean downloadUrlToStream(String urlString, OutputStream os) {
        HttpURLConnection connection = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try{
            final URL url = new URL(urlString);
            connection = (HttpURLConnection)url.openConnection();
            bis = new BufferedInputStream(connection.getInputStream(), 8*1024);
            bos = new BufferedOutputStream(os, 8*1024);
            int b;
            while((b=bis.read()) != -1) {
                bos.write(b);
            }
            return true;
        }catch (final IOException e) {
            e.printStackTrace();
        }finally {
            if(connection != null) {
                connection.disconnect();
            }
            try{
                if(bos != null) {
                    bos.close();
                }
                if(bis != null) {
                    bis.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //将字符串进行MD5编码
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try{
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        }catch(NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //得到DiskLruCache实例
    public void getEdit() {
        String imgUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
        String key = hashKeyForDisk(imgUrl);
        try{
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


}
