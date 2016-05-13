package com.example.joe.mashangpinche.db;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by joe on 2016/4/28.
 */
@Root(name = "destination")
public class Destination implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Element
    private String cityName;
    @Element
    private String destName;
    @Element
    private double latitude;
    @Element
    private double longitude;
    public String getDestName() {
        return destName;
    }
    public void setDestName(String destName) {
        this.destName = destName;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
