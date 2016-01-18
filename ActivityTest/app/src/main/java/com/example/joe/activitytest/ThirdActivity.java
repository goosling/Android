package com.example.joe.activitytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by joe on 2016/1/17.
 */
public class ThirdActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.third_layout);
    }
}
