package com.example.joe.simplenews.news.presenter;

import android.content.Context;

import com.example.joe.simplenews.beans.NewsDetailBean;
import com.example.joe.simplenews.news.model.NewsModel;
import com.example.joe.simplenews.news.model.NewsModelImpl;
import com.example.joe.simplenews.news.view.NewsDetailView;

/**
 * Created by JOE on 2016/6/6.
 */
public class NewsDetailPresenterImpl implements NewsDetailPresenter, NewsModelImpl.OnLoadNewsDetailListener {

    private Context mContent;
    private NewsDetailView mNewsDetailView;
    private NewsModel mNewsModel;

    public NewsDetailPresenterImpl(Context mContent, NewsDetailView mNewsDetailView) {
        this.mContent = mContent;
        this.mNewsDetailView = mNewsDetailView;
        mNewsModel = new NewsModelImpl();
    }

    @Override
    public void loadNewsDetail(final String docId) {
        mNewsDetailView.showProgress();
        mNewsModel.loadNewsDetail(docId, this);
    }


    @Override
    public void onSuccess(NewsDetailBean newsDetailBean) {
        if(newsDetailBean != null) {
            mNewsDetailView.showNewsDetailContent(newsDetailBean.getBody());
        }
        mNewsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsDetailView.hideProgress();
    }
}