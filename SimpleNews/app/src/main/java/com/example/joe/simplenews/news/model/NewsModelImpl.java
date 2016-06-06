package com.example.joe.simplenews.news.model;

import com.example.joe.simplenews.beans.NewsBean;
import com.example.joe.simplenews.beans.NewsDetailBean;
import com.example.joe.simplenews.commons.Urls;
import com.example.joe.simplenews.news.NewsJsonUtils;
import com.example.joe.simplenews.news.widget.NewsFragment;
import com.example.joe.simplenews.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by JOE on 2016/6/5.
 *
 * 新闻业务处理类
 */
public class NewsModelImpl implements NewsModel{

    public NewsModelImpl() {
        super();
    }

    /**
     * 加载新闻列表
     * @param url
     * @param listener
     */

    @Override
    public void loadNews(String url, final int type, final NewsModelImpl.OnLoadNewsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<NewsBean> newsList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
                listener.onSuccess(newsList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    @Override
    public void loadNewsDetail(final String docId, final NewsModelImpl.OnLoadNewsDetailListener listener) {
        String url = getDetailUrl(docId);
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                NewsDetailBean detailBean = NewsJsonUtils.readJsonNewsDetailBean(response, docId);
                listener.onSuccess(detailBean);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news detail info failure", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    /**
     * 获取ID
     * @param type
     * @return
     */
    private String getID(int type) {
        String id;
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                id = Urls.NBA_ID;
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                id = Urls.CAR_ID;
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }

    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Urls.NEWS_DETAIL);
        sb.append(docId).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }

    public interface OnLoadNewsListListener {
        void onSuccess(List<NewsBean> list);
        void onFailure(String msg, Exception e);
    }

    public interface OnLoadNewsDetailListener {
        void onSuccess(NewsDetailBean newsDetailBean);

        void onFailure(String msg, Exception e);
    }

}
