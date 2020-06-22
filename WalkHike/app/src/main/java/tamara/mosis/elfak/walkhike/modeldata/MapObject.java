package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MapObject {

    public Position position;
    public int objectType;
    public String desc;
    public boolean isPublic;

    @Exclude
    public String key;
    public MapObject() {}

    public MapObject(int objectType, String desc, boolean isPublic) {
        this.objectType = objectType;
        this.desc = desc;
        this.isPublic = isPublic;
    }

    @Override
    public String toString()
    {
        return this.desc + " pos: " + position.toString();
    }
}
