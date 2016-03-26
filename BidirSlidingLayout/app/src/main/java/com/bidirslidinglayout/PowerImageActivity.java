package com.bidirslidinglayout;

import android.app.Activity;
import android.os.Bundle;

import PrivateView.PowerImageView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JOE on 2016/3/21.
 */
public class PowerImageActivity extends Activity {


    @Bind(R.id.image_view)
    PowerImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_image_view);
        ButterKnife.bind(this);
    }
}
