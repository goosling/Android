package com.wechatsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowShowingAlways();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if(menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconVisible", Boolean.TYPE
                    );
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
//屏蔽物理menu键
    private void setOverflowShowingAlways() {
        try{
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
