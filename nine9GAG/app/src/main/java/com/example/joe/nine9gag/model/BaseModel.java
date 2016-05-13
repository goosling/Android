package com.example.joe.nine9gag.model;

import com.google.gson.Gson;

/**
 * Created by joe on 2016/4/19.
 */
public abstract class BaseModel {
    public String toJson() {
        return new Gson().toJson(this);
    }
}
