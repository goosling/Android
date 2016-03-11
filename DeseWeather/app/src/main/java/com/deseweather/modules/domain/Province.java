package com.deseweather.modules.domain;

import java.io.Serializable;

/**
 * Created by JOE on 2016/3/11.
 */
public class Province implements Serializable {

    private String proName;

    private int proSort;

    public String getProName() {
        return proName;
    }

    public int getProSort() {
        return proSort;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public void setProSort(int proSort) {
        this.proSort = proSort;
    }
}
