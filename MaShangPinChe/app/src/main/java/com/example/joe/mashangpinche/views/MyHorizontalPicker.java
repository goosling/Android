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
    // mask
    private ImageView imageView;

    /*
     * view 的宽度
     */
    private int width;
    /*
     * view的高度
     */
    private int height;
    private int hsvWidth;
    private int hsvHeight;
    /*
     * hsv中subview的数量
     */
    private int itemCnt;
    /*
     * hsv中item的宽度，=subViewWidth+2*subViewMargin
     */
    private int itemWidth;
    /*
     * hsv中itme的高度，=subViewHeight
     */
    private int itemHeight;
    /*
     * hsv中subview的宽度
     */
    private int subViewWidth;
    /*
     * hsvsubview的高度。
     */
    private int subViewHeight;

    private RelativeLayout.LayoutParams hsvLayoutParams;
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

    public static final String MSG_KEY = "currentIndex";

    private MsgHandler msgHandler;
    private int msgWhat;


    public MyHorizontalPicker(Context context, AttributeSet attributeSet ) {
        super(context, attributeSet);

        hsv = new MyHorizontalScrollView(context){

            @Override
            public void onCurrentIndexChanged() {
                // TODO Auto-generated method stub
                if (null != msgHandler){
                    // 发送消息
                    Bundle b = new Bundle();
                    b.putInt(MyHorizontalPicker.MSG_KEY, getAdjustedCurrentIndex());
                    Message msg = new Message();
                    msg.setData(b);
                    msg.what = msgWhat;
                    msgHandler.sendMessage(msg);
                }
            }

        };
        imageView = new ImageView(context);

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
    public void addSubView(View view, int pos){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(subViewWidth, subViewHeight);
        lp.setMargins(subViewMargin, subViewMargin, subViewMargin, subViewMargin);
        lp.gravity=Gravity.CENTER;
        hsv.addSubView(view, pos, lp);
    }

    /**
     * hsv 增加subview.
     * @param view
     */
    public void addSubView(View view){
        addSubView(view, 0);
    }

    /**
     * 移动视图，移动位置根据dummy subview的数量进行调整。
     * @param pos
     */
    public void moveToSubView(int pos){
        int hsvPos = 0;;
        switch(maskViewPos){
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
    public void setDummySubViewCnt(int cnt){
        this.dummySubViewCnt = cnt;
    }
    public int getDummySubViewCnt(){
        return this.dummySubViewCnt;
    }

    /**
     * 初始化。
     * @param itemCnt，hsv视图单屏能够显示的subview的数量

     * @param msgWhat， 消息标识. 如果没有消息处理方，可以设置为0
     */
    public void init(int itemCnt, int maskImageID,
                     int maskImagePos, int maskBorderWidth,
                     int subViewMargin, MsgHandler msgHandler, int msgWhat){
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

        hsvLayoutParams = new RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(hsv, hsvLayoutParams);

        // 添加mask
        RelativeLayout.LayoutParams ivLayoutParams = new RelativeLayout.LayoutParams( subViewWidth + 2 * maskBorderWidth, subViewHeight + 2 * maskBorderWidth);
        ivLayoutParams.addRule(RelativeLayout.ABOVE, hsv.getId());
        this.maskViewPos = maskImagePos;
        ivLayoutParams.addRule(maskImagePos, RelativeLayout.TRUE);
        imageView.setImageDrawable(getResources().getDrawable(maskImageID));
        ivLayoutParams.setMargins(this.subViewMargin - maskBorderWidth, this.subViewMargin - maskBorderWidth, 0, 0);
//		ivLayoutParams.setMargins(this.subViewMargin - maskBorderWidth, 0, 0, 0);
        this.addView(imageView, ivLayoutParams);

        hsv.setMsgHandler(msgHandler);
        hsv.setMsgWhat(msgWhat);
        this.msgHandler = msgHandler;
        this.msgWhat = msgWhat;
    }


    /**
     * 通过dummyview的个数来调整当前指定的subview.
     * @return
     */
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
        return hsv.getItemCount();
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
        return hsv.getSubViewAt(index);
    }
}
