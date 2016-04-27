package com.example.joe.mashangpinche.views;

import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by joe on 2016/4/26.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

    /**
     * 当前用户是否在触摸hsv*/
    private boolean currentlyTouching = false;

    /**
     * subview的宽度*/
    private int itemWidth;

    /**
	 * hsv 里面的LinearLayout，它用来装载subview。
	 */
    private LinearLayout mLinearLayout;


}
