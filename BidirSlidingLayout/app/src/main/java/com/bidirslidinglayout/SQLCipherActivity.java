package com.bidirslidinglayout;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import Utils.MyDataBaseOpenHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOE on 2016/3/21.
 */
public class SQLCipherActivity extends Activity {

    @Bind(R.id.add_data)
    Button addData;
    @Bind(R.id.query_data)
    Button queryData;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlcipher);
        ButterKnife.bind(this);
        SQLiteDatabase.loadLibs(this);
        MyDataBaseOpenHelper helper = new MyDataBaseOpenHelper(this, "demo.db", null, 1);
        database = helper.getWritableDatabase("secret_key");
    }

    @OnClick({R.id.add_data, R.id.query_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_data:
                ContentValues values = new ContentValues();
                values.put("name", "GOT");
                values.put("pages", 566);
                database.insert("Book", null, values);
                break;
            case R.id.query_data:
                Cursor cursor = database.query("Book", null, null, null, null, null, null);
                if(cursor != null) {
                    while(cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        Log.d("TAG", "book names is "+name);
                        Log.d("TAG", "book pages is "+ pages);
                    }
                }
                cursor.close();
                break;
        }
    }
}
