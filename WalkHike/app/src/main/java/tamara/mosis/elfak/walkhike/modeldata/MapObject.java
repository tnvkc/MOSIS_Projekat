package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class MapObject implements Serializable {

    public Position position;
    public int objectType;
    public String desc;
    public boolean isPublic;
    public String datetime;
    public String date;
    public User createdBy;
    public User sharedWith;
    public String photo;

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
