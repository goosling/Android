package com.example.joe.mashangpinche.db;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JOE on 2016/5/22.
 */
@Root(name = "destList")
public class DestinationList implements Serializable {
    private static final long serialVersionUID = 1L;

    @ElementList(inline = true, required = false, entry = "destination")
    private List<Destination> destList;

    public List<Destination> getDestList() {
        return destList;
    }

    public void setDestList(List<Destination> destList) {
        this.destList = destList;
    }
}
