package UTIL;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joehu on 2016/3/29.
 */
public class DataUtil {
    /**
     * 返回该链接地址的html数据
     *
     * @param urlStr
     * @return
     * @throws CommonException
     */
    public static String doGet(String urlStr) throws CommonException {
        StringBuffer sb = new StringBuffer();
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if(conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                InputStreamReader reader = new InputStreamReader(in, "utf-8");
                int len = 0;
                //byte[] bytes = new byte[1024];
                char[] buf = new char[1024];

                while((len=reader.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                in.close();
                reader.close();
            }else {
                throw new CommonException("访问网络失败");
            }
        }catch(Exception e) {
            throw new CommonException("访问网络失败");
        }
        return sb.toString();
    }
}
