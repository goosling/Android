package com.example.joe.weatherclock.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JOE on 2016/6/22.
 */
public class ImageItem implements Parcelable {

    /**
     * 图片id
     */
    private String imageId;

    /**
     * 图片缩略图path
     */
    private String thumbnailPath;

    /**
     * 图片path
     */
    private String imagePath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageId);
        dest.writeString(this.thumbnailPath);
        dest.writeString(this.imagePath);
    }

    private ImageItem(Parcel in) {
        this.imageId = in.readString();
        this.imagePath = in.readString();
        this.thumbnailPath = in.readString();
    }

    public static final Parcelable.Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
