package utils;

import retrofit.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * Created by joe on 2016/4/24.
 */
public class HttpUtils {
    //eg : http://ip.taobao.com/service/getIpInfo.php?ip=202.202.32.202

    private static final String ENDPOINT = "http://ip.taobao.com/service";

    //retrofit第一步，创建接口
    public interface TaobaoIPService{
        @GET("getIpInfo.php")

    }

    Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public TaobaoIPService service = retrofit.create(TaobaoIPService.class);
}
