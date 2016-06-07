package com.example.joe.simplenews.news.widget;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.joe.simplenews.R;
import com.example.joe.simplenews.beans.NewsBean;
import com.example.joe.simplenews.news.presenter.NewsDetailPresenter;
import com.example.joe.simplenews.news.presenter.NewsDetailPresenterImpl;
import com.example.joe.simplenews.news.view.NewsDetailView;
import com.example.joe.simplenews.utils.ImageLoaderUtils;
import com.example.joe.simplenews.utils.ToolsUtil;
import com.example.joe.swipeback.SwipeBackLayout;
import com.example.joe.swipeback.app.SwipeBackActivity;

import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * Created by JOE on 2016/6/5.
 *
 * 新闻详情界面
 */
public class NewsDetailActivity extends SwipeBackActivity implements NewsDetailView{

    private NewsBean mNews;
    private HtmlTextView mTvNewsContent;
    private NewsDetailPresenter mNewsDetailPresenter;
    private ProgressBar mProgressBar;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar)findViewById(R.id.progress);
        mTvNewsContent = (HtmlTextView)findViewById(R.id.htNewsContent);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeSize(ToolsUtil.getWidthInPx(this));
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        mNews = (NewsBean)getIntent().getSerializableExtra("news");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mNews.getTitle());

        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage),
                mNews.getImgSrc());

        mNewsDetailPresenter = new NewsDetailPresenterImpl(getApplication(), this);
        mNewsDetailPresenter.loadNewsDetail(mNews.getDocId());
    }

    @Override
    public void showNewsDetailContent(String newsDetailContent) {
        mTvNewsContent.setHtmlFromString(newsDetailContent, new HtmlTextView.LocalImageGetter());
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
