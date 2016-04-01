package com.joehu.csdnapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;

/**
 * Created by joehu on 2016/4/1.
 */
public class VolleyTest {
    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }

    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
        
    }
}
