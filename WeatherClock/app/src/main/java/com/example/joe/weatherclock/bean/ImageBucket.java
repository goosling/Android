package com.example.joe.weatherclock.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/6/22.
 *
 * 相册实例
 */
public class ImageBucket implements Parcelable {

    /**
     * 相册照片数量
     */
    public int count;

    /**
     * 相册名
     */
    public String bucketName;

    /**
     * 相册照片List
     */
    public ArrayList<ImageItem> bucketList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeString(bucketName);
        dest.writeTypedList(bucketList);
    }

    public ImageBucket() {}

    protected ImageBucket(Parcel in) {
        this.count = in.readInt();
        this.bucketName = in.readString();
        this.bucketList = in.createTypedArrayList(ImageItem.CREATOR);
    }

    public static final Parcelable.Creator<ImageBucket> CREATOR = new Creator<ImageBucket>() {
        @Override
        public ImageBucket createFromParcel(Parcel source) {
            return new ImageBucket(source);
        }

        @Override
        public ImageBucket[] newArray(int size) {
            return new ImageBucket[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public List<ImageItem> getBucketList() {
        return bucketList;
    }

    public void setBucketList(ArrayList<ImageItem> bucketList) {
        this.bucketList = bucketList;
    }
}
