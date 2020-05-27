package tamara.mosis.elfak.walkhike;

public class Leaderboard_Entry_Distance extends Leaderboard_Entry {

    public Leaderboard_Entry_Distance(String username, double points) {
        super(username, points);
    }

    public String getPoints()
    {
        return String.format("walked: %fm", points);
    }
}
