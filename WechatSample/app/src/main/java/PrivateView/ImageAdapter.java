package PrivateView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.wechatsample.R;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JOE on 2016/3/24.
 */
public class ImageAdapter extends ArrayAdapter<String> {
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, BitmapDrawable> mMemoryCache;

    public ImageAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        //获取应用最大可用内存
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.photo_list_view_item, null);
        }else {
            view = convertView;
        }
        ImageView image = (ImageView)view.findViewById(R.id.image);
        BitmapDrawable drawable = getBitmapFromMemoryCache(url);
        if(drawable == null) {
            image.setImageDrawable(drawable);
        }else {
            BitmapWorkerTask task = new BitmapWorkerTask(image);
            task.execute(url);
        }
        return view;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param drawable
     *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
     */
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if(getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, drawable);
        }
    }


    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    //异步下载图片任务
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        private ImageView mImageView;

        public BitmapWorkerTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            if(mImageView != null && drawable != null) {
                mImageView.setImageDrawable(drawable);
            }
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            String imageUrl = params[0];
            Bitmap bitmap = downloadBitmap(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }


        private Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            try{
                URL url = new URL(imageUrl);
                connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(5 * 1000);
                connection.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
            return bitmap;
        }
    }
}
