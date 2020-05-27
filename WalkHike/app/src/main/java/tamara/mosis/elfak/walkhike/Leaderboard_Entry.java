package tamara.mosis.elfak.walkhike;

public abstract class Leaderboard_Entry {

    protected String username;
    protected double points;

    public Leaderboard_Entry(String username, double points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract String getPoints();

    public void setPoints(double points) {
        this.points = points;
    }
}
