package com.joehu.csdnapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_3d_layout);

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

