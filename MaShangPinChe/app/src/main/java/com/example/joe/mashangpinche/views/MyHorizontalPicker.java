package com.example.joe.mashangpinche.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.joe.mashangpinche.activities.IwantUApp.MsgHandler;



/**
 * Created by joe on 2016/4/30.
 */
public class MyHorizontalPicker extends RelativeLayout {

    private MyHorizontalScrollView hsv;

    private ImageView mImageView;

    private int hsvHeight;

    private int hsvWidth;

    /**
     * View的宽高*/
    private int width, height;

    //hsv中subview的数量
    private int itemCnt;

    /*
	 * hsv中item的宽度，=subViewWidth+2*subViewMargin
	 */
    private int itemWidth;

    private int itemHeight;

    //hsv中subview的宽高
    private int subViewWidth, subViewHeight;

    private RelativeLayout.LayoutParams hsvParams;

    /*
	 * mask view在view中的位置。可以为RelativeLayout.CENTER_HORIZONTAL、RelativeLayout。ALIGN_PARENT_LEFT、RelativeLayout。ALIGN_PARENT_RIGHT
	 */
    private int maskViewPos;

    /*
	 * dummy subview的单侧数量，
	 */
    private int dummySubViewCnt;

    /*
	 * subview之间的水平间隔距离
	 */
    private int subViewMargin;

    public static final String MSG_KEYS = "currentIndex";

    private MsgHandler msgHandler;

    private int msgWhat;

    public MyHorizontalPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        hsv = new MyHorizontalScrollView(context) {
            @Override
            public void onCurrentIndexChanged() {
                if(null != msgHandler) {
                    Bundle b = new Bundle();
                    b.putInt(MyHorizontalPicker.MSG_KEYS, getAdjustedCurrentIndex());
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = msgWhat;
                    msgHandler.sendMessage(msg);
                }
            }
        };
        mImageView = new ImageView(context);
    }

    public MyHorizontalPicker(Context context, AttributeSet attributeSet, MsgHandler msgHandler, int msgWhat) {
        this(context, attributeSet);

//		hsv.setMsgHandler(msgHandler);
//		hsv.setMsgWhat(msgWhat);
        this.msgHandler = msgHandler;
        this.msgWhat = msgWhat;
    }

    /**
     * hsv 增加subview
     * @param view
     * @param pos
     */
    public void addSubView(View view, int pos) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(subViewWidth, subViewHeight);
        lp.setMargins(subViewMargin, subViewMargin, subViewMargin, subViewMargin);
        lp.gravity = Gravity.CENTER;
        hsv.addSubView(view, pos, lp);
    }

    public void addSubView(View view) {
        addSubView(view, 0);
    }

    /**
     * 移动视图，移动位置根据dummy subview的数量进行调整。
     * @param pos
     */
    public void moveToSubView(int pos) {
        int hsvPos = 0;
        switch(maskViewPos) {
            case RelativeLayout.CENTER_HORIZONTAL:
                hsvPos = pos - dummySubViewCnt;
                break;
            case RelativeLayout.ALIGN_PARENT_LEFT:
                hsvPos = pos;
                break;
            case RelativeLayout.ALIGN_PARENT_RIGHT:
                hsvPos = pos - dummySubViewCnt * 2;
                break;
        }
        hsv.moveToSubView(hsvPos);
    }

    /**
     * 初始化。
     * @param itemCnt，hsv视图单屏能够显示的subview的数量
     * @param maskImageID  mask image的souce id.
     * @param maskImagePos  mask image的位置，
     * @param maskBorderWidth  在main页面中，mask为一个边框， 该参数为边框的线条宽度，通过该宽度使得mask包围住iv
     * @param subViewMargin  subview之间水平间距。
     * @param msgHandler  消息处理方，如果没有课设置Null
     * @param msgWhat， 消息标识. 如果没有消息处理方，可以设置为0
     */
    public void init(int itemCnt, int maskImageID,
                     int maskImagePos, int maskBorderWidth,
                     int subViewMargin, MsgHandler mMsgHandler, int msgWhat){
        width = this.getLayoutParams().width;
        height = this.getLayoutParams().height;
        this.subViewMargin = subViewMargin;
        hsvHeight= height;
        hsvWidth = width;
        this.itemCnt= itemCnt;
        itemWidth = hsvWidth / this.itemCnt;
        itemHeight = itemWidth;
        subViewWidth = itemWidth - 2 * this.subViewMargin;
        subViewHeight = subViewWidth;
        hsv.setItemWidth(itemWidth);

        hsvParams = new RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(hsv, hsvParams);

        // 添加mask
        RelativeLayout.LayoutParams ivLayoutParams = new RelativeLayout.LayoutParams( subViewWidth + 2 * maskBorderWidth, subViewHeight + 2 * maskBorderWidth);
        ivLayoutParams.addRule(RelativeLayout.ABOVE, hsv.getId());
        this.maskViewPos = maskImagePos;
        ivLayoutParams.addRule(maskImagePos, RelativeLayout.TRUE);
        mImageView.setImageDrawable(getResources().getDrawable(maskImageID));
        ivLayoutParams.setMargins(this.subViewMargin - maskBorderWidth, this.subViewMargin - maskBorderWidth, 0, 0);
//		ivLayoutParams.setMargins(this.subViewMargin - maskBorderWidth, 0, 0, 0);
        this.addView(mImageView, ivLayoutParams);

        hsv.setMsgHandler(mMsgHandler);
        hsv.setMsgWhat(msgWhat);
        this.msgHandler = msgHandler;
        this.msgWhat = msgWhat;
    }

    public int getAdjustedCurrentIndex(){
        switch(maskViewPos){
            case RelativeLayout.CENTER_HORIZONTAL:
                return hsv.getCurrentIndex() + dummySubViewCnt;
            case RelativeLayout.ALIGN_PARENT_LEFT:
                return hsv.getCurrentIndex() ;
            case RelativeLayout.ALIGN_PARENT_RIGHT:
                return hsv.getCurrentIndex() + dummySubViewCnt * 2;
            default:
                return 0;
        }
    }

    public int getItemWidth() {
        return itemWidth;
    }
    public int getItemHeight() {
        return itemHeight;
    }
    public int getItemCnt(){
        return hsv.getItemCnt();
    }

    public void removeAllItemViews(){
        hsv.removeAllItemViews();
    }

    public int getSubViewWidth() {
        return subViewWidth;
    }

    public void setSubViewWidth(int subViewWidth) {
        this.subViewWidth = subViewWidth;
    }

    public int getSubViewHeight() {
        return subViewHeight;
    }

    public void setSubViewHeight(int subViewHeight) {
        this.subViewHeight = subViewHeight;
    }

    public View getSubviewAt(int index){
        return hsv.getSubviewAt(index);
    }
}
