package com.example.joe.mashangpinche.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.joe.mashangpinche.R;
import com.example.joe.mashangpinche.db.Login;
import com.example.joe.mashangpinche.db.Member;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by joe on 2016/4/28.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private IwantUApp app;

    private Button bt_register, bt_login;

    private Login login;

    private Member member;

    private ProgressDialog mProgressDialog;

    private boolean forceUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //结束app
        if(getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        app = (IwantUApp)this.getApplication();
        IwantUApp.addActivity(this);

        bt_register = (Button) findViewById(R.id.login_bt_register);
        bt_register.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.login_bt_login);
        bt_login.setOnClickListener(this);

        bt_register.setVisibility(View.INVISIBLE);
        bt_login.setVisibility(View.INVISIBLE);

        initProgressDialog();

        new CommanderTask().execute();
    }

    public void onStart() {
        super.onStart();
        Log.d("login", "onStart is invoked");
    }

    public void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        Log.d("login", "onResume is invoked");
    }

    public void onPause() {
        super.onPause();
        Log.d("login", "onPause is invoked");

    }

    public void onStop() {
        super.onStop();
        Log.d("login", "onStop is invoked");
    }

    public void onRestart() {
        super.onRestart();
        Log.d("login", "onRestart is invoked");
    }

    //登录或注册
    private void loginOrRegister() {
        member = app.getMember();
        String id = member.getId();
        String phoneNum = member.getPhoneNum();
        String imsi = member.getImsi();

        Log.e("login", "id is " + id);
        Log.e("login", "imsi is " + imsi);
        Log.e("login", "phoneNum is " + phoneNum);

        //用户没有注册或者用户更换了手机
        if(null == id) {
            setShouldRegisterLayout();
            return;
        }

        //没有手机号或者没有imsi
        if(null == imsi || phoneNum == null || phoneNum.length() < 11) {
            setShouldRegisterLayout();
            return;
        }

        //用户更换sim卡
        if(!imsi.equals(app.getIMSI())) {
            setShouldRegisterLayout();
            return;
        }

        //注册用户，检测会员的合法性
        login = new Login();
        login.setTaID(id);
        login.setPhoneNum(phoneNum);
        login.setDatetime(System.currentTimeMillis());
        new LoginTask().execute();
    }

    /**
     * 当需要登录时的界面，登录按钮可用，注册按钮不可用
     */
    private void setShouldLoginLayout() {
        bt_login.setVisibility(View.VISIBLE);
        bt_register.setVisibility(View.INVISIBLE);
    }

    /**
     * 当需要注册时的界面，登录按钮不可用，注册按钮可用
     */
    private void setShouldRegisterLayout() {
        bt_login.setVisibility(View.INVISIBLE);
        bt_register.setVisibility(View.VISIBLE);
    }

    private void startIwantActivity() {
        Intent intent = new Intent();
        intent.setClass(this, IWantActivity.class);
        intent.putExtra(IwantUApp.ONTOLOGY_MEMBER, (Parcelable) member);
        startActivity(intent);
        this.finish();
    }

    /**
     * 向服务器请求命令。 当前只有一个命令，就是强制更新客户端。未来可扩展 。 20140317
     *
     * @author joe
     *
     */
    private class CommanderTask extends AsyncTask<MediaType, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(MediaType... params) {
            final String url = app.getServerBaseURL() + "commander";

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
            headers.set("Connection", "Close");
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(IwantUApp.CONNETION_TIMEOUT);
            RestTemplate template = new RestTemplate(requestFactory);



            return null;
        }
    }


    public void handleMsg(Message msg) {

    }
}
