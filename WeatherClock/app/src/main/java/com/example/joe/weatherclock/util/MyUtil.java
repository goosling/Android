package com.example.joe.weatherclock.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.joe.weatherclock.R;
import com.example.joe.weatherclock.bean.AlarmClock;
import com.example.joe.weatherclock.common.WeacConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by JOE on 2016/7/8.
 *
 * 工具类
 */
public class MyUtil {

    private static final String TAG = "MyUtil";

    /**
     * 保存壁纸信息
     *
     * @param context  context
     * @param saveType 保存类型：WeacConstants.WALLPAPER_NAME;WeacConstants.WALLPAPER_PATH
     * @param value    value
     */
    public static void saveWallPaper(Context context, String saveType, String value) {
        SharedPreferences sp = context.getSharedPreferences(WeacConstants.EXTRA_WEAC_SHARE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        switch(saveType) {
            case WeacConstants.WALLPAPER_NAME:
                editor.putString(WeacConstants.WALLPAPER_PATH, null);
                break;
            case WeacConstants.WALLPAPER_PATH:
                editor.putString(WeacConstants.WALLPAPER_NAME, null);
                break;
        }
        editor.putString(saveType, value);
        editor.apply();
    }

    /**
     * 设置壁纸
     *
     * @param vg       viewGroup
     * @param activity activity
     */
    @SuppressWarnings("deprecation")
    public static void setBackground(ViewGroup vg, Activity activity) {
        //取得主题背景信息
        SharedPreferences sp = activity.getSharedPreferences(WeacConstants.EXTRA_WEAC_SHARE,
                Activity.MODE_PRIVATE);
        String value = sp.getString(WeacConstants.WALLPAPER_PATH, null);
        //默认壁纸为自定义
        if(value != null) {
            //自定义壁纸
            Drawable drawable1 = Drawable.createFromPath(value);
            //文件没有被删除
            if(drawable1 != null) {
                vg.setBackground(drawable1);
            }else {
                saveWallPaper(activity, WeacConstants.WALLPAPER_NAME, WeacConstants.DEFAULT_WALLPAPER_NAME);
                setWallPaper(vg, activity, sp);
            }
        }else {
            setWallPaper(vg, activity, sp);
        }
        setStatusBarTranslucent(vg, activity);
    }

    public static void setStatusBarTranslucent(ViewGroup vg, Activity activity) {
        //如果版本在4.4以上
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏高度
            int height = getStatusBarHeight(activity);
            if(height <= 0) {
                return ;
            }

            vg.setPadding(0, height, 0, 0);
            //状态栏透明状态
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }

    public static void setStatusBarTranslucent(Activity activity) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        int height = 0;
        int resId = activity.getResources().getIdentifier("status_bar_height"
            , "dimen", "android");
        if(resId > 0) {
            height = activity.getResources().getDimensionPixelSize(resId);
        }

        return height;
    }

    private static void setWallPaper(ViewGroup vg, Activity activity, SharedPreferences sp) {
        int resId = getResId(activity, sp);
        vg.setBackgroundResource(resId);
    }

    private static int getResId(Context context, SharedPreferences sp) {
        String value = sp.getString(WeacConstants.WALLPAPER_NAME, WeacConstants.DEFAULT_WALLPAPER_NAME);
//        int resId = context.getApplicationContext().getResources().getIdentifier(
//                value, "drawable", context.getPackageName());

        Class drawable = R.drawable.class;
        int resId;
        try {
            Field field = drawable.getField(value);
            resId = field.getInt(field.getName());
        } catch (Exception e) {
            resId = R.drawable.wallpaper_0;
            LogUtil.e(TAG, "setWallPaper(Context context): " + e.toString());
        }
        return resId;
    }

    //设置模糊壁纸
    public static void setBackgroundBlur(ViewGroup vg, Activity activity) {
        vg.setBackgroundDrawable(getWallpaperBlurDrawable(activity));
        setStatusBarTranslucent(activity);
    }

