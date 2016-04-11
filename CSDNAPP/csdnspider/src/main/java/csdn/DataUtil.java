package csdn;

import java.net.HttpURLConnection;
import java.net.URL;

import bean.CommonException;

/**
 * Created by joe on 2016/4/10.
 */
public class DataUtil {
    //返回链接地址的html数据

    public static String doGet(String urlStr) throws CommonException {
        StringBuffer sb = new StringBuffer();
        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
        }
    }
}
