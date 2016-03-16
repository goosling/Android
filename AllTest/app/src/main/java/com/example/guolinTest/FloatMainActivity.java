package com.example.guolinTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.joe.alltest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOE on 2016/3/15.
 */
public class FloatMainActivity extends Activity {


    @Bind(R.id.start_float_window)
    Button startFloatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_float_window)
    public void onClick() {
        Intent intent = new Intent(FloatMainActivity.this, FloatWindowService.class);
        startService(intent);
        finish();
    }
}
