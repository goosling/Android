package com.example.joe.alltest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by JOE on 2016/3/3.
 */
public class LoadPicture extends Activity {

    private Button loadPicture;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instore_layout);

        iv = (ImageView)findViewById(R.id.image_sample);
        loadPicture = (Button)findViewById(R.id.append);
        loadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                原来加载图片方法，但是如果图片过大，则会出现内存溢出问题
                Bitmap bitmap = BitmapFactory.decodeFile("bitmap.jpeg");
                iv.setImageBitmap(bitmap);*/

                BitmapFactory.Options options = new BitmapFactory.Options();
                //不读取像素组到内存中，只读取图片信息
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile("bitmap.jpeg");
                //从options中获取图片分辨率
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;

                //获取屏幕分辨率
                WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
                int windowHeight = wm.getDefaultDisplay().getHeight();
                int windowWdith = wm.getDefaultDisplay().getWidth();

                //计算采样率
                int scaleX = imageHeight/windowHeight;
                int scaleY = imageWidth/windowWdith;
                int scale = 1;
                if(scaleX>scaleY && scaleY>=1) {
                    scale = scaleY;
                }else if(scaleX<scaleY && scaleX>=1) {
                    scale = scaleX;
                }
                //读取图片像素到内存中
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeFile("bitmap.jpeg");
                iv.setImageBitmap(bitmap);
            }
        });
    }
}
