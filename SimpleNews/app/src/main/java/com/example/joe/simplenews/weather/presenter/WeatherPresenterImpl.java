package com.example.joe.simplenews.weather.presenter;

import android.content.Context;

import com.example.joe.simplenews.beans.WeatherBean;
import com.example.joe.simplenews.utils.ToolsUtil;
import com.example.joe.simplenews.weather.model.WeatherModel;
import com.example.joe.simplenews.weather.model.WeatherModelImpl;
import com.example.joe.simplenews.weather.view.WeatherView;

import java.util.List;

/**
 * Created by JOE on 2016/6/6.
 */
public class WeatherPresenterImpl implements WeatherPresenter, WeatherModelImpl.LoadWeatherListener {

    private WeatherView mWeatherView;

    private WeatherModel mWeatherModel;

    private Context mContext;

    public WeatherPresenterImpl(Context context, WeatherView mWeatherView) {
        this.mWeatherView = mWeatherView;
        this.mContext = context;
        mWeatherModel = new WeatherModelImpl();
    }

    @Override
    public void onSuccess(List<WeatherBean> list) {
        if(list != null && list.size() > 0) {
            WeatherBean todayWeather = list.remove(0);
            mWeatherView.setToday(todayWeather.getDate());
            mWeatherView.setTemperature(todayWeather.getTemperature());
            mWeatherView.setWeather(todayWeather.getWeather());
            mWeatherView.setWind(todayWeather.getWind());
            mWeatherView.setWeatherImage(todayWeather.getImageRes());
        }
        mWeatherView.setWeatherData(list);
        mWeatherView.hideProgress();
        mWeatherView.showWeatherLayout();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mWeatherView.hideProgress();
        mWeatherView.showErrorToast("获取天气数据失败");
    }

    @Override
    public void loadWeatherData() {
        mWeatherView.showProgress();
        if(!ToolsUtil.isNetworkAvailable(mContext)) {
            mWeatherView.hideProgress();
            mWeatherView.showErrorToast("无网络连接");
        }

        WeatherModelImpl.LoadLocationListener listener = new WeatherModelImpl.LoadLocationListener() {
            @Override
            public void onSuccess(String cityName) {
                //定位成功，获得定位城市天气预报
                mWeatherView.setCity(cityName);
                mWeatherModel.loadWeatherData(cityName, WeatherPresenterImpl.this);
            }

            @Override
            public void onFailure(String msg, Exception e) {
                mWeatherView.showErrorToast("定位失败");
                mWeatherView.setCity("深圳");
                mWeatherModel.loadWeatherData("深圳", WeatherPresenterImpl.this);
            }
        };
        mWeatherModel.loadLocation(mContext, listener);
    }
}
