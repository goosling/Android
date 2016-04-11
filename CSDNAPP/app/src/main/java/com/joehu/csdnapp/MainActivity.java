package com.joehu.csdnapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;

import MyView.TabAdapter;

public class MainActivity extends FragmentActivity {

    private TabPageIndicator mIndictor;

    private ViewPager mViewPager;

    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIndictor = (TabPageIndicator)findViewById(R.id.id_indicator);
        mViewPager = (ViewPager)findViewById(R.id.id_pager);
        mAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndictor.setViewPager(mViewPager, 0);


    }

    //自动释放资源


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch(level) {
            case TRIM_MEMORY_UI_HIDDEN:
                //进行资源释放操作
                
                break;
        }
    }
}

