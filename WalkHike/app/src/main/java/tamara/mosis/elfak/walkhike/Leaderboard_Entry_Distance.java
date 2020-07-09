package tamara.mosis.elfak.walkhike;

public class Leaderboard_Entry_Distance extends Leaderboard_Entry {

    public Leaderboard_Entry_Distance(String username, double points, String image) {
        super(username, points, image);
    }

    public String getPoints()
    {
        return String.format("walked: %fm", points);
    }
    public double getP() { return  points;}
}
