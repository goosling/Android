package util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JOE on 2016/2/26.
 */
public class HttpUtils {
    public HttpUtils() {
    }

    public static String getJSONContent(String path) {
        try{
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            if (code==200) {
                return changeJSONString(connection.getInputStream());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String changeJSONString(InputStream in) {
        String jsonString = "";
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            byte[] data = new byte[1024];
            while((len=in.read()) != -1) {
                bos.write(data, 0, len);
            }
            jsonString = new String(bos.toByteArray());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
