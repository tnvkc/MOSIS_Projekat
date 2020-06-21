package tamara.mosis.elfak.walkhike.Activities;

import tamara.mosis.elfak.walkhike.Leaderboard_Entry;

public class Leaderboard_Entry_Activity extends Leaderboard_Entry {

    public Leaderboard_Entry_Activity(String username, double points) {
        super(username, points);
    }

    public String getPoints()
    {
        return String.format("points: %f", points);
    }
}
