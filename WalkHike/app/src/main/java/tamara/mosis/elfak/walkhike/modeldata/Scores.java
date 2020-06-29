package tamara.mosis.elfak.walkhike.modeldata;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Scores {
    public int weeklyActivity;
    public int monthlyActivity;
    public int alltimeActivity;

    public int weeklyDistance;
    public int monthlyDistance;
    public int alltimeDistance;
    public User user;

    @Exclude
    public String key;
    public Scores() {}

    public Scores(User newUser)
    {
        this.alltimeDistance = 0;
        this.monthlyDistance  = 0;
        this.weeklyDistance = 0;

        this.alltimeActivity = 0;
        this.monthlyActivity  = 0;
        this.weeklyActivity = 0;
        this.user = newUser;
    }

    @Override
    public String toString()
    {
        return this.user.toString() + " : activity + " + this.weeklyActivity + "," + this.monthlyActivity + ", " + this.alltimeActivity +
                 " : distance + " + this.weeklyDistance + "," + this.monthlyDistance + ", " + this.alltimeDistance ;
    }
}
