package com.example.joe.mi_notes.ui;

import android.content.Context;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.joe.mi_notes.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JOE on 2016/5/31.
 */
public class NoteEditText extends EditText {

    private static final String TAG = "NoteEditText";

    private int mIndex;

    private int mSelectionStartBeforeDelete;

    private static final String SCHEME_TEL = "tel";

    private static final String SCHEME_HTTP = "http:";

    private static final String SCHEME_EMAIL = "mailto:";

    private static final Map<String, Integer> mSchemeActionResMap =
            new HashMap<>();

    static{
        mSchemeActionResMap.put(SCHEME_TEL, R.string.note_link_tel);
        mSchemeActionResMap.put(SCHEME_HTTP, R.string.note_link_web);
        mSchemeActionResMap.put(SCHEME_EMAIL, R.string.note_link_email);
    }

    public interface OnTextViewChangeListener{
        /**
         * Delete current edit text when happens
         * and the text is null
         */
        void onEditTextDelete(int index, String text);

        /**
         * Add edit text after current edit text when
         * happen
         */
        void onEditTextEnter(int index, String text);

        /**
         * Hide or show item option when text change
         */
        void onTextChange(int index, boolean hasText);
    }

    private OnTextViewChangeListener mOnTextViewChangeListener;

    public NoteEditText(Context context) {
        super(context, null);
        mIndex = 0;
    }

    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
    }

    public NoteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public void setOnTextViewChangeListener(OnTextViewChangeListener listener) {
        mOnTextViewChangeListener = listener;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_DEL:
                if(mOnTextViewChangeListener != null) {
                    if(0 == mSelectionStartBeforeDelete && mIndex != 0) {
                        mOnTextViewChangeListener.onEditTextDelete(mIndex, getText().toString());
                        return true;
                    }
                }else {
                    Log.d(TAG, "OnTextViewChangeListener was not seted");
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
                if (mOnTextViewChangeListener != null) {
                    int selectionStart = getSelectionStart();
                    String text = getText().subSequence(selectionStart, length()).toString();
                    setText(getText().subSequence(0, selectionStart));
                    mOnTextViewChangeListener.onEditTextEnter(mIndex + 1, text);
                } else {
                    Log.d(TAG, "OnTextViewChangeListener was not seted");
                }
                break;
            default:
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                if(mOnTextViewChangeListener != null) {
                    return false;
                }
                break;
            case KeyEvent.KEYCODE_DEL:
                mSelectionStartBeforeDelete = getSelectionStart();
                break;
            default:
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int)event.getX();
                int y = (int)event.getY();
                x -= getTotalPaddingLeft();
                y -= getTotalPaddingTop();
                x += getScrollX();
                y += getScrollY();

                Layout layout = getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                Selection.setSelection(getText(), off);
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(mOnTextViewChangeListener != null) {
            if(!focused && TextUtils.isEmpty(getText())) {
                mOnTextViewChangeListener.onTextChange(mIndex, false);
            }else {
                mOnTextViewChangeListener.onTextChange(mIndex, true);
            }
        }

        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        if(getText() instanceof Spanned) {
            int selecStart = getSelectionStart();
            int selecEnd = getSelectionEnd();

            int min = Math.min(selecStart, selecEnd);
            int max = Math.max(selecStart, selecEnd);

            final URLSpan[] urls = ((Spanned)getText()).getSpans(min, max, URLSpan.class);
            if (urls.length == 1) {
                int defaultResId = 0;
                for(String schema: mSchemeActionResMap.keySet()) {
                    if(urls[0].getURL().indexOf(schema) >= 0) {
                        defaultResId = mSchemeActionResMap.get(schema);
                        break;
                    }
                }

                if (defaultResId == 0) {
                    defaultResId = R.string.note_link_other;
                }

                menu.add(0, 0, 0, defaultResId).setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                // goto a new intent
                                urls[0].onClick(NoteEditText.this);
                                return true;
                            }
                        });
            }
        }

        super.onCreateContextMenu(menu);
    }
}
