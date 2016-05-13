package util;

/**
 * Created by JOE on 2016/2/20.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}