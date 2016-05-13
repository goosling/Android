package com.bidirslidinglayout;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by JOE on 2016/3/20.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        setContentView();
        findViews();
        getData();
        showContent();
    }

    public abstract void setContentView();
    public abstract void findViews();
    public abstract void getData();
    public abstract void showContent();
}
