package com.example.joe.simplenews.weather.model;

import android.content.Context;

/**
 * Created by JOE on 2016/6/6.
 */
public interface WeatherModel {

    void loadWeatherData(String cityName, WeatherModelImpl.LoadWeatherListener listener);

    void loadLocation(Context context, WeatherModelImpl.LoadLocationListener listener);
}
