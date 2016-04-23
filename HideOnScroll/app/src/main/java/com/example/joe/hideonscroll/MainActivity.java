package com.example.joe.hideonscroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 1.初始化Toolbar

     2.获取mFabButton的引用（mFabButton是FAB的对象哦，也就是屏幕右下方的小按钮）

     3.初始化RecyclerView*/


    private Button partOneButton, partTwoButton, partThreeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initButtons();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
    }

    private void initButtons() {
        partOneButton = (Button)findViewById(R.id.partOneButton);
        partTwoButton = (Button)findViewById(R.id.partTwoButton);
        partThreeButton = (Button)findViewById(R.id.partThreeButton);

        partOneButton.setOnClickListener(this);
        partTwoButton.setOnClickListener(this);
        partThreeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.partOneButton:
                startActivity(PartOneActivity.class);
                break;
            case R.id.partTwoButton:
                startActivity(PartTwoActivity.class);
                break;
            default:
                startActivity(PartThreeActivity.class);
                break;
        }
    }

    private void startActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
