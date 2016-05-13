package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by joe on 2016/4/7.
 */
public class ScrollerActivity extends Activity {

    private LinearLayout layout;

    private Button scrollToBtn, scrollByBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroller_layout);
        layout = (LinearLayout)findViewById(R.id.layout);
        scrollToBtn = (Button)findViewById(R.id.scroll_to_btn);
        scrollByBtn = (Button)findViewById(R.id.scroll_by_btn);
        scrollToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.scrollTo(-60, -100);
            }
        });
        scrollByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.scrollBy(-60, -100);
            }
        });
    }
}
