package com.example.joe.mashangpinche.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.joe.mashangpinche.R;
import com.example.joe.mashangpinche.db.Feedback;

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void handleMsg(Message msg) {

    }
}
