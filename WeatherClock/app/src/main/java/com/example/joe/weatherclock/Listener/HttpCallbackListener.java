package com.example.joe.weatherclock.Listener;

/**
 * Http访问返回回调接口
 * Created by JOE on 2016/6/22.
 */
public interface HttpCallbackListener {

    //加载结束
    void onFinish(String response);

    //加载失败
    void onError(Exception e);
}
