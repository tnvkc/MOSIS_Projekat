package tamara.mosis.elfak.walkhike;

public abstract class Leaderboard_Entry {

    protected String username;
    protected double points;
    protected String image;

    public Leaderboard_Entry(String username, double points, String image) {
        this.username = username;
        this.points = points;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract String getPoints();
    public abstract  double getP();

    public void setPoints(double points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }
}
