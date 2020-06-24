package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;

public class Friendship {

    public String desc;

    public boolean accepted;
    public User fromUser;
    public User toUser;

    @Exclude
    public String key;
    public Friendship() {}

    public Friendship(boolean acc)
    {
        this.accepted = acc;
        this.accepted = false;
    }

    @Override
    public String toString()
    {
        return  accepted + " [from " + fromUser.toString() + " to " + toUser+ "]";
    }
}
