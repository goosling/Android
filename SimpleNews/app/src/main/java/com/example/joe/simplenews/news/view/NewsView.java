package com.example.joe.simplenews.news.view;

import com.example.joe.simplenews.beans.NewsBean;

import java.util.List;

/**
 * Created by JOE on 2016/6/5.
 */
public interface NewsView {

    void showProgress();

    void addNews(List<NewsBean> newsList);

    void hideProgress();

    void showLoadFailMsg();

}
