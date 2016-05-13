package com.deseweather.modules.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/3/11.
 */
public class WeatherAPI {
    @SerializedName("HeWeather data service 3.0") @Expose
    public List<Weather> mWeatherDataService = new ArrayList<Weather>();
}
