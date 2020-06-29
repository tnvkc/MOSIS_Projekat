package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Position implements Serializable {

    public String latitude;
    public String longitude;
    public String desc;

    @Exclude
    public String key;
    public Position() {}

    public Position(String desc)
    {
        this.desc = desc;
    }

    public Position(String lat, String lon)
    {
        this.latitude = lat;
        this.longitude = lon;
    }

    @Override
    public String toString()
    {
        return this.desc + "("+ this.latitude + "," + this.longitude +")";
    }
}
