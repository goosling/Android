package com.example.joe.simplenews.beans;

import java.io.Serializable;

/**
 * Created by JOE on 2016/6/3.
 * 天气实体类
 */
public class WeatherBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String temperature;

    private String weather;

    private String wind;

    private String week;

    private String date;

    private int imageRes;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
