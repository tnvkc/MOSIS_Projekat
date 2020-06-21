package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Position {

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
