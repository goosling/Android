package com.example.joe.mashangpinche.utils;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.joe.mashangpinche.activities.IwantUApp;

/**
 * Created by joe on 2016/4/30.
 */
public class AppUtil extends Application {

    public static final String[] SERVICE_TYPES = {
            IwantUApp.CONS_SERVICE_TYPE_CUSTOMER,
            IwantUApp.CONS_SERVICE_TYPE_MERCHANT,
            IwantUApp.CONS_SERVICE_TYPE_MUTUAL };

    public static int CONS_VCODE_LENGTH = 4;
    public AppUtil() {

    }

    public static Bitmap decodeBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int insampleSize = 1;

        if(height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float)height / (float)reqHeight);
            final int widthRatio = Math.round((float)width / (float)reqWidth);

            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            insampleSize = heightRatio < widthRatio? heightRatio : widthRatio;
        }
        return insampleSize;
    }
}
