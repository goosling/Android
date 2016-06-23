package com.example.joe.weatherclock.bean.Event;

/**
 * Created by JOE on 2016/6/22.
 */
public class ScanCodeEvent {

    private String mImageUrl;

    public String getImageUrl() {
        return mImageUrl;
    }

    public ScanCodeEvent(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
