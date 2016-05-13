package com.wechatsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import PrivateView.ImageAdapter;
import db.Images;

/**
 * Created by JOE on 2016/3/24.
 */
public class ListViewActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list_view);
        listView = (ListView)findViewById(R.id.list_view);
        ImageAdapter adapter = new ImageAdapter(this, 0, Images.imageThumbUrls);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean hasWindowFocus() {
        return super.hasWindowFocus();
    }
}
