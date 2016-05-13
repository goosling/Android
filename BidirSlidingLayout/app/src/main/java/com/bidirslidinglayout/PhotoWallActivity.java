package com.bidirslidinglayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by JOE on 2016/3/19.
 */
public class PhotoWallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo_wall_layout);
    }
}
