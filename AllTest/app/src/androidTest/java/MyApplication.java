import android.app.Application;

/**
 * Created by joe on 2016/4/7.
 */
public class MyApplication extends Application {

    private static MyApplication app;

    //application全局只有一个，无需对其进行多重实例保护
    public MyApplication getInstance() {
        return app;
    }

    //this即为当前application实例
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
