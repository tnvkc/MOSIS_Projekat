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
    public String createdBy;
    public String sharedWith;
    public String photo;
    public int reactionsGreat;
    public int reactionsMeh;
    public int reactionsBoo;

    @Exclude
    public String key;
    public MapObject() {}

//    public MapObject(int objectType, String desc, boolean isPublic) {
//        this.objectType = objectType;
//        this.desc = desc;
//        this.isPublic = isPublic;
//    }

    @Override
    public String toString()
    {
        return this.desc + " pos: " + position.toString();
    }
}
