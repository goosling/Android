package com.deseweather.modules.domain;

import java.io.Serializable;

/**
 * Created by JOE on 2016/3/11.
 */
public class City implements Serializable {

    private String cityName;
    private int provinceID;

    public int getProvinceID() {
        return provinceID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }
}
