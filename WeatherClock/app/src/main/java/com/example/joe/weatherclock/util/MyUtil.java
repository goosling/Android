package com.example.joe.weatherclock.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 取得对应的天气类型图片id
     *
     * @param type  天气类型
     * @param isDay 是否为白天
     * @return 天气类型图片id
     */
    public static int getWeatherTypeImageID(String type, boolean isDay) {
        if (type == null) {
            return R.drawable.ic_weather_no;
        }
        int weatherId;
        switch (type) {
            case "晴":
                if (isDay) {
                    weatherId = R.drawable.ic_weather_sunny_day;
                } else {
                    weatherId = R.drawable.ic_weather_sunny_night;
                }
                break;
            case "多云":
                if (isDay) {
                    weatherId = R.drawable.ic_weather_cloudy_day;
                } else {
                    weatherId = R.drawable.ic_weather_cloudy_night;
                }
                break;
            case "阴":
                weatherId = R.drawable.ic_weather_overcast;
                break;
            case "雷阵雨":
            case "雷阵雨伴有冰雹":
                weatherId = R.drawable.ic_weather_thunder_shower;
                break;
            case "雨夹雪":
                weatherId = R.drawable.ic_weather_sleet;
                break;
            case "冻雨":
                weatherId = R.drawable.ic_weather_ice_rain;
                break;
            case "小雨":
            case "小到中雨":
            case "阵雨":
                weatherId = R.drawable.ic_weather_light_rain_or_shower;
                break;
            case "中雨":
            case "中到大雨":
                weatherId = R.drawable.ic_weather_moderate_rain;
                break;
            case "大雨":
            case "大到暴雨":
                weatherId = R.drawable.ic_weather_heavy_rain;
                break;
            case "暴雨":
            case "大暴雨":
            case "特大暴雨":
            case "暴雨到大暴雨":
            case "大暴雨到特大暴雨":
                weatherId = R.drawable.ic_weather_storm;
                break;
            case "阵雪":
            case "小雪":
            case "小到中雪":
                weatherId = R.drawable.ic_weather_light_snow;
                break;
            case "中雪":
            case "中到大雪":
                weatherId = R.drawable.ic_weather_moderate_snow;
                break;
            case "大雪":
            case "大到暴雪":
                weatherId = R.drawable.ic_weather_heavy_snow;
                break;
            case "暴雪":
                weatherId = R.drawable.ic_weather_snowstrom;
                break;
            case "雾":
                weatherId = R.drawable.ic_weather_foggy;
                break;
            case "霾":
                weatherId = R.drawable.ic_weather_haze;
                break;
            case "沙尘暴":
                weatherId = R.drawable.ic_weather_duststorm;
                break;
            case "强沙尘暴":
                weatherId = R.drawable.ic_weather_sandstorm;
                break;
            case "浮尘":
            case "扬沙":
                weatherId = R.drawable.ic_weather_sand_or_dust;
                break;
            default:
                if (type.contains("尘") || type.contains("沙")) {
                    weatherId = R.drawable.ic_weather_sand_or_dust;
                } else if (type.contains("雾") || type.contains("霾")) {
                    weatherId = R.drawable.ic_weather_foggy;
                } else if (type.contains("雨")) {
                    weatherId = R.drawable.ic_weather_ice_rain;
                } else if (type.contains("雪") || type.contains("冰雹")) {
                    weatherId = R.drawable.ic_weather_moderate_snow;
                } else {
                    weatherId = R.drawable.ic_weather_no;
                }
                break;
        }

        return weatherId;
    }


    /**
     * 取得天气类型描述
     *
     * @param type1 白天天气类型
     * @param type2 夜间天气类型
     * @return 天气类型
     */
    public static String getWeatherType(Context context, String type1, String type2) {
        if(type1.equals(type2)) {
            return type1;
        }else {
            return String.format(context.getString(R.string.turn), type1, type2);
        }
    }

    /**
     * 将地址信息转换为城市
     *
     * @param address 地址
     * @return 城市名称
     */
    public static String formatCity(String address) {
        String city = null;

        if (address.contains("自治州")) {
            if (address.contains("市")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("市"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("县"));
            } else if (address.contains("地区")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("地区"));
            }

        } else if (address.contains("自治区")) {
            if (address.contains("地区") && address.contains("县")) {
                city = address.substring(address.indexOf("地区") + 2, address.indexOf("县"));
            } else if (address.contains("地区")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("地区"));
            } else if (address.contains("市")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("市"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("县"));
            }

        } else if (address.contains("地区")) {
            if (address.contains("县")) {
                city = address.substring(address.indexOf("地区") + 2, address.indexOf("县"));
            }

        } else if (address.contains("香港")) {
            if (address.contains("九龙")) {
                city = "九龙";
            } else if (address.contains("新界")) {
                city = "新界";
            } else {
                city = "香港";
            }

        } else if (address.contains("澳门")) {
            if (address.contains("氹仔")) {
                city = "氹仔岛";
            } else if (address.contains("路环")) {
                city = "路环岛";
            } else {
                city = "澳门";
            }

        } else if (address.contains("台湾")) {
            if (address.contains("台北")) {
                city = "台北";
            } else if (address.contains("高雄")) {
                city = "高雄";
            } else if (address.contains("台中")) {
                city = "台中";
            }

        } else if (address.contains("省")) {
            if (address.contains("市") && address.contains("县")) {
                city = address.substring(address.lastIndexOf("市") + 1, address.indexOf("县"));
            } else if (!address.contains("市") && address.contains("县")) {
                city = address.substring(address.indexOf("省") + 1, address.indexOf("县"));
            } else if (!address.contains("市")) {
                int start = address.indexOf("市");
                int end = address.lastIndexOf("市");
                if (start == end) {
                    city = address.substring(address.indexOf("省") + 1, end);
                } else {
                    city = address.substring(start, end);
                }
            }

        } else if (address.contains("市")) {
/*            if (address.contains("区")) {
                city = address.substring(address.indexOf("市") + 1, address.indexOf("区"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("市") + 1, address.indexOf("县"));
            }*/
            if (address.contains("中国")) {
                city = address.substring(address.indexOf("国") + 1, address.indexOf("市"));
            } else {
                city = address.substring(0, address.indexOf("市"));
            }
        }

        return city;
    }

    public static File getFileDirectory(Context context, String path) {
        File file = null;
        if (isHasSDCard()) {
            file = new File(Environment.getExternalStorageDirectory(), path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    file = null;
                }
            }
        }
        if (file == null) {
            // 使用内部缓存[MediaStore.EXTRA_OUTPUT ("output")]是无法正确写入裁切后的图片的。
            // 系统是用全局的ContentResolver来做这个过程的文件io操作，app内部的存储被忽略。（猜测）
            file = new File(context.getFilesDir(), path);
        }
        return file;
    }

    public static File getExternalFileDirectory(Context context, String path) {
        File file = null;
        if (isHasSDCard()) {
            file = new File(context.getExternalFilesDir(null), path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    file = null;
                }
            }
        }
        if (file == null) {
            // 使用内部缓存[MediaStore.EXTRA_OUTPUT ("output")]是无法正确写入裁切后的图片的。
            // 系统是用全局的ContentResolver来做这个过程的文件io操作，app内部的存储被忽略。（猜测）
            file = new File(context.getFilesDir(), path);
        }
        return file;
    }

    public static boolean isHasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String getFilePath(Context context, String path) {
        return getExternalFileDirectory(context, path).getAbsolutePath();
    }

    /**
     * set intent options
     *
     * @param context  context
     * @param uri      image path uri
     * @param filepath save path (e.g.: "/AppDir/a.mp3", "/AppDir/files/images/a.jpg")
     * @param type     0，截取壁纸/拍照；1，截取Logo
     * @return Intent
     */
    public static Intent getCropImageOptions(Context context, Uri uri, String filepath, int type) {
        int height;
        int width;

        //截取壁纸、拍照
        if(type == 0) {
            width = context.getResources().getDisplayMetrics().widthPixels;
            height = context.getResources().getDisplayMetrics().heightPixels;
        }else {
            //截取logo
            width = height = dip2px(context, 30);
        }

        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框比例
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        // 保存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getExternalFileDirectory
                (context, filepath)));
        // 是否去除面部检测
        intent.putExtra("noFaceDetection", true);
        // 是否保留比例
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 裁剪区的宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        // 是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", false);
        return intent;
    }

    /**
     * 网址验证
     *
     * @param url 需要验证的内容
     */
    public static boolean checkWebsite(String url) {
        String format = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+" +
                "([\\w\\-\\.,@?^=%&;:/~\\+#]*[\\w\\-\\@?^=%&;/~\\+#])?";

        return startCheck(format, url);
    }

    /**
     * 网址路径（无协议部分）验证
     *
     * @param url 需要验证的路径
     */
    public static boolean checkWebSitePath(String url) {
        String format = "[\\w\\-_]+(\\.[\\w\\-_]+)+" +
                "([\\w\\-\\.,@?^=%&;:/~\\+#]*[\\w\\-\\@?^=%&;/~\\+#])?";
        return startCheck(format, url);
    }

    /**
     * 匹配正则表达式
     *
     * @param format 匹配格式
     * @param str    匹配内容
     * @return 是否匹配成功
     */
    private static boolean startCheck(String format, String str) {
        boolean tem;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(str);

        tem = matcher.matches();
        return tem;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * 保存自定义二维码logo地址
     */
    public static void saveQRcodeLogoPath(Context context, String logoPath) {
        SharedPreferences share = context.getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        edit.putString(WeacConstants.QRCODE_LOGO_PATH, logoPath);
        edit.apply();
    }

    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 获取应用版本号
     *
     * @param context context
     * @return 版本号
     */
    public static String getVersion(Context context) {
        String version;
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(TAG, "assignViews: " + e.toString());
            version = context.getString(R.string.version);
        }
        return version;
    }

}

