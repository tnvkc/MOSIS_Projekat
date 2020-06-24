package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;

public class User {

    public String username;
    public String email;
    public String desc;

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
