package com.example.joe.simplenews.news.model;

/**
 * Created by JOE on 2016/6/5.
 */
public interface NewsModel {

    void loadNews(String url, int type, NewsModelImpl.OnLoadNewsListListener listener);

    void loadNewsDetail(String docId, NewsModelImpl.OnLoadNewsDetailListener listener);
}
