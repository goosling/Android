package com.example.joe.simplenews.images.presenter;

import android.widget.ImageView;

import com.example.joe.simplenews.beans.ImageBean;
import com.example.joe.simplenews.images.model.ImageModel;
import com.example.joe.simplenews.images.model.ImageModelImpl;

import java.util.List;

/**
 * Created by JOE on 2016/6/4.
 */
public class ImagePresenterImpl implements ImagePresenter, ImageModelImpl.OnLoadImageListListener {

    private ImageModel mImageModel;
    private ImageView mImageView;

    public ImagePresenterImpl(ImageView mImageView) {
        this.mImageView = mImageView;
        this.mImageModel = new ImageModelImpl();
    }

    @Override
    public void loadImageList() {
        //mImageView.showProgress();
        mImageModel.loadImageList(this);
    }

    @Override
    public void onSuccess(List<ImageBean> list) {
        mImageView.addImages(list);
        mImageView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mImageView.hideProgress();
        mImageView.showLoadFailMsg();
    }
}
