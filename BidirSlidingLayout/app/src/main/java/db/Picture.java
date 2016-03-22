package db;

/**
 * Created by JOE on 2016/3/21.
 */
public class Picture {
    private String name;

    private int resource;

    public Picture(String name, int resource) {
        this.name = name;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public int getResource() {
        return resource;
    }
}
