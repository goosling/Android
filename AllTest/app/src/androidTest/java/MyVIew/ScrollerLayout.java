package MyVIew;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by joe on 2016/4/7.
 */
public class ScrollerLayout extends ViewGroup {
    //用于完成滚动操作的实例
    private Scroller mScroller;

    //判断拖动的最小移动像素
    private int mTouchSlop;

    //按下时屏幕坐标
    private float mXDown;

    private float mXMove;

    //上次触发ACTION_MOVE时屏幕坐标
    private float mXLastMove;

    //界面可滚动的左边界
    private int leftBorder;

    private int rightBorder;

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //第一步，创建scroller的实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        //获取touchSlop的值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //为ScrollerLayout中每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                //为ScrollerLayout中每一个子控件在水平方向上布局
                childView.layout(i * childView.getMeasuredWidth(), 0,
                        (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
            }
            //初始化左右边界的值
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(getChildCount() - 1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                //当手指拖动值大于touchSlop时，则认为应该进行拖动
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                //拖动距离
                int scrolledX = (int)(mXLastMove - mXMove);
                //边界保护
                if(getScrollX()+scrolledX < leftBorder) {
                    scrollTo(leftBorder, 0);
                    return true;
                }else if(getScrollX()+getWidth()+scrolledX > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy(scrolledX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                int targetIndex = (getScrollX() + getWidth()/2)/getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                //第二步，调用startScroll方法初始化滚动数据并刷新页面
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        //重写computeScroll
        if(mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
