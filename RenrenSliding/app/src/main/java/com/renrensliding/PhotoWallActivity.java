package com.renrensliding;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import db.Image;

/**
 * Created by JOE on 2016/3/18.
 */
public class PhotoWallActivity extends Activity {

    private GridView mPhotoWall;

    private PhotoWallAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_layout);
        mPhotoWall = (GridView)findViewById(R.id.photo_wall);
        adapter = new PhotoWallAdapter(this, 0, Image.imageThumbUrls, mPhotoWall);
        mPhotoWall.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
    }
}
