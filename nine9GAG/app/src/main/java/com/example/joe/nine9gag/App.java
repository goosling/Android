package com.example.joe.nine9gag;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by joe on 2016/4/19.
 */
public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        initImageLoader(getApplicationContext());
    }

    public static Context getsContext() {
        return sContext;
    }

    //初始化ImageLoader
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)//线程池中线程的个数
                .denyCacheImageMultipleSizesInMemory()//禁止缓存多张图片
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .discCacheSize(10 * 1024 * 1024)//内存缓存的大小
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//缓存文件名的保存方式
                .tasksProcessingOrder(QueueProcessingType.FIFO)//工作队列
                .build();
        ImageLoader.getInstance().init(configuration);
    }

}
