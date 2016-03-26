package com.example.joe.alltest;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/2/27.
 */
public class ImageSwitcherActivity extends Activity {

    private Button nextPic, lastPic;
    private ImageSwitcher imageSwitcher;
    private int index = 0;
    private List<Drawable> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        putData();
        imageSwitcher = (ImageSwitcher)findViewById(R.id.iamgeSwitcher1);
        nextPic = (Button)findViewById(R.id.next_pic);
        lastPic = (Button)findViewById(R.id.last_pic);
        nextPic.setOnClickListener(myClick);
        lastPic.setOnClickListener(myClick);

        //通过代码设定图片效果
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(ImageSwitcherActivity.this,
                android.R.anim.slide_in_left));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(ImageSwitcherActivity.this,
                android.R.anim.slide_out_right));
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(ImageSwitcherActivity.this);
            }
        });
        imageSwitcher.setImageDrawable(list.get(0));

    }

    

    private View.OnClickListener myClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.next_pic:
                    index--;
                    if(index<0) {
                        index = list.size()-1;
                    }
                    imageSwitcher.setImageDrawable(list.get(index));
                    break;
                case R.id.last_pic:
                    index++;
                    if(index>=list.size()) {
                        index = 0;
                    }
                    imageSwitcher.setImageDrawable(list.get(index));
                    break;
            }
        }
    };

    //初始化图片资源
    private void putData() {
        list = new ArrayList<Drawable>();
        list.add(getResources().getDrawable(R.mipmap.bmp1));
        list.add(getResources().getDrawable(R.mipmap.bmp2));
        list.add(getResources().getDrawable(R.mipmap.bmp3));
        list.add(getResources().getDrawable(R.mipmap.bmp4));
        list.add(getResources().getDrawable(R.mipmap.bmp5));

    }
}
