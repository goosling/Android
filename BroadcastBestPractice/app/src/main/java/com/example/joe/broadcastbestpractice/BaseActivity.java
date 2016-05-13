package com.example.joe.broadcastbestpractice;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by JOE on 2016/1/26.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
