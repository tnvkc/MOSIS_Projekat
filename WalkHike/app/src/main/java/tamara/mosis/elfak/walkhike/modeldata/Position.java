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

    @Override
    public String toString()
    {
        return this.desc;
    }
}
