package com.example.joe.simplenews.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by JOE on 2016/6/4.
 *
 * OkHttp网络连接封装工具类
 */
public class OkHttpUtils {
    private static final String TAG = "OkHttp";

    private static OkHttpUtils mInstance;

    private OkHttpClient mClient;

    private Handler mDelivery;

    private OkHttpUtils() {
        mClient = new OkHttpClient();
        mClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mClient.setReadTimeout(30, TimeUnit.SECONDS);
        //cookie enabled
        mClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private synchronized static OkHttpUtils getInstance() {
        if(mInstance == null) {
            mInstance = new OkHttpUtils();
        }

        return mInstance;
    }

    private void getRequest(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }

    private void postRequest(String url, final ResultCallback callback, List<Param> params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    private void deliveryResult(final ResultCallback callback, Request request) {
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallback(callback, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String str = response.body().toString();
                    if (callback.mType == String.class) {
                        sendSuccessCallback(callback, str);
                    } else {
                        Object object = JsonUtils.deserialize(str, callback.mType);
                        sendSuccessCallback(callback, object);
                    }
                } catch (final Exception e) {
                    LogUtils.e(TAG, "convert json failure", e);
                    sendFailCallback(callback, e);
                }
            }
        });
    }

    private void sendFailCallback(final ResultCallback callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    private void sendSuccessCallback(final ResultCallback callback, final Object object) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if(callback != null) {
                    callback.onSuccess(object);
                }
            }
        });
    }

    private Request buildPostRequest(String url, List<Param> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for(Param param : params) {
            builder.add(param.key, param.value);
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**********************对外接口************************/

    /**
     * get请求
     * @param url  请求url
     * @param callback  请求回调
     */
    public static void get(String url, ResultCallback callback) {
        getInstance().getRequest(url, callback);
    }

    /**
     * post请求
     * @param url       请求url
     * @param callback  请求回调
     * @param params    请求参数
     */
    public static void post(String url, final ResultCallback callback, List<Param> params) {
        getInstance().postRequest(url, callback, params);
    }

    /**
     * http请求回调类,回调方法在UI线程中执行
     * @param <T>
     */
    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperClassTypeParameter(getClass());
        }

        static Type getSuperClassTypeParameter(Class<?> subClass) {
            Type superClass = subClass.getGenericSuperclass();
            if(superClass instanceof Class) {
                throw new RuntimeException("Missing type parameter");
            }

            ParameterizedType params = (ParameterizedType)superClass;
            return $Gson$Types.canonicalize(params.getActualTypeArguments()[1]);
        }

        /**
         * 请求成功回调
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         * @param e
         */
        public abstract void onFailure(Exception e);
    }

    /**
     * post请求参数类
     */
    public static class Param {
        String key;
        String value;

        public Param() {}

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

}
