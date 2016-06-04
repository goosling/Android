package com.example.joe.simplenews.images;

import com.example.joe.simplenews.beans.ImageBean;
import com.example.joe.simplenews.utils.JsonUtils;
import com.example.joe.simplenews.utils.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/6/4.
 */
public class ImageJsonUtils {
    private static final String TAG = "ImageJsonUtils";

    /**
     * 将获取到的json转换为图片列表对象
     * @param res
     * @return
     */
    public static List<ImageBean> readJsonImageBeans(String res) {
        List<ImageBean> beans = new ArrayList<>();
        try{
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(res).getAsJsonArray();
            for(int i=0; i < array.size(); i++) {
                JsonObject object = array.get(i).getAsJsonObject();
                ImageBean news = JsonUtils.deserialize(object, ImageBean.class);
                beans.add(news);
            }
        }catch (Exception e) {
            LogUtils.e(TAG, "read json imageBeans error");
        }
        return beans;
    }
}
