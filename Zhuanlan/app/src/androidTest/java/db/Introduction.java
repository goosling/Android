package db;

import org.litepal.crud.DataSupport;

/**
 * Created by joe on 2016/4/9.
 */
public class Introduction extends DataSupport{
    private int id;

    private String guide;

    private String digest;

    public void setId(int id) {
        this.id = id;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public int getId() {
        return id;
    }

    public String getGuide() {
        return guide;
    }

    public String getDigest() {
        return digest;
    }
}
