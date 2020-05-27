package tamara.mosis.elfak.walkhike;

public class Leaderboard_Entry_Activity extends Leaderboard_Entry {

    public Leaderboard_Entry_Activity(String username, double points) {
        super(username, points);
    }

    public String getPoints()
    {
        return String.format("points: %f", points);
    }
}
