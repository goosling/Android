package com.example.joe.simplenews.images.view;

import com.example.joe.simplenews.beans.ImageBean;

import java.util.List;

/**
 * Created by JOE on 2016/6/4.
 */
public interface ImageView {
    void addImages(List<ImageBean> list);
    void showProgress();
    void hideProgress();
    void showLoadFailMsg();
}
