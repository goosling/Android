package com.example.joe.simplenews.beans;

import java.io.Serializable;

/**
 * Created by JOE on 2016/6/3.
 */
public class ImageBean implements Serializable{

    private static final long serialVersionUID = 1l;

    private String title;

    private String thumbUrl;

    private String sourceUrl;

    private int height;

    private int width;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
