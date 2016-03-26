package com.example.joe.alltest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by JOE on 2016/3/4.
 */
public class GalleryPick extends Activity {

    private Button pick;
    private ImageView iv_pick;
    private final static String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_pick_layout);

        pick = (Button)findViewById(R.id.pick_picture);
        iv_pick = (ImageView)findViewById(R.id.iv_pick);
        pick.setOnClickListener(getImage);
    }

    private View.OnClickListener getImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            //以需要返回值的模式开启一个intent
            startActivityForResult(intent, 0);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == -1) {
            //获取原图uri
            Uri uri = data.getData();
            iv_pick.setImageURI(uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
