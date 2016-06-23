package com.example.joe.weatherclock.bean;

/**
 * Created by JOE on 2016/6/22.
 *
 *  保存默认主题壁纸名和壁纸ID
 */
public class Theme {

    private String mResName;
    private int mResId;

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public String getResName() {
        return mResName;
    }

    public void setResName(String resName) {
        mResName = resName;
    }
}
