package UTIL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by joe on 2016/4/12.
 */
public class NetUtil {
    /**
     * 检查当前手机网络
     **/
    public static boolean checkNet(Context context) {
        //判断连接方式
        boolean wifiConnected = isWIFIConnected(context);
        boolean mobileConnected = isMOBILEConnected(context);
        if(wifiConnected == false && mobileConnected == false) {
            return false;
        }
        return true;
    }

    //判断手机是否wifi连接
    public static boolean isWIFIConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isMOBILEConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
