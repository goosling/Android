package com.example.joe.zhihudaily;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.joe.zhihudaily.bean.DailyNews;
import com.example.joe.zhihudaily.db.DailyNewsDataSource;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by JOE on 2016/6/12.
 */
public class ZhihuDailyApplication extends Application {

    private static DailyNews mDailyNews;
    private static DailyNewsDataSource mSource;
    private static ZhihuDailyApplication applicationContext;

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public static ZhihuDailyApplication getInstance() {
        return applicationContext;
    }

    public static DailyNewsDataSource getDataSource() {
        return mSource;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;

        initImageLoader(getApplicationContext());
        mSource = new DailyNewsDataSource(getApplicationContext());
        try{
            mSource.open();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

}
