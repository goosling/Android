package MyView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by joehu on 2016/3/29.
 */
public class TabAdapter extends FragmentPagerAdapter {
    public static final String[] TITLE = new String[] {"业界", "移动", "研发", "程序员杂志", "云计算"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MainFragment fragment = new MainFragment(position);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position % TITLE.length];
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }
}
