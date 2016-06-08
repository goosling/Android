package com.example.joe.simplenews.main.presenter;

import com.example.joe.simplenews.R;
import com.example.joe.simplenews.main.view.MainView;

/**
 * Created by JOE on 2016/6/8.
 */
public class MainPresenterImpl implements MainPresenter {

    private MainView mMainView;

    public MainPresenterImpl(MainView mMainView) {
        this.mMainView = mMainView;
    }

    @Override
    public void switchNavigation(int id) {
        switch(id) {
            case R.id.navigation_item_news:
                mMainView.switch2News();
                break;

        }
    }
}
