package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MapObjectType1 extends MapObject {
    public String dodatak1;

    @Override
    public String toString()
    {
        return this.desc + " pos: " + position.toString() + " " + dodatak1;
    }

}
