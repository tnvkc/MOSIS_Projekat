package tamara.mosis.elfak.walkhike;

public class Notification {

    public static final int icon_trophy = 1;
    public static final int icon_marker = 2;

    private int icon;
    private String notification_text;
    private String notification_time;

    public Notification(int icon, String notification_text, String notification_time) {
        this.icon = icon;
        this.notification_text = notification_text;
        this.notification_time = notification_time;
    }

    public int getIcon() {
        return icon;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public String getNotification_time() {
        return notification_time;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }

    public void setNotification_time(String notification_time) {
        this.notification_time = notification_time;
    }
}
