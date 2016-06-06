package com.example.joe.simplenews.news;

import com.example.joe.simplenews.beans.NewsBean;
import com.example.joe.simplenews.beans.NewsDetailBean;
import com.example.joe.simplenews.utils.JsonUtils;
import com.example.joe.simplenews.utils.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/6/5.
 */
public class NewsJsonUtils {

    private final static String TAG = "NewsJsonUtils";

    /**
     * 将获取到的json转换为新闻列表对象
     * @param res
     * @param value
     * @return
     */
    public static List<NewsBean> readJsonNewsBeans(String res, String value) {
        List<NewsBean> beans = new ArrayList<>();
        try{
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(res).getAsJsonObject();
            JsonElement element = object.get(value);
            if(element == null) {
                return null;
            }

            JsonArray array = element.getAsJsonArray();
            for(int i=1; i < array.size(); i++) {
                JsonObject jo = array.get(i).getAsJsonObject();
                if(jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if(jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }

                if(!jo.has("imgextra")) {
                    NewsBean news = JsonUtils.deserialize(jo, NewsBean.class);
                    beans.add(news);
                }
            }
        }catch(Exception e) {
            LogUtils.e(TAG, "readJsonNewsBeans error");
        }
        return beans;
    }

    public static NewsDetailBean readJsonNewsDetailBean(String res, String docId) {
        NewsDetailBean bean = null;
        try{
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(res).getAsJsonObject();
            JsonElement element = object.get(docId);
            if(element == null) {
                return null;
            }

            bean = JsonUtils.deserialize(element.getAsJsonObject(), NewsDetailBean.class);
        }catch (Exception e) {
            LogUtils.e(TAG, "readJsonNewsBeans error");
        }
        return bean;
    }
}
