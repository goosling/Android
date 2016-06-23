package com.example.joe.weatherclock.util;

import com.example.joe.weatherclock.Listener.HttpCallbackListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * Created by JOE on 2016/6/22.
 */
public class HttpUtil {

    private static OkHttpClient mClient;

    /**
     * 发送http请求
     *
     * @param address  访问地址
     * @param cityName 城市名
     * @param listener 响应监听
     */
    public static void sendHttpRequest(final String address, final String cityName,
                                       final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String address1;
                    if(address == null) {
                        address1 = "http://wthrcdn.etouch.cn/WeatherApi?city="
                                + URLEncoder.encode(cityName, "UTF-8");
                    }else {
                        address1 = address;
                    }
                    if(mClient == null) {
                        mClient = new OkHttpClient();
                    }
                    mClient.setReadTimeout(6000, TimeUnit.MILLISECONDS);
                    mClient.setConnectTimeout(6000, TimeUnit.MILLISECONDS);
                    mClient.setWriteTimeout(6000, TimeUnit.MILLISECONDS);
                    Request request = new Request.Builder().url(address1).build();
                    Response response = mClient.newCall(request).execute();

                    String result = response.body().toString();

                    if(listener != null) {
                        listener.onFinish(result);
                    }
                }catch(Exception e) {
                    if(listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }
}
