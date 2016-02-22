package model;

/**
 * Created by JOE on 2016/2/20.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityCode;

    public String getCountyName() {
        return countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
