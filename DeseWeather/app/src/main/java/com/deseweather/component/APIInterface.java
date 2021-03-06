package com.deseweather.component;

import com.deseweather.modules.domain.VersionAPI;
import com.deseweather.modules.domain.WeatherAPI;

import rx.Observable;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by JOE on 2016/3/11.
 */
public interface APIInterface {
    String HOST = "https://api.heweather.com/x3/";

    @GET("weather") Observable<WeatherAPI> mWeatherAPI(@Query("city") String city,
                                       @Query("key") String key);

    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    @GET("http://api.fir.im/apps/latest/5630e5f1f2fc425c52000006") Observable<VersionAPI> mVersionAPI(
            @Query("api_token") String api_token);
}
