package testListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.joe.activitytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 2016/1/21.
 */
public class ListViewActivity extends Activity {

    private List<Fruit> fruitList = new ArrayList<Fruit>();

//    private String data[] = {"Apple", "Banana", "Orange", "Watermelon", "Pear",
//                            "Grape", "Pineapple", "Strawberry", "Cherry", "Mango"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        initFruits();//初始化水果

        FruitAdapter adapter = new FruitAdapter(ListViewActivity.this, R.layout.fruit_item,
                fruitList);

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListViewActivity.this,
       //         android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
     }

    private void initFruits() {
        Fruit apple = new Fruit("Apple", R.drawable.ascii_dora);
        fruitList.add(apple);
        Fruit banana = new Fruit("Banana", R.drawable.ascii_dora);
        fruitList.add(banana);
        Fruit orange = new Fruit("Orange", R.drawable.ascii_dora);
        fruitList.add(orange);
        Fruit watermelon = new Fruit("Watermelon", R.drawable.ascii_dora);
        fruitList.add(watermelon);
        Fruit pear = new Fruit("Pear", R.drawable.ascii_dora);
        fruitList.add(pear);
        Fruit grape = new Fruit("Grape", R.drawable.ascii_dora);
        fruitList.add(grape);
        Fruit pineapple = new Fruit("Pineapple", R.drawable.ascii_dora);
        fruitList.add(pineapple);
        Fruit strawberry = new Fruit("Strawberry", R.drawable.ascii_dora);
        fruitList.add(strawberry);
        Fruit cherry = new Fruit("Cherry", R.drawable.ascii_dora);
        fruitList.add(cherry);
        Fruit mango = new Fruit("Mango", R.drawable.ascii_dora);
        fruitList.add(mango);
    }
}
