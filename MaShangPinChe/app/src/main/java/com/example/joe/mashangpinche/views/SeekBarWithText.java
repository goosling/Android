package com.example.joe.mashangpinche.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by joe on 2016/4/26.
 */
public class SeekBarWithText extends SeekBar {

    private Drawable thumb;

    public SeekBarWithText(Context context) {
        this(context, null);
    }

    public SeekBarWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarWithText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setThumbWithText(String text) {

    }

}
