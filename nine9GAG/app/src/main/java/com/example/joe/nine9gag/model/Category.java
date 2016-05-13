package com.example.joe.nine9gag.model;

/**
 * Created by joe on 2016/4/19.
 */
public enum Category {
    hot("hot"), trending("trending"), fresh("fresh");
    private String mDisplayName;

    Category(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

}
