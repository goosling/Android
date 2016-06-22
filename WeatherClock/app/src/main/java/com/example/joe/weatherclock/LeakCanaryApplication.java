package com.example.joe.weatherclock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.example.joe.weatherclock.util.LogUtil;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePalApplication;

/**
 * Created by JOE on 2016/6/20.
 */
public class LeakCanaryApplication extends LitePalApplication {

    public static RefWatcher getRefWatcher(Context context) {
        LeakCanaryApplication app = (LeakCanaryApplication)context.getApplicationContext();
        return app.getRefWatcher(context);
    }

    private RefWatcher mWatcher;

    //release版本使用此方法
    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWatcher = installLeakCanary();
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtil.getInstance().v("=========", activity + "  onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtil.getInstance().v("=========", activity + "  onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtil.getInstance().v("=========", activity + "  onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtil.getInstance().v("=========", activity + "  onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtil.getInstance().v("=========", activity + "  onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtil.getInstance().v("=========", activity + "  onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtil.getInstance().v("=========", activity + "  onActivityDestroyed");
            }
        });
    }
}
