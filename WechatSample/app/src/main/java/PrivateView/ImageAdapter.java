package PrivateView;

import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;
import android.widget.ArrayAdapter;

/**
 * Created by JOE on 2016/3/24.
 */
public class ImageAdapter extends ArrayAdapter<String> {
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, BitmapDrawable> mMemoryCache;

    
}
