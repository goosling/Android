package com.example.joe.mashangpinche.db;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by JOE on 2016/5/22.
 * 用户发短信或者打电话结果
 */
@Root(name = "actionresult")
public class ActionResult implements Serializable{
    private static final long serialVersionUID = -9181123030885566491L;
    public static final int ACTION_CALL = 1;
    public  static final int ACTION_SMS = 2;
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAIL = 2;
    public static final int RESULT_UNKNOWN = 0;

    @Element
    private String myID;
    @Element
    private String taID;

    //用户行为
    @Element(required = false)
    private int theAction;

    //打电话结果，通过用户询问获得
    @Element(required = false)
    private int result;

    //打电话或者发短信时间
    @Element(required = false)
    private long actionTime;

    public String getMyID() {
        return myID;
    }

    public void setMyID(String myID) {
        this.myID = myID;
    }

    public String getTaID() {
        return taID;
    }

    public void setTaID(String taID) {
        this.taID = taID;
    }

    public int getTheAction() {
        return theAction;
    }

    public void setTheAction(int theAction) {
        this.theAction = theAction;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getActionTime() {
        return actionTime;
    }

    public void setActionTime(long actionTime) {
        this.actionTime = actionTime;
    }
}
