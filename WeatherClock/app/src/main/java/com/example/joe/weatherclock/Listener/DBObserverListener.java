package com.example.joe.weatherclock.Listener;

/**
 * Created by JOE on 2016/6/22.
 * DB数据观察者接口
 */
public interface DBObserverListener {
    //db数据有更新
    void onDBDataChanged();
}
