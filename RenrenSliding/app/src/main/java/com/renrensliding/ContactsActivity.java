package com.renrensliding;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import db.Contact;

/**
 * Created by JOE on 2016/3/17.
 */
public class ContactsActivity extends Activity {
    //分组的布局
    private LinearLayout titleLayout;
    //分组上显示的字母
    private TextView title;
    //联系人ListView
    private ListView contactsListView;

    //联系人列表适配器
    private ContactAdapter adapter;

    //存储手机中所有联系人
    private List<Contact> contacts = new ArrayList<>();

    private AlphabetIndexer indexer;

    private Button alphabetButton;

    private TextView sectiontoastText;

    private RelativeLayout sectionToastLayout;

    //定义字母表排序规则
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    //上次第一个可用元素，用于滚动记录标识
    private int lastFirstVisibleItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_layout);

        adapter = new ContactAdapter(this, R.layout.contact_item, contacts);
        titleLayout = (LinearLayout)findViewById(R.id.title_layout);
        title = (TextView)findViewById(R.id.title);
        contactsListView = (ListView)findViewById(R.id.contacts_list_view);
        alphabetButton = (Button)findViewById(R.id.alphabetButton);
        sectiontoastText = (TextView)findViewById(R.id.section_toast_text);

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,
                new String[] { "display_name", "sort_key" }, null, null, "sort_key");
        if(cursor.moveToFirst()) {
            do{
                String name = cursor.getString(0);
                String sortKey = cursor.getString(1);
                Contact contact = new Contact();
                contact.setName(name);
                contact.setSortKey(sortKey);
                contacts.add(contact);
            }while(cursor.moveToNext());
        }
        startManagingCursor(cursor);
        indexer = new AlphabetIndexer(cursor, 1, alphabet);
        adapter.setindexer(indexer);
        if(contacts.size()>0) {
            setupContactsListView();
            setAlphabetListener();
        }
    }

    /**
     * 为联系人ListView设置监听事件，根据当前的滑动状态来改变分组的显示位置，从而实现挤压动画的效果。
     */
    private void setupContactsListView() {
        contactsListView.setAdapter(adapter);
        contactsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int section = indexer.getSectionForPosition(firstVisibleItem);
                int nextSecPosition = indexer.getPositionForSection(section+1);
                if(firstVisibleItem != lastFirstVisibleItem) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)titleLayout.getLayoutParams();
                    params.topMargin = 0;
                    titleLayout.setLayoutParams(params);
                    title.setText(String.valueOf(alphabet.charAt(section)));
                }
                if(nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if(childView != null) {
                        int titleHeight = titleLayout.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)titleLayout.getLayoutParams();
                        if(bottom<titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int)pushedDistance;
                            titleLayout.setLayoutParams(params);
                        }else {
                            if(params.topMargin != 0) {
                                params.topMargin = 0;
                                titleLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     *
     * @param sortKeyString
     *            数据库中读取出的sort key
     * @return 英文字母或者#
     */
    private String getSortKey(String sortKeyString) {
        alphabetButton.getHeight();
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if(key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    /**
     * 设置字母表上的触摸事件，根据当前触摸的位置结合字母表的高度，计算出当前触摸在哪个字母上。
     * 当手指按在字母表上时，展示弹出式分组。手指离开字母表时，将弹出式分组隐藏。
     */
    private void setAlphabetListener() {
        alphabetButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float alphabetHeight = alphabetButton.getHeight();
                float y = event.getY();
                int sectionPosition = (int)((y / alphabetHeight) / (1f / 27f));
                if(sectionPosition<0) {
                    sectionPosition = 0;
                }else if(sectionPosition > 26){
                    sectionPosition = 26;
                }
                String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));
                int position = indexer.getPositionForSection(sectionPosition);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        alphabetButton.setBackgroundResource(R.drawable.a_z_click);
                        sectionToastLayout.setVisibility(View.VISIBLE);
                        sectiontoastText.setText(sectionLetter);
                        contactsListView.setSelection(position);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        sectiontoastText.setText(sectionLetter);
                        contactsListView.setSelection(position);
                        break;
                    default:
                        alphabetButton.setBackgroundResource(R.drawable.a_z);
                        sectionToastLayout.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

}
