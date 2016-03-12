package com.deseweather.modules.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.deseweather.base.BaseApplication;

/**
 * Created by JOE on 2016/3/11.
 */
public class Setting {

    public static final String CHANGE_ICONS = "change_icons";//切换图标
    public static final String CLEAR_CACHE = "clear_cache";//清空缓存
    public static final String AUTO_UPDATE = "change_update_time";
    public static final String CITY_NAME = "城市";
    public static final String HOUR = "小时";
    public static final String HOUR_SElECT = "hour_select";

    public static final String API_TOKEN = "";
    public static final String KEY = "3e481cbfe41942a880edb369c83b4364";//和风天气key

    public static int ONE_HOUR = 3600;

    private static Setting mInstance;

    private SharedPreferences sp;

    public static Setting getInstance() {
        if (mInstance == null) {
            mInstance = new Setting(BaseApplication.mAppContext);
        }
        return mInstance;
    }

    private Setting(Context context) {
        sp = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public Setting putBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean def) {
        return sp.getBoolean(key, def);
    }

    public Setting putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public Setting putString(String key, String value) {
        sp.edit().putString(key, value);
        return this;
    }

    public String getString(String key, String value) {
        return sp.getString(key, value);
    }


}
