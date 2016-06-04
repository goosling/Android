package com.example.joe.simplenews.images.model;

import com.example.joe.simplenews.beans.ImageBean;
import com.example.joe.simplenews.commons.Urls;
import com.example.joe.simplenews.images.ImageJsonUtils;
import com.example.joe.simplenews.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by JOE on 2016/6/4.
 */
public class ImageModelImpl implements ImageModel {
    /**
     * 获取图片列表
     * @param listener
     */
    @Override
    public void loadImageList(final OnLoadImageListListener listener) {
        String url = Urls.IMAGES_URL;
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<ImageBean> beans = ImageJsonUtils.readJsonImageBeans(response);
                listener.onSuccess(beans);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load image list on failure", e);
            }
        };
    }

    public interface OnLoadImageListListener {
        void onSuccess(List<ImageBean> list);
        void onFailure(String msg, Exception e);
    }
}
