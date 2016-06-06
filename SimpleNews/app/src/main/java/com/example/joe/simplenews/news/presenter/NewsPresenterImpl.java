package com.example.joe.simplenews.news.presenter;

import com.example.joe.simplenews.beans.NewsBean;
import com.example.joe.simplenews.commons.Urls;
import com.example.joe.simplenews.news.model.NewsModel;
import com.example.joe.simplenews.news.model.NewsModelImpl;
import com.example.joe.simplenews.news.view.NewsView;
import com.example.joe.simplenews.news.widget.NewsFragment;
import com.example.joe.simplenews.utils.LogUtils;

import java.util.List;

/**
 * Created by JOE on 2016/6/5.
 */
public class NewsPresenterImpl implements NewsPresenter, NewsModelImpl.OnLoadNewsListListener {

    private static final String TAG = "NewsPresenterImpl";

    private NewsView mNewsView;

    private NewsModel mNewsModel;

    public NewsPresenterImpl(NewsView newsView) {
        this.mNewsView = newsView;
        this.mNewsModel = new NewsModelImpl();
    }

    /**
     * 根据类别和页面索引创建url
     * @param type
     * @param pageIndex
     * @return
     */
    private String getUrl(int type, final int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch(type) {
            case NewsFragment.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }

    @Override
    public void loadNews(int type, int page) {
        String url = getUrl(type, page);
        LogUtils.d(TAG, url);
        //只有第一页或者刷新的时候才显示进度条
        if(page == 0) {
            mNewsView.showProgress();
        }

        mNewsModel.loadNews(url, type, this);
    }

    @Override
    public void onSuccess(List<NewsBean> list) {
        mNewsView.hideProgress();
        mNewsView.addNews(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.showLoadFailMsg();
    }
}
