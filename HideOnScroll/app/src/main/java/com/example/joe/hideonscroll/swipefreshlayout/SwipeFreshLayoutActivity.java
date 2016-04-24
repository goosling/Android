package com.example.joe.hideonscroll.swipefreshlayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by joe on 2016/4/23.
 */
public class SwipeFreshLayoutActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeLayout;

    private ListView mListView;

    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_fresh_layout);

        mListView = (ListView)findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData()));

        mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        mSwipeLayout.setOnClickListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private ArrayList<String> getData() {
        list.add("Hello");
        list.add("This is goosling");
        list.add("An Android Developer");
        list.add("Love Open Source");
        list.add("My GitHub: goosling");
        list.add("weibo: xiaoliu920");
        return list;
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
