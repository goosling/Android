package com.example.joe.simplenews.weather.view;

import com.example.joe.simplenews.beans.WeatherBean;

import java.util.List;

/**
 * Created by JOE on 2016/6/6.
 */
public interface WeatherView {

    void showProgress();

    void hideProgress();

    void showWeatherLayout();

    void setCity(String city);

    void setToday(String date);

    void setTemperature(String temperature);

    void setWind(String wind);

    void setWeather(String weather);

    void setWeatherImage(int res);

    void setWeatherData(List<WeatherBean> list);

    void showErrorToast(String msg);
}
