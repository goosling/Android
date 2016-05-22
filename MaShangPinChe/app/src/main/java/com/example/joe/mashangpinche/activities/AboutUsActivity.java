package com.example.joe.mashangpinche.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.joe.mashangpinche.R;

/**
 * Created by JOE on 2016/5/21.
 */
public class AboutUsActivity extends ActionBarActivity {

    private IwantUApp app;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.aboutus_actionbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
        app = (IwantUApp)this.getApplication();
        app.addActivity(this);

        TextView tv = (TextView)findViewById(R.id.aboutus_tv);
        String content = getResources().getString(R.string.aboutus_content);
        tv.setText(Html.fromHtml(content));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutus_actionbar_back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
