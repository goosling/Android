package com.example.joe.simplenews.commons;

/**
 * Created by JOE on 2016/6/3.
 * 接口API的url
 */
public class Urls {
    //http://c.m.163.com/nc/article/headline/T1348647909107/0-5.html  头条
    public static final int PAGE_SIZE = 20;

    public static final String HOST = "http://c.m.163.com/";
    public static final String END_URL = "-" + PAGE_SIZE + ".html";
    public static final String END_DETAIL_URL = "/full.html";

    // 头条
    public static final String TOP_URL = HOST + "nc/article/headline/";
    public static final String TOP_ID = "T1348647909107";
    // 新闻详情
    public static final String NEWS_DETAIL = HOST + "nc/article/";

    public static final String COMMON_URL = HOST + "nc/article/list/";

    // 汽车
    public static final String CAR_ID = "T1348654060988";
    // 笑话
    public static final String JOKE_ID = "T1350383429665";
    // nba
    public static final String NBA_ID = "T1348649145984";

    // 图片
    public static final String IMAGES_URL = "http://api.laifudao.com/open/tupian.json";

    // 天气预报url
    public static final String WEATHER = "http://wthrcdn.etouch.cn/weather_mini?city=";

    //百度定位
    public static final String INTERFACE_LOCATION = "http://api.map.baidu.com/geocoder";
}
