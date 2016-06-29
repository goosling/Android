package com.example.joe.weatherclock.adapter;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joe.weatherclock.bean.CityManager;

/**
 * Created by JOE on 2016/6/29.
 */
public class CityManageAdapter extends ArrayAdapter<CityManager> {

    /**
     * 保存控件实例
     */
    private final class ViewHolder {
        // 城市天气控件
        ViewGroup cityWeather;
        // 城市名
        TextView cityName;
        // 天气类型图片
        ImageView weatherTypeIv;
        // 高温
        TextView tempHigh;
        // 低温
        TextView tempLow;
        // 天气类型文字
        TextView weatherTypeTv;
        // 设置默认
        TextView setDefaultTv;
        // 添加城市按钮
        ImageView addCityIv;
        // 删除城市按钮
        ImageView deleteCityBtn;
        // 控件布局
        ViewGroup background;
        // 进度条
        ProgressBar progressBar;
    }
}
