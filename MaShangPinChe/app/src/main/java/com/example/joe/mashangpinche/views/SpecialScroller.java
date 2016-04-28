package com.example.joe.mashangpinche.views;

/**
 * Created by joe on 2016/4/26.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

@TargetApi(9)
public class SpecialScroller {

    private OverScroller mScroller = null;

    @TargetApi(9)
    public void startScroll(int startX, int startY, int dx, int dy,
                            int duration) {
        mScroller.startScroll(startX, startY, dx, dy, duration);
    }

    @TargetApi(9)
    public boolean isFinished() {
        return mScroller.isFinished();
    }

    @TargetApi(9)
    public OverScroller getScroller() {
        return mScroller;
    }

    public void forceFinished() {
        mScroller.forceFinished(true);
    }


    public void create(Context context, Interpolator interpolator) {
        mScroller = new OverScroller(context, interpolator);
        // mScroller.extendDuration(500);
    }

    public void create(Context context) {
        mScroller = new OverScroller(context);
        // mScroller.extendDuration(500);
    }

    public void abortAnimation() {
        if (mScroller != null) {
            mScroller.abortAnimation();
        }
    }

    private interface ScrollerEx {

        void create(Context context, Interpolator interpolator);

        OverScroller getScroller();

        void abortAnimation();

        void startScroll(int startX, int startY, int dx, int dy, int duration);

        boolean isFinished();

        void forceFinished();

    }

    
}