    //取得模糊处理之后的壁纸资源
    public static Drawable getWallpaperBlurDrawable(Context context) {
        Bitmap bitmap;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //取得背景主题
        SharedPreferences sp = context.getSharedPreferences(WeacConstants.EXTRA_WEAC_SHARE,
                Activity.MODE_PRIVATE);
        String value = sp.getString(WeacConstants.WALLPAPER_PATH, null);

        if(value != null) {
            try{
                BitmapFactory.decodeStream(new FileInputStream(new File(value)), null, options);
                //设置图片模糊为20
                options.inSampleSize = 20;
                options.inJustDecodeBounds = false;
                //再次解析图片
                bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(value)), null ,options);
                bitmap = fastBlur(context, 0, value, bitmap, 20);
            }catch (FileNotFoundException e) {
                LogUtil.e(TAG, "getWallPaperBlurDrawable(Context context): " + e.toString());
                bitmap = setWallpaperBlur(context, options, sp);
            }
        }else {
            bitmap = setWallpaperBlur(context, options, sp);
        }

        // 返回经过毛玻璃模糊度20处理后的Bitmap
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private static Bitmap setWallpaperBlur(Context context, BitmapFactory.Options options, SharedPreferences share) {
        int resId = getResId(context, share);
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        // 设置图片模糊度为20
        options.inSampleSize = 20;
        options.inJustDecodeBounds = false;
        // 使用设置的inSampleSize值再次解析图片
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        bitmap = fastBlur(context, resId, null, bitmap, 20);
        return bitmap;
    }

    private static Bitmap fastBlur(Context context, int resId, String filePath, Bitmap sentBitmap,
                                   int radius) {
        try {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1) {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
//        Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int temp = 256 * divsum;
            int dv[] = new int[temp];
            for (i = 0; i < temp; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) |
                            dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

//        Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return (bitmap);
        } catch (Exception e) {
            LogUtil.e("MyUtil", e.toString());
/*            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                return blurBitmap(context, sentBitmap, radius);
            } else {*/
            if (filePath == null) {
                return BitmapFactory.decodeResource(context.getResources(), resId);
            } else {
                return BitmapFactory.decodeFile(filePath);
            }
//            }
        }
    }

    public static void startAlarmClock(Context context, AlarmClock alarmClock) {
//        Intent intent = new Intent("com.kaku.weac.broadcast.ALARM_CLOCK_ONTIME");
        Intent intent = new Intent(context, AlarmClockBroadcast.class);
        intent.putExtra(WeacConstants.ALARM_CLOCK, alarmClock);
        // FLAG_UPDATE_CURRENT：如果PendingIntent已经存在，保留它并且只替换它的extra数据。
        // FLAG_CANCEL_CURRENT：如果PendingIntent已经存在，那么当前的PendingIntent会取消掉，然后产生一个新的PendingIntent。
        PendingIntent pi = PendingIntent.getBroadcast(context,
                alarmClock.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        // 取得下次响铃时间
        long nextTime = calculateNextTime(alarmClock.getHour(),
                alarmClock.getMinute(), alarmClock.getWeeks());
        // 设置闹钟
        // 当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pi);
        }

    }

    /**
     * 取消闹钟
     *
     * @param context        context
     * @param alarmClockCode 闹钟启动code
     */
    public static void cancelAlarmClock(Context context, int alarmClockCode) {
        Intent intent = new Intent(context, AlarmClockBroadcast.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, alarmClockCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context
                .getSystemService(Activity.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * 开启倒计时
     *
     * @param context    context
     * @param timeRemain 剩余时间
     */
    public static void startClockTimer(Context context, long timeRemain) {
        Intent intent = new Intent(context, TimerOnTimeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 1000, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        long countdownTime = timeRemain + SystemClock.elapsedRealtime();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, countdownTime, pi);
        }else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, countdownTime, pi);
        }
    }

    /**
     * 取得下次响铃时间
     *
     * @param hour   小时
     * @param minute 分钟
     * @param weeks  周
     * @return 下次响铃时间
     */
    public static long calculateNextTime(int hour, int minute, String weeks) {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long nextTime = calendar.getTimeInMillis();
        //当单次响铃时
        if(weeks == null) {
            //当设置时间大于系统时间时
            if(nextTime > now) {
                return nextTime;
            }else {
                //设置时间加一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                nextTime = calendar.getTimeInMillis();
                return nextTime;
            }
        }else {
            nextTime = 0;
            //临时比较用响铃时间
            long tempTime;

            final String[] weeksValue = weeks.split(",");
            for(String aWeeksValue: weeksValue) {
                int week = Integer.parseInt(aWeeksValue);
                //设置重复的周
                calendar.set(Calendar.DAY_OF_WEEK, week);
                tempTime = calendar.getTimeInMillis();
                if(tempTime <= now) {
                    //设置时间加7天
                    tempTime += AlarmManager.INTERVAL_DAY * 7;
                }
                if(nextTime == 0) {
                    nextTime = tempTime;
                }else {
                    nextTime = Math.min(tempTime, nextTime);
                }
            }
        }
        return nextTime;
    }

    /**
     * 转换文件大小
     *
     * @param fileLength file
     * @param pattern    匹配模板 "#.00","0.0"...
     * @return 格式化后的大小
     */
    public static String formatFileSize(long fileLength, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        String fileSizeString;
        if(fileLength < 1024) {
            fileSizeString = "0KB";
        } else if(fileLength < 1048576) {
            fileSizeString = df.format((double)fileLength / 1024) + "KB";
        }else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String formatFileDuration(int ms) {
        // 单位秒
        int ss = 1000;
        // 单位分
        int mm = ss * 60;

        // 剩余分钟
        int remainMinute = ms / mm;
        // 剩余秒
        int remainSecond = (ms - remainMinute * mm) / ss;

        return addZero(remainMinute) + ":"
                + addZero(remainSecond);

    }

    /**
     * 格式化时间
     *
     * @param hour   小时
     * @param minute 分钟
     * @return 格式化后的时间:[xx:xx]
     */
    public static String formatTime(int hour, int minute) {
        return addZero(hour) + ":" + addZero(minute);
    }

    /**
     * 时间补零
     *
     * @param time 需要补零的时间
     * @return 补零后的时间
     */
    public static String addZero(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }

        return String.valueOf(time);
    }

    /**
     * 振动单次100毫秒
     *
     * @param context context
     */
    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    /**
     * 去掉文件扩展名
     *
     * @param fileName 文件名
     * @return 没有扩展名的文件名
     */
    public static String removeEx(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < fileName.length())) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context context
     * @return 是否连接到网络
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {

                    return true;
                }
            }
        }
        return false;
    }

    private static long mLastClickTime = 0;             // 按钮最后一次点击时间
    private static final int SPACE_TIME = 500;          // 空闲时间

    /**
     * 是否连续点击按钮多次
     *
     * @return 是否快速多次点击
     */
    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - mLastClickTime <= SPACE_TIME) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }

    


}

