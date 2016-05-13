package Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by JOE on 2016/3/23.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;

    public TabListener(Activity activity, String tag, Class<T> clz) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(mFragment == null) {
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            ft.add(android.R.id.content, mFragment, mTag);
        }else {
            ft.attach(mFragment);
        }
    }

    public void onTabUnSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(mFragment != null) {
            ft.detach(mFragment);
        }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
