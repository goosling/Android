package com.bidirslidinglayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import Utils.Rotate3dAnimation;
import db.Picture;
import db.PictureAdapter;

/**
 * Created by JOE on 2016/3/21.
 */
public class RotatePictureActivity extends Activity {

    //根布局
    private RelativeLayout relativeLayout;

    //用来展示图片列表的listView
    private ListView listView;

    private ImageView picture;

    private PictureAdapter adapter;


    private List<Picture> picList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.picture_rotate);
        //对图片数据进行初始化操作
        initPics();
        relativeLayout = (RelativeLayout)findViewById(R.id.layout);
        listView = (ListView)findViewById(R.id.pic_list_view);
        picture = (ImageView)findViewById(R.id.picture);
        adapter = new PictureAdapter(this, 0, picList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击某项时，将imageView中图片设置为相应资源
                picture.setImageResource(picList.get(position).getResource());
                //获得布局 的中心点作为旋转点
                float centerX = view.getWidth() / 2f;
                float centerY = view.getHeight() / 2f;
                //构建3d旋转动画对象，旋转角度从0到90， 使得listview从不可见到可见
                final Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90, centerX, centerY,
                        310.0f, true);
                //动画持续时间500ms
                rotation.setDuration(500);
                //动画完成后保持完成的状态
                rotation.setFillAfter(true);
                rotation.setInterpolator(new AccelerateInterpolator());
                //设置动画监听器
                rotation.setAnimationListener(new TurnToImageView());
                relativeLayout.setAnimation(rotation);
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float centerX = v.getWidth() / 2f;
                float centerY = v.getHeight() / 2f;
                // 构建3D旋转动画对象，旋转角度为360到270度，这使得ImageView将会从可见变为不可见，并且旋转的方向是相反的
                final Rotate3dAnimation rotation = new Rotate3dAnimation(360, 270, centerX,
                        centerY, 310.0f, true);
                // 动画持续时间500毫秒
                rotation.setDuration(500);
                // 动画完成后保持完成的状态
                rotation.setFillAfter(true);
                rotation.setInterpolator(new AccelerateInterpolator());
                // 设置动画的监听器
                rotation.setAnimationListener(new TurnToListView());
                relativeLayout.startAnimation(rotation);
            }
        });
    }

    private void initPics() {
        Picture bmp1 = new Picture("bmp1", R.drawable.bmp1);
        picList.add(bmp1);
        Picture bmp2 = new Picture("bmp2", R.drawable.bmp2);
        picList.add(bmp2);
        Picture bmp3 = new Picture("bmp3", R.drawable.bmp3);
        picList.add(bmp3);
        Picture bmp4 = new Picture("bmp4", R.drawable.bmp4);
        picList.add(bmp4);
        Picture bmp5 = new Picture("bmp5", R.drawable.bmp5);
        picList.add(bmp5);
    }

    /**
     * 注册在ListView点击动画中的动画监听器，用于完成ListView的后续动画。
     *
     */
    class TurnToImageView implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 当ListView的动画完成后，还需要再启动ImageView的动画，让ImageView从不可见变为可见
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            float centerX = relativeLayout.getWidth() / 2f;
            float centerY = relativeLayout.getHeight() / 2f;
            //将listview设置为隐藏
            listView.setVisibility(View.GONE);
            //将imageView显示
            picture.setVisibility(View.VISIBLE);
            picture.requestFocus();
            // 构建3D旋转动画对象，旋转角度为270到360度，这使得ImageView将会从不可见变为可见
            final Rotate3dAnimation rotation = new Rotate3dAnimation(270, 360, centerX, centerY,
                    310.0f, false);
            // 动画持续时间500毫秒
            rotation.setDuration(500);
            // 动画完成后保持完成的状态
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            relativeLayout.startAnimation(rotation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * 注册在ImageView点击动画中的动画监听器，用于完成ImageView的后续动画。
     *
     * @author guolin
     */
    class TurnToListView implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            float centerX = relativeLayout.getWidth() / 2f;
            float centerY = relativeLayout.getHeight() / 2f;
            picture.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.requestFocus();
            // 构建3D旋转动画对象，旋转角度为90到0度，这使得ListView将会从不可见变为可见，从而回到原点
            final Rotate3dAnimation rotation = new Rotate3dAnimation(90, 0, centerX, centerY,
                    310.0f, false);
            // 动画持续时间500毫秒
            rotation.setDuration(500);
            // 动画完成后保持完成的状态
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            relativeLayout.startAnimation(rotation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
