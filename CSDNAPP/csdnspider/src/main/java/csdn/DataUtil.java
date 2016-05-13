package csdn;

import java.io.InputStream;
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

            if(connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                int len = 0;
                byte[] buf = new byte[2014];

                while((len = in.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len, "UTF-8"));
                }
                in.close();
            }else {
                throw new CommonException("访问网络失败");
            }
        }catch (Exception e) {
            throw new CommonException("访问网络失败");
        }
        return sb.toString();
    }
}
