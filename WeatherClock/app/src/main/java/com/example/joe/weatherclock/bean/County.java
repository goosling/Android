package com.example.joe.weatherclock.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by JOE on 2016/6/22.
 */
public class County extends DataSupport {

    private String countyName;
    private String weatherCode;

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
