package com.example.joe.mashangpinche.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joe.mashangpinche.R;
import com.example.joe.mashangpinche.db.Feedback;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by joe on 2016/4/30.
 */
public class FeedBackActivity extends Activity {

    private IwantUApp app;
    private EditText et_email;
    private EditText et_content;
    private Feedback feedback;

    private String taId;
    private String email;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        app = (IwantUApp)this.getApplication();
        IwantUApp.addActivity(this);

        et_email = (EditText)findViewById(R.id.feedback_email);
        et_content = (EditText)findViewById(R.id.feedback_content);

        feedback = new Feedback();

        if(savedInstanceState != null) {
            taId = savedInstanceState.getString("taID");
            content = savedInstanceState.getString("content");
            email = savedInstanceState.getString("email");
            et_content.setText(content);
            et_email.setText(email);
        }else {
            Intent intent = getIntent();
            intent.getStringExtra(IwantUApp.ONTOLOGY_TAID);
        }
        feedback.setTaID(taId);
    }

    public void onStart() {
        super.onStart();
        Log.d("feedback", "onStart is invoked");
    }

    public void onResume() {
        super.onResume();
        Log.d("feedback", "onResume is invoked");
    }

    public void onPause() {
        super.onPause();
        Log.d("feedback", "onPause is invoked");
    }

    public void onStop() {
        super.onStop();
        Log.d("feedback", "onStop is invoked");
    }

    public void onRestart() {
        super.onRestart();
        Log.d("feedback", "onRestart is invoked");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feedback_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.feedback_actionbar_send:
                content = et_content.getText().toString();
                email = et_email.getText().toString();
                if(content.length() > 0) {
                    feedback.setContent(content);
                    feedback.setEmail(email);
                    new FeedbackTask().execute();
                }else {
                    Toast.makeText(getApplicationContext(),
                            R.string.toast_feedback_input, Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("taID", taId);
        outState.putString("content", et_content.getText().toString());
        outState.putString("email", et_email.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private class FeedbackTask extends AsyncTask<MediaType, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (null == s) {
                IwantUApp.msgHandler
                        .sendEmptyMessage(IwantUApp.MSG_TO_FEEDBACK_EX_UNKNOWN);
                return;
            }
            try {
                int responseCode = Integer.parseInt(s, 16);
                switch (responseCode) {
                    case IwantUApp.RESPONSE_CODE_FEEDBACK_SUCCESS:
                        IwantUApp.msgHandler
                                .sendEmptyMessage(IwantUApp.MSG_TO_FEEDBACK_SUCCESS);
                        break;
                    case IwantUApp.RESPONSE_CODE_FEEDBACK_FAIL:
                        IwantUApp.msgHandler
                                .sendEmptyMessage(IwantUApp.MSG_TO_FEEDBACK_FAIL);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                IwantUApp.msgHandler
                        .sendEmptyMessage(IwantUApp.MSG_TO_LOGIN_EX_UKNOWN);
                return;
            }
        }

        @Override
        protected String doInBackground(MediaType... params) {
            final String url = app.getServerBaseURL() + "/feedback";
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_XML);
            HttpEntity<Feedback> requestEntity = new HttpEntity<Feedback>(
                    feedback, requestHeaders);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(IwantUApp.CONNETION_TIMEOUT);
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            restTemplate.getMessageConverters().add(
                    new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(
                    new SimpleXmlHttpMessageConverter());
            ResponseEntity<String> response;
            try {
                response = restTemplate.exchange(url, HttpMethod.POST,
                        requestEntity, String.class);
            } catch (Exception e) {
                return null;
            }
            return (String) response.getBody();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public void handleMsg(Message msg) {
        switch(msg.what) {
            case IwantUApp.MSG_TO_FEEDBACK_EX_UNKNOWN:
            case IwantUApp.MSG_TO_FEEDBACK_FAIL:
                Toast.makeText(getApplicationContext(),
                        R.string.toast_feedback_fail, Toast.LENGTH_SHORT).show();
                break;
            case IwantUApp.MSG_TO_FEEDBACK_SUCCESS:
                Toast.makeText(getApplicationContext(),
                        R.string.toast_feedback_success, Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
        }
    }
}
