package com.example.joe.mashangpinche.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joe.mashangpinche.activities.IwantUApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    /**
     * 检测editText是否合法输入*/
    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    public static String getTaServiceType(String myServiceType) {
        // TODO Auto-generated method stub
        if (SERVICE_TYPES[0].equals(myServiceType)) {
            return SERVICE_TYPES[1];
        } else if (SERVICE_TYPES[1].equals(myServiceType)) {
            return SERVICE_TYPES[0];
        } else if (SERVICE_TYPES[2].equals(myServiceType)) {
            return SERVICE_TYPES[2];
        } else {
            return "";
        }
    }

    public String getIMEI() {
        TelephonyManager telMgr = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        String IMEI = telMgr.getDeviceId();
        return IMEI.toString();
        //return telMgr.getDeviceId().toString();
    }

    /**
     * 检查姓的合法性
     *
     * @return
     */
    public static boolean checkLastName() {
        return true;
    }

    /**
     * 日后要改进检测机制，比如考虑到扩展号码字段
     *
     * @param pn
     * @return
     */
    public static boolean isPhoneNumber(String pn) {
        int len = pn.length();
        byte[] bytes = pn.getBytes();
        int i = 0;

        if(11 == len) {
            i = 0;
        }else if(13 == len) {
            if(('8' == bytes[0]) && ('6' == bytes[1])) {
                i = 2;
            }else {
                return false;
            }
        }else if (14 == len) {
            if(('+' == bytes[0]) && ('8' == bytes[0]) && ('6' == bytes[1])) {
                i = 3;
            }else {
                return false;
            }
        }else {
            return false;
        }

        if('1' != bytes[i]) {
            return false;
        }
        i++;

        for(; i<len; i++) {
            if(('0' > bytes[i]) || ('9' < bytes[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean preCheckVerCode(String code) {
        if (null == code || CONS_VCODE_LENGTH != code.length()) {
            return false;
        }
        return true;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dipRes2Px(Context context, int resId) {
        float f = context.getResources().getDimension(resId);
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (f * scale);
    }

    /**
     * 释放imageview中的bitmap资源，以节省内存。
     * @param iv
     */
    public static void recycleImageViewBitmap(ImageView iv) {
        if(iv == null) {
            return;
        }
        Drawable drawable = iv.getDrawable();
        if(drawable == null) {
            return;
        }

        if(drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if(bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
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

    public static void toast(Context context, int resId) {
        Toast.makeText(context.getApplicationContext(),
                resId, Toast.LENGTH_SHORT)
                .show();
    }
}
