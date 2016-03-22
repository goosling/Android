package com.bidirslidinglayout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.io.File;

import PrivateView.ZoomImageView;
import db.Image;

/**
 * Created by JOE on 2016/3/20.
 */
public class ImageDetailsActivity extends Activity implements ViewPager.OnPageChangeListener{

    private ZoomImageView zoomImageView;

    private ViewPager viewPager;

    //显示当前的页数
    private TextView pageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_details);

        int imagePosition = getIntent().getIntExtra("image_position", 0);
        pageText = (TextView)findViewById(R.id.page_text);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imagePosition);
        viewPager.setOnPageChangeListener(this);

        //设定当前页数和总页数
        pageText.setText((imagePosition + 1) + "/" + Image.imageUrls.length);

        /**zoomImageView = (ZoomImageView)findViewById(R.id.zoom_image_view);
        String imagePath = getIntent().getStringExtra("image_path");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        zoomImageView.setImageBitmap(bitmap);*/
    }

    /**
     * ViewPager适配器*/
    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return Image.imageUrls.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String imagePath = getImagePath(Image.imageUrls[position]);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if(bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.bmp1);
            }

            View view = LayoutInflater.from(ImageDetailsActivity.this).inflate(R.layout.zoom_image_layout, null);
            ZoomImageView zoomImageView = (ZoomImageView)view.findViewById(R.id.zoom_image_view);
            zoomImageView.setImageBitmap(bitmap);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View)object;
            container.removeView(view);
        }
    }

    /**
     * 获取图片的本地存储路径。
     *
     * @param imageUrl
     *            图片的URL地址。
     * @return 图片的本地存储路径。
     */
    private String getImagePath(String imageUrl) {
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        String imageName = imageUrl.substring(lastSlashIndex + 1);
        String imageDir = Environment.getExternalStorageDirectory().getPath()+"PhotoWallFalls";
        File file = new File(imageDir);
        if(!file.exists()) {
            file.mkdir();
        }
        String imagePath = imageDir + imageName;
        return imagePath;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int currentPage) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        pageText.setText((currentPage + 1) + "/" + Image.imageUrls.length);
    }
}
