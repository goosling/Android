package com.example.joe.hideonscroll.activity.partone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import com.example.joe.hideonscroll.R;
import com.example.joe.hideonscroll.adapter.partone.RecyclerAdapter;

/**
 * Created by joe on 2016/4/22.
 */
public class PartOneActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ImageButton mFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_one);

        initToolBar();
        mFabButton = (ImageButton)findViewById(R.id.fabButton);
        initRecyclerView();
    }

    private void initToolBar() {
        mToolbar = (Toolbar)findViewById(R.id.toolBar1);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter
    }
}
