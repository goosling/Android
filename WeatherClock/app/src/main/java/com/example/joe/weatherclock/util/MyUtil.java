package com.example.joe.weatherclock.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.joe.weatherclock.common.WeacConstants;

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
    }
}
