package com.example.joe.simplenews.weather;

import android.text.TextUtils;

import com.example.joe.simplenews.R;
import com.example.joe.simplenews.beans.WeatherBean;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/6/6.
 */
public class WeatherJsonUtils {
    /**
     * 从定位的json字串中获取城市
     * @param json
     * @return
     */
    public static String getCity(String json) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        JsonElement status = object.get("status");
        if(status != null && "OK".equals(status.getAsString())) {
            JsonObject result = object.getAsJsonObject("result");
            if(result != null) {
                JsonObject addressComponent = result.getAsJsonObject("addressComponent");
                if(addressComponent != null) {
                    JsonElement city = addressComponent.get("city");
                    if(city != null) {
                        return city.getAsString().replace("市", "");
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取天气信息
     * @param json
     * @return
     */
    public static List<WeatherBean> getWeather(String json) {
        List<WeatherBean> weatherList = new ArrayList<>();
        if(TextUtils.isEmpty(json)) {
           return weatherList;
        }

        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        String status = object.get("status").getAsString();
        if("1000".equals(status)) {
            JsonArray array = object.getAsJsonObject("data").getAsJsonArray("forecast");
            for(int i = 0; i < array.size(); i++) {
                WeatherBean weatherBean = getWeatherBeanFromJson(array.get(i).getAsJsonObject());
                weatherList.add(weatherBean);
            }
        }
        return weatherList;
    }

    private static WeatherBean getWeatherBeanFromJson(JsonObject object) {
        String temperature = object.get("high").getAsString() + " "
                + object.get("low").getAsString();
        String weather = object.get("type").getAsString();
        String wind = object.get("fengxiang").getAsString();
        String date = object.get("date").getAsString();

        WeatherBean weatherBean = new WeatherBean();

        weatherBean.setTemperature(temperature);
        weatherBean.setDate(date);
        weatherBean.setWind(wind);
        weatherBean.setWeather(weather);
        weatherBean.setWeek(date.substring(date.length() - 3));
        weatherBean.setImageRes(getWeatherImage(weather));
        return weatherBean;
    }

    private static int getWeatherImage(String weather) {
        if (weather.equals("多云") || weather.equals("多云转阴") || weather.equals("多云转晴")) {
            return R.mipmap.biz_plugin_weather_duoyun;
        } else if (weather.equals("中雨") || weather.equals("中到大雨")) {
            return R.mipmap.biz_plugin_weather_zhongyu;
        } else if (weather.equals("雷阵雨")) {
            return R.mipmap.biz_plugin_weather_leizhenyu;
        } else if (weather.equals("阵雨") || weather.equals("阵雨转多云")) {
            return R.mipmap.biz_plugin_weather_zhenyu;
        } else if (weather.equals("暴雪")) {
            return R.mipmap.biz_plugin_weather_baoxue;
        } else if (weather.equals("暴雨")) {
            return R.mipmap.biz_plugin_weather_baoyu;
        } else if (weather.equals("大暴雨")) {
            return R.mipmap.biz_plugin_weather_dabaoyu;
        } else if (weather.equals("大雪")) {
            return R.mipmap.biz_plugin_weather_daxue;
        } else if (weather.equals("大雨") || weather.equals("大雨转中雨")) {
            return R.mipmap.biz_plugin_weather_dayu;
        } else if (weather.equals("雷阵雨冰雹")) {
            return R.mipmap.biz_plugin_weather_leizhenyubingbao;
        } else if (weather.equals("晴")) {
            return R.mipmap.biz_plugin_weather_qing;
        } else if (weather.equals("沙尘暴")) {
            return R.mipmap.biz_plugin_weather_shachenbao;
        } else if (weather.equals("特大暴雨")) {
            return R.mipmap.biz_plugin_weather_tedabaoyu;
        } else if (weather.equals("雾") || weather.equals("雾霾")) {
            return R.mipmap.biz_plugin_weather_wu;
        } else if (weather.equals("小雪")) {
            return R.mipmap.biz_plugin_weather_xiaoxue;
        } else if (weather.equals("小雨")) {
            return R.mipmap.biz_plugin_weather_xiaoyu;
        } else if (weather.equals("阴")) {
            return R.mipmap.biz_plugin_weather_yin;
        } else if (weather.equals("雨夹雪")) {
            return R.mipmap.biz_plugin_weather_yujiaxue;
        } else if (weather.equals("阵雪")) {
            return R.mipmap.biz_plugin_weather_zhenxue;
        } else if (weather.equals("中雪")) {
            return R.mipmap.biz_plugin_weather_zhongxue;
        } else {
            return R.mipmap.biz_plugin_weather_duoyun;
        }
    }
}
