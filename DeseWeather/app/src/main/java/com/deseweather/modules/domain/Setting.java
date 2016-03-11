package com.deseweather.modules.domain;

import android.content.SharedPreferences;

import com.deseweather.base.BaseApplication;

/**
 * Created by JOE on 2016/3/11.
 */
public class Setting {

    private static final String CHANGE_ICONS = "change_icons";//切换图标
    private static final String CLEAR_CACHE = "clear_cache";//清空缓存
    private static final String AUTO_UPDATE = "change_update_time";
    private static final String CITY_NAME = "城市";
    private static final String HOUR = "小时";
    private static final String HOUR_SElECT = "hour_select";

    private static final String API_TOKEN = "";
    private static final String KEY = "3e481cbfe41942a880edb369c83b4364";//和风天气key

    public static int ONE_HOUR = 3600;

    private static Setting mInstance;

    private SharedPreferences sp;

    public static Setting getInstance() {
        if(mInstance == null) {
            mInstance = new Setting(BaseApplication.mAppContext);
        }
    }


}
