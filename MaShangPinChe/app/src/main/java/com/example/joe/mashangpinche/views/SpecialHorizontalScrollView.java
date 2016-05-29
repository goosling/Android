package com.example.joe.mashangpinche.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.example.joe.mashangpinche.activities.MainActivity;

import java.lang.reflect.Field;

/**
 * Created by JOE on 2016/5/29.
 */
public class SpecialHorizontalScrollView extends ScrollView {

    public static final String MSG_TO_MAINACTIVITY = "Current_Ta_Index";

    private boolean currentlyTouching = false;
    private boolean currentScrolling = false;

    private int lastTouchedX = 0;

    private int currentX = 0;

    private int itemWidth = 64;

    private Field mScrollerField;

    private GestureDetector mGestureDetector;

    private static final int SWIPE_MIN_DISTANCE = 5;

    private static final int SWIPE_MIN_VELOCITY = 300;

    private MainActivity mainActivity;

    SpecialScroller mScroller = null;

    private int currentTaIndex = 0;

    public SpecialHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mainActivity = (MainActivity)context;

        initScroller();
    }

    public SpecialHorizontalScrollView(Context context) {
        super(context);
    }

    public SpecialHorizontalScrollView(Context context, AttributeSet attrs,
                                       int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setItemWidth(int itemWidth) {

        this.itemWidth = itemWidth;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(Math.abs(l - oldl) > 1) {
            currentlyTouching = true;
        }else {
            currentlyTouching = false;
            if(!currentlyTouching) {
                adjustX(l);
                mScroller.forceFinished();
            }
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentlyTouching = true;
                lastTouchedX = (int) ev.getX();
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public  int adjustX(int currentX) {

    }
}
