package com.example.joe.mashangpinche.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.joe.mashangpinche.activities.IwantUApp.MsgHandler;

import com.example.joe.mashangpinche.activities.IwantUApp;

import java.lang.reflect.Field;

/**
 * Created by joe on 2016/4/26.
 */
@SuppressLint("NewApi")
public abstract class MyHorizontalScrollView extends HorizontalScrollView {

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

    SpecialScroller mScroller = null;

    private IwantUApp.MsgHandler mHandler;

    private int msgWhat;

    private int currentIndex = 0;

    public MyHorizontalScrollView(Context context) {
        this(context, null);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLinearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        this.addView(mLinearLayout, mLayoutParams);

        initScroller();
    }

    public MyHorizontalScrollView(Context context, MsgHandler msgHandler, int msgWhat) {
        this(context, null);
        mHandler = msgHandler;
        this.msgWhat = msgWhat;
    }

    /**
     * 增加subview
     *
     * @param view
     */
    public void addSubView(View view, LinearLayout.LayoutParams params) {
        mLinearLayout.addView(view, params);
    }

    public void addSubView(View view, int pos, LinearLayout.LayoutParams params) {
        mLinearLayout.addView(view, pos, params);
    }

    /**
     * 移动视图到一个指定的subview上。 如果直接用scrollBy,没有任何效果，必须用post。
     */
    public void moveToSubView(int index) {
        this.currentIndex = index;
        this.post(new Runnable() {
            @Override
            public void run() {
                scrollTo(currentIndex * itemWidth, 0);
            }
        });
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(Math.abs(l - oldl) <= 1 && !currentlyTouching) {
            adjustX(l);
            mScroller.forceFinished();
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    public int adjustX(int currentX) {
        currentIndex = currentX / itemWidth;
        int item_residual = currentX % itemWidth;
        if(item_residual >= itemWidth / 2) {
            currentIndex += 1;
        }

        int adjustedX = currentIndex * itemWidth;
        scrollTo(adjustedX, 0);

        onCurrentIndexChanged();

        return adjustedX;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentlyTouching = true;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if(action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL) {
            int currentX = getScrollX();
            adjustX(currentX);
            currentlyTouching = false;
        }
        return super.onTouchEvent(ev);
    }

    private void initScroller() {
        Field mScrollerField;
        try{
            mScrollerField = HorizontalScrollView.class.
                    getDeclaredField("mScroller");
            mScrollerField.setAccessible(true);
            mScroller = new SpecialScroller();
            DecelerateInterpolator dip = new DecelerateInterpolator(1);
            mScroller.create(getContext(), dip);
            try{
                mScrollerField.set(this, mScroller.getScroller());
            }catch(Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setSubTextViewColorGray(int index) {
        TextView textView = (TextView)getChildAt(index);
        textView.setTextColor(Color.GRAY);
    }

    public int getItemCount() {
        return mLinearLayout.getChildCount();
    }

    public void setMsgHandler(MsgHandler msgHandler) {
        this.mHandler = msgHandler;
    }

    public int getMsgWhat() {
        return msgWhat;
    }

    public void setMsgWhat(int msgWhat) {
        this.msgWhat = msgWhat;
    }

    //subview的宽度
    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    //删除所有subView
    public void removeAllItemViews() {
        mLinearLayout.removeAllViews();
    }

    public View getSubViewAt(int index) {
        return mLinearLayout.getChildAt(index);
    }

    public abstract void onCurrentIndexChanged();

}
