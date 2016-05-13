package netWorkTest;

import android.app.Activity;
import android.content.Entity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joe.servicetest.R;

import java.util.logging.Handler;

/**
 * Created by JOE on 2016/2/18.
 */
public class HttpClientTest extends Activity implements View.OnClickListener{
    public static final int SHOW_RESPONSE = 0;
    private Button sendRequest;
    private TextView responseText;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_RESPONSE:
                    String response = (String)msg.obj;
                    responseText.setText(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_layout);
        sendRequest = (Button)findViewById(R.id.send_request);
        responseText = (TextView)findViewById(R.id.response);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.send_request) {
            sendRequestWithHttpClient();
        }
    }

    private void sendRequestWithHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://www.baidu.com");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        // 将服务器返回的结果存放到Message中
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
