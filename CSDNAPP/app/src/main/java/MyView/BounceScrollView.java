package MyView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by joe on 2016/4/12.
 */
public class BounceScrollView extends ScrollView {

    private boolean isCalled;

    private Callback mCallback;

    private View view;

    private Rect mRect = new Rect();

    private int y;

    private boolean isFirst = true;

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /***
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        if(getChildCount() > 0) {
            view = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (view != null)
        {
            commonOnTouch(ev);
        }

        return super.onTouchEvent(ev);
    }

    private void commonOnTouch(MotionEvent ev) {
        int action = ev.getAction();
        int cy = (int)ev.getY();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceY = cy - y;
                if(isFirst) {
                    distanceY = 0;
                    isFirst = false;
                }


        }
    }

    /**
     * 当从上往下，移动距离达到一半时，回调接口
     *
     * @return
     */
    private boolean shouldCallback(int dy) {
        if(dy > 0 && view.getTop() > getHeight()/2) {
            return true;
        }
        return false;
    }

    private void resetPosition() {
        Animation animation = new TranslateAnimation(0, 0, view.getTop(),
                mRect.top);
        animation.setDuration(200);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        view.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();
        isFirst = true;
        isCalled = false ;
    }

    public boolean isNeedMove()
    {
        int offset = view.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 0是顶部，后面那个是底部
        if (scrollY == 0 || scrollY == offset)
        {
            return true;
        }
        return false;
    }


    public void setCallBack(Callback callback)
    {
        mCallback = callback;
    }

    interface Callback
    {
        void callback();
    }
}
