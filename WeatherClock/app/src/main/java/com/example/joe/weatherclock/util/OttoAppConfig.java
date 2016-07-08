package com.example.joe.weatherclock.util;

import com.squareup.otto.Bus;

/**
 * Created by JOE on 2016/7/8.
 */
public class OttoAppConfig {

    private static Bus sBus;

    public static Bus getInstance() {
        if(sBus == null) {
            synchronized (OttoAppConfig.class) {
                if(sBus == null) {
                    sBus = new Bus();
                }
            }
        }
        return sBus;
    }
}
