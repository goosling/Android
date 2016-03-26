package PrivateView;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.wechatsample.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import db.DiskLruCache;

/**
 * Created by JOE on 2016/3/23.
 */
public class PhotoWallAdapter extends ArrayAdapter<String> {

    //记录所有下载或正在等待下载的人物
    private Set<BitmapWorkerTask> taskCollection;

    private LruCache<String, Bitmap> mMemoryCache;

    private DiskLruCache mDiskLrucache;

    private GridView mPhotoWall;

    //记录每个子项的高度
    private int mItemHeight;

    public PhotoWallAdapter(Context context, int resource, String[] objects,
                            GridView photoWall) {
        super(context, resource, objects);
        mPhotoWall = photoWall;
        taskCollection = new HashSet<>();

        //获取程序最大可用内存
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        try {
            //获取图片储存路径
            File cacheDir = getDiskCacheDir(context, "thumb");
            if(!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            mDiskLrucache = DiskLruCache.open(cacheDir, getAppVersion(context),
                    1, 10*1024*1024);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String url = getItem(position);
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.photo_layout, null);
        }else {
            view = convertView;
        }

        final ImageView imageView = (ImageView)view.findViewById(R.id.photo);
        if(imageView.getLayoutParams().height != mItemHeight) {
            imageView.getLayoutParams().height = mItemHeight;
        }
        //给imageview设置一个tag，保证异步加载图片不会乱序
        imageView.setTag(url);
        imageView.setImageResource(R.mipmap.ic_launcher);
        loadBitmaps(imageView, url);
        return view;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param bitmap
     *            LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
     * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
     */
    public void loadBitmaps(ImageView imageView, String imageUrl) {
        try{
            Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
            if(bitmap == null) {
                BitmapWorkerTask task = new BitmapWorkerTask();
                taskCollection.add(task);
                task.execute(imageUrl);
            }else {
                if(imageView != null && bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    //取消所有任务
    public void cancelAllTasks() {
        if(taskCollection != null) {
            for(BitmapWorkerTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }




    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||
                !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public int getAppVersion(Context context) {
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionCode;
        }catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void setItemHeight(int height) {
        if(height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try{
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            cacheKey = bytesToHexString(md.digest());
        }catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //将缓存记录储存到journal中
    public void flushCache() {
        if(mDiskLrucache != null) {
            try{
                mDiskLrucache.flush();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //异步下载任务
    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        //图片的url地址
        private String imageUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //根据tag找到相应的bitmap，显示出来
            ImageView imageView = (ImageView)mPhotoWall.findViewWithTag(imageUrl);
            if(imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            FileDescriptor descriptor = null;
            FileInputStream fis = null;
            DiskLruCache.Snapshot snapshot = null;
            try{
                final String key = hashKeyForDisk(imageUrl);
                snapshot = mDiskLrucache.get(key);
                if(snapshot == null) {
                    DiskLruCache.Editor editor = mDiskLrucache.edit(key);
                    if(editor != null) {
                        OutputStream os = editor.newOutputStream(0);
                        if(downloadUrlToStream(imageUrl, os)) {
                            editor.commit();
                        }else {
                            editor.abort();
                        }
                    }

                    //缓存被写入之后，再次查找key对应的缓存
                    snapshot = mDiskLrucache.get(key);
                }
                if(snapshot != null) {
                    fis = (FileInputStream)snapshot.getInputStream(0);
                    descriptor = fis.getFD();
                }
                //将缓存数据解析成bitmap对象
                Bitmap bitmap = null;
                if(descriptor != null) {
                    bitmap = BitmapFactory.decodeFileDescriptor(descriptor);
                }
                if(bitmap!= null) {
                    addBitmapToMemoryCache(params[0], bitmap);
                }
                return bitmap;
            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(descriptor == null && fis != null) {
                    try{
                        fis.close();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        /**
         * 建立HTTP请求，并获取Bitmap对象。
         *
         * @param urlString
         *            图片的URL地址
         * @return 解析后的Bitmap对象
         */
        private boolean downloadUrlToStream(String urlString, OutputStream os) {
            HttpURLConnection connection = null;
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            try{
                final URL url = new URL(urlString);
                connection = (HttpURLConnection)url.openConnection();
                in = new BufferedInputStream(connection.getInputStream(), 8*1024);
                out = new BufferedOutputStream(os, 8*1024);
                int b;
                while((b=in.read()) != -1) {
                    out.write(b);
                }
                return true;
            }catch (final IOException e) {
                e.printStackTrace();
            }finally {
                if(connection!= null) {
                    connection.disconnect();
                }
                try{
                    if(out != null) {
                        out.close();
                    }
                    if(in != null) {
                        in.close();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

}
