package db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JOE on 2016/3/21.
 */
public class PictureAdapter extends ArrayAdapter<Picture> {

    public PictureAdapter(Context context, int resource, List<Picture> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Picture picture = getItem(position);
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,
                    null);
        }else {
            view = convertView;
        }
        TextView tv1 = (TextView)view.findViewById(android.R.id.text1);
        tv1.setText(picture.getName());
        return view;
    }
}
