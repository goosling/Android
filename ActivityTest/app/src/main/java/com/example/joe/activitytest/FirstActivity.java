package com.example.joe.activitytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by joe on 2016/1/17.
 */
public class FirstActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //隐藏任务栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.first_layout);
        Button button1 = (Button)findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FirstActivity.this, "You clicked button1",
//                        Toast.LENGTH_SHORT).show();
                //销毁活动
                // finish();

                // intent 事件,显式intent
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivity(intent);

                //隐式intent
//                Intent intent = new Intent("com.example.joe.activitytest.ACTION_START");
//                intent.addCategory("com.example.joe.activitytest.MY_CATEGORY");
//                startActivity(intent);

                //调用浏览器
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Intent intent = new Intent(Intent.ACTION_DIAL);//拨号
//                intent.setData(Uri.parse("tel:10086"));
//                intent.setData(Uri.parse("http://www.taobao.com"));
//                startActivity(intent);

                //向下一个活动传入数据
                String data = "firstActivity";
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                intent.putExtra("extra_data", data);
                startActivity(intent);
            }
        });
    }
    //给当前活动创建菜单
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    // 定义菜单响应事件
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(this, "You clicked Add", Toast.LENGTH_SHORT).show();;
                break;
            case R.id.remove_item:
                Toast.makeText(this, "You clicked Remove", Toast.LENGTH_SHORT).show();;
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String returnedData = data.getStringExtra("data_return");
                    Log.d("FirstActivity", returnedData);
                }
                break;
            default:
        }
    }
    //按返回键传递数据
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return", "Hello FirstActivity");
        setResult(RESULT_OK, intent);
        finish();
    }




}
