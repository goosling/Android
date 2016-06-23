package com.example.joe.weatherclock.bean.Event;

/**
 * Created by JOE on 2016/6/22.
 */
public class QRcodeLogoEvent {
    private String mLogoPath;

    public QRcodeLogoEvent(String logoPath) {
        mLogoPath = logoPath;
    }

    public String getLogoPath() {
        return mLogoPath;
    }


}
