package com.example.joe.fragmentbestpractice;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/1/25.
 */
public class NewsTitleFragment extends Fragment {

    private ListView newsTitleListView;
    private List<News> newsList;
    private NewsAdapter adapter;
    private boolean isTwoPane;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        newsList = getNews();
        adapter = new NewsAdapter(activity, R.layout.news_item, newsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_frag,container, false);
        newsTitleListView = (ListView)view.findViewById(R.id.news_title_listview);
        newsTitleListView.setAdapter(adapter);
        newsTitleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = newsList.get(position);
                if(isTwoPane) {
                    //如果为双页模式，则刷新NewsContentFragment的内容
                    NewsContentFragment newsContentFragment = (NewsContentFragment)getFragmentManager()
                            .findFragmentById(R.id.news_content_fragment);
                    newsContentFragment.refresh(news.getTitle(), news.getContent());
                }else {
                    //如果为单页模式，直接启动NewsContentActivity
                    NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_content) != null) {
            isTwoPane = true; //可以找到news_content,为双页
        }else {
            isTwoPane = false;
        }
    }

    private List<News> getNews() {
        List<News> newsList = new ArrayList<News>();
        News news1 = new News();
        news1.setTitle("Succeed in college as Learning Disabled Student");
        news1.setContent("College freshmen will soon learn to live with a " +
                "roommate, adjust to a new social scene and survive less-than-stellar " +
                "dining hall food. Students with learning disabilities will face these " +
                "transitions while also grappling with a few more hurdles.");
        newsList.add(news1);
        News news2 = new News();
        news2.setTitle("Google Android exec poached by China's Xiaomi");
        news2.setContent("China's Xiaomi has poached a key Google executive "+
                "involved in the tech giant's Android phones, in a move seen as a coup "+
                "for the rapidly growing Chinese smartphone maker.");
        newsList.add(news2);
        return newsList;
    }
}
