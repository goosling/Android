package MyView;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joehu.csdnapp.R;

/**
 * Created by joehu on 2016/3/29.
 */
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {
    private int newsType = 0;



    public MainFragment(int newsType) {
        this.newsType = newsType;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_item_fragment_main, null);
        TextView tip = (TextView)view.findViewById(R.id.id_tip);
        tip.setText(TabAdapter.TITLE[newsType]);
        return view;
    }
}
