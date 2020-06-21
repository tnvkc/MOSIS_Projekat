package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MapObject {

    public Position position;
    public String desc;

    @Exclude
    public String key;
    public MapObject() {}

    public MapObject(String desc)
    {
        this.desc = desc;
    }

    @Override
    public String toString()
    {
        return this.desc + " pos: " + position.toString();
    }
}
