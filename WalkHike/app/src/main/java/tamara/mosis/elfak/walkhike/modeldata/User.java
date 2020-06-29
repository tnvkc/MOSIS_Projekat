package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class User implements Serializable {

    public String username;
    public String email;
    public String desc;
    public Position UserPosition;
    public String image;

    @Exclude
    public String key;
    public User() {}

    public User(String username)
    {
        this.username = username;
    }

    @Override
    public String toString()
    {
        return this.username + "," + this.email;
    }
}
