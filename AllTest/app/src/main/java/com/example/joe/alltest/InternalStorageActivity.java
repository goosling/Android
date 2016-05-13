package com.example.joe.alltest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by JOE on 2016/2/28.
 */
public class InternalStorageActivity extends Activity {

    private EditText etFilename, etContent;
    private Button btnSave, btnDelete, btnQuery, btnAppend;
    private ListView lvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instore_layout);

        etFilename = (EditText)findViewById(R.id.etInternalFilename);
        etContent = (EditText)findViewById(R.id.etInternalContent);
        btnSave = (Button)findViewById(R.id.save);
        btnDelete = (Button)findViewById(R.id.delete);
        btnQuery = (Button)findViewById(R.id.query);
        btnAppend = (Button)findViewById(R.id.append);
        lvData = (ListView)findViewById(R.id.lvInternalFiles);

        btnSave.setOnClickListener(click);
        btnAppend.setOnClickListener(click);
        btnQuery.setOnClickListener(click);
        btnDelete.setOnClickListener(click);

        lvData.setOnItemClickListener(itemClick);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyInternalStorage storage = null;
            String filename = null;
            String content = null;
            switch (v.getId()) {
                case R.id.save:
                    filename = etFilename.getText().toString();
                    content = etContent.getText().toString();
                    storage = new MyInternalStorage(InternalStorageActivity.this);
                    try{
                        storage.save(filename, content);
                        Toast.makeText(InternalStorageActivity.this, "保存文件传成功",
                                Toast.LENGTH_SHORT).show();
                    }catch(IOException e) {
                        e.printStackTrace();
                        Toast.makeText(InternalStorageActivity.this, "保存文件失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.delete:
                    filename = etFilename.getText().toString();
                    storage = new MyInternalStorage(InternalStorageActivity.this);
                    storage.delete(filename);
                    Toast.makeText(InternalStorageActivity.this, "删除文件成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.query:
                    storage = new MyInternalStorage(InternalStorageActivity.this);
                    String[] files = storage.queryAllFile();
                    ArrayAdapter<String> fileArray = new ArrayAdapter<String>(
                            InternalStorageActivity.this,
                            android.R.layout.simple_list_item_1, files
                    );
                    lvData.setAdapter(fileArray);
                    Toast.makeText(InternalStorageActivity.this, "查询文件列表",
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.append:
                    filename = etFilename.getText().toString();
                    content = etContent.getText().toString();
                    storage = new MyInternalStorage(InternalStorageActivity.this);
                    try{
                        storage.append(filename, content);
                        Toast.makeText(InternalStorageActivity.this, "文件添加成功",
                                Toast.LENGTH_SHORT).show();
                    }catch (IOException e ) {
                        e.printStackTrace();
                        Toast.makeText(InternalStorageActivity.this, "文件添加失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView)parent;
            ArrayAdapter<String> adapter = (ArrayAdapter<String>)lv.getAdapter();
            String filename = adapter.getItem(position);
            etFilename.setText(filename);
            MyInternalStorage storage = new MyInternalStorage(InternalStorageActivity.this);
            String content;
            try{
                content = storage.get(filename);
                etContent.setText(content);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    };
}
