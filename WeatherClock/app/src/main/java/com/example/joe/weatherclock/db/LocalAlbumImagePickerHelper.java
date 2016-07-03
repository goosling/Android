package com.example.joe.weatherclock.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.example.joe.weatherclock.R;
import com.example.joe.weatherclock.bean.ImageBucket;
import com.example.joe.weatherclock.bean.ImageItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JOE on 2016/7/3.
 *
 * 本地相册照片读取
 */
public class LocalAlbumImagePickerHelper {

    private ContentResolver mContentResolver;

    //相册缩略图
    private HashMap<String, String> mThumbnailList = new HashMap<>();

    /**
     * 相册列表信息
     */
    private HashMap<String, ImageBucket> mImageBucketList = new HashMap<>();

    private static LocalAlbumImagePickerHelper mHelper;

    private Context mContext;

    private LocalAlbumImagePickerHelper(Context context) {
        mContentResolver = context.getContentResolver();
        this.mContext = context;
    }

    public static LocalAlbumImagePickerHelper getInstance(Context context) {
        if(mHelper == null) {
            synchronized (LocalAlbumImagePickerHelper.class) {
                if(mHelper == null) {
                    mHelper = new LocalAlbumImagePickerHelper(context.getApplicationContext());
                }
            }
        }
        return mHelper;
    }

    //取得图片缩略图
    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cursor = mContentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                null, null, null);
        if(cursor != null) {
            getThumbnailColumnData(cursor);
            cursor.close();
        }
    }

    private void getThumbnailColumnData(Cursor cursor) {
        mThumbnailList.clear();
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
                int image_pathColumn = cursor.getColumnIndex(Thumbnails.DATA);

                do{
                    //图片缩略图id
                    int image_id = cursor.getInt(image_idColumn);
                    //图片缩略图path
                    String image_path = cursor.getString(image_pathColumn);

                    mThumbnailList.put(""+image_id, image_path);
                }while(cursor.moveToNext());
            }
        }
    }

    /**
     * 添加最近相册
     */
    private ImageBucket mImageBucketRecent;

    /**
     * 取得相册图片列表
     */
    private void buildImagesBucketList() {
        getThumbnail();
        mImageBucketList.clear();

        //最近相册照片数为50张
        int count = 0;
        mImageBucketRecent = new ImageBucket();
        mImageBucketRecent.bucketList = new ArrayList<>();
        mImageBucketRecent.bucketName = mContext.getString(R.string.recent);

        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA,
                Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME};
        Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
                Media.DATE_MODIFIED + " desc");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
                int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
                int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Media
                        .BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);

                do {
                    // 相片id
                    String _id = cursor.getString(photoIDIndex);
                    // 相片path
                    String path = cursor.getString(photoPathIndex);
                    // 相册名
                    String bucketName = cursor.getString(bucketDisplayNameIndex);
                    bucketName = changeLocalName(bucketName);
                    // 相册id
                    String bucketId = cursor.getString(bucketIdIndex);

                    ImageBucket bucket = mImageBucketList.get(bucketId);
                    // 相同的相册名，不重复添加
                    if (bucket == null) {
                        bucket = new ImageBucket();
                        mImageBucketList.put(bucketId, bucket);
                        bucket.bucketList = new ArrayList<>();
                        bucket.bucketName = bucketName;
                    }
                    bucket.count++;
                    ImageItem imageItem = new ImageItem();
                    imageItem.setImageId(_id);
                    imageItem.setImagePath(path);
                    imageItem.setThumbnailPath(mThumbnailList.get(_id));
                    bucket.bucketList.add(imageItem);

                    // 添加最近相册
                    if (count < 50) {
                        mImageBucketRecent.count++;
                        ImageItem imageItem1 = new ImageItem();
                        imageItem1.setImageId(_id);
                        imageItem1.setImagePath(path);
                        imageItem1.setThumbnailPath(mThumbnailList.get(_id));
                        mImageBucketRecent.bucketList.add(imageItem1);
                        count++;
                    }
                    /////////

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private String changeLocalName(String name) {
        if (name.equalsIgnoreCase("Camera")) {
            name = mContext.getString(R.string.camera);
        } else if (name.equalsIgnoreCase("Screenshots")) {
            name = mContext.getString(R.string.screen_short);
        } else if (name.equalsIgnoreCase("Weixin")) {
            name = mContext.getString(R.string.we_chat);
        } else if (name.equalsIgnoreCase("save")) {
            name = mContext.getString(R.string.save);
        } else if (name.equalsIgnoreCase("download")) {
            name = mContext.getString(R.string.download);
        } else if (name.equalsIgnoreCase("Pictures")) {
            name = mContext.getString(R.string.picture);
        }
        return name;
    }

    /**
     * 取得相册图片列表
     *
     * @return 相册图片列表
     */
    public List<ImageBucket> getImagesBucketList() {
        buildImagesBucketList();
        List<ImageBucket> imageBucketList = new ArrayList<>();
        for (Map.Entry<String, ImageBucket> entry : mImageBucketList.entrySet()) {
            imageBucketList.add(entry.getValue());
        }
        if (imageBucketList.size() > 0) {
            Collections.sort(imageBucketList, new BucketComparator());
            imageBucketList.add(0, mImageBucketRecent);
        }
        return imageBucketList;
    }

    class BucketComparator implements Comparator<ImageBucket> {
        @Override
        public int compare(ImageBucket o1, ImageBucket o2) {
            String name1 = o1.bucketName;
            String name2 = o2.bucketName;
            return name2.compareToIgnoreCase(name1);
        }
    }
}
