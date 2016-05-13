package com.example.joe.alltest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by JOE on 2016/3/1.
 */
public class AsyncTask extends Activity {

    private Button button1;
    private ImageView imageView;
    private static String PATH = "http://ww4.sinaimg.cn/bmiddle/786013a5jw1e7akotp4bcj20c80i3aao.jpg";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instore_layout);

        button1 = (Button)findViewById(R.id.append);
        //imageView = (ImageView)findViewById(R.id.);

        //声明一个等待框以提示用户等待
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示信息");
        dialog.setMessage("正在下载，请等待。。。");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    /*public class MyTask extends AsyncTask<String, Void, Bitmap> {

    }*/
}
