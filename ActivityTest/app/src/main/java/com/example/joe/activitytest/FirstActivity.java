package com.example.joe.activitytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by joe on 2016/1/17.
 */
public class FirstActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //隐藏任务栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.first_layout);
        Button button1 = (Button)findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this, "You clicked button1",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
