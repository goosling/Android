package com.example.joe.mashangpinche.db;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JOE on 2016/5/29.
 */
public class PubWithMemList implements Serializable {


    private static final long serialVersionUID = 1L;

    private ArrayList<PubWithMem> pmList;

    public ArrayList<PubWithMem> getPmList() {
        return pmList;
    }

    public void setPmList(ArrayList<PubWithMem> pmList) {
        this.pmList = pmList;
    }
}
