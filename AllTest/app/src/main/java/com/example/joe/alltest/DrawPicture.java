package com.example.joe.alltest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JOE on 2016/3/4.
 */
public class DrawPicture extends Activity {

    @Bind(R.id.resume)
    Button resume;
    @Bind(R.id.save)
    Button save;
    @Bind(R.id.iv_canvas)
    ImageView ivCanvas;
    private Button btn_save, btn_resume;
    private ImageView iv_canvas;
    private final static String TAG = "drawPicture";
    private Bitmap baseBitmap;
    private Canvas canvas;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas_layout);
        ButterKnife.bind(this);

        //初始化paint
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);

        iv_canvas = (ImageView) findViewById(R.id.iv_canvas);
        btn_resume = (Button) findViewById(R.id.resume);
        btn_save = (Button) findViewById(R.id.save);

        btn_resume.setOnClickListener(click);
        btn_save.setOnClickListener(click);
        iv_canvas.setOnTouchListener(touch);
    }

    private View.OnTouchListener touch = new View.OnTouchListener() {
        //定义开始触摸的坐标
        float startX, startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                //用户按下动作
                case MotionEvent.ACTION_DOWN:
                    if (baseBitmap == null) {
                        baseBitmap = Bitmap.createBitmap(iv_canvas.getWidth(),
                                iv_canvas.getHeight(), Bitmap.Config.ARGB_8888);
                        canvas = new Canvas(baseBitmap);
                        canvas.drawColor(Color.WHITE);
                    }
                    //记录开始点坐标
                    startX = event.getX();
                    startY = event.getY();
                    break;
                //用户手指在屏幕上移动
                case MotionEvent.ACTION_MOVE:
                    //记录移动位置点坐标
                    float endX = event.getX();
                    float endY = event.getY();

                    canvas.drawLine(startX, startY, endX, endY, paint);

                    //重新设置开始点
                    startX = event.getX();
                    startY = event.getY();
                    //将image展示到ImageView当中
                    iv_canvas.setImageBitmap(baseBitmap);
                    break;

                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;

            }
            return true;
        }
    };

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save:
                    saveBitmap();
                    break;
                case R.id.resume:
                    resumeCanvas();
                    break;
                default:
                    break;
            }
        }
    };

    //保存图片到sd卡上
    protected void saveBitmap() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            baseBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(DrawPicture.this, "保存图片成功", Toast.LENGTH_SHORT).show();
            ;

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
            intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //清除画板
    protected void resumeCanvas() {
        //手动清除画板的图，重构一个画板
        if (baseBitmap != null) {
            baseBitmap = Bitmap.createBitmap(iv_canvas.getWidth(),
                    iv_canvas.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(baseBitmap);
            canvas.drawColor(Color.WHITE);
            iv_canvas.setImageBitmap(baseBitmap);
            Toast.makeText(DrawPicture.this, "清除画板成功，可以重新画图", Toast.LENGTH_SHORT).show();
            ;
        }
    }


}
