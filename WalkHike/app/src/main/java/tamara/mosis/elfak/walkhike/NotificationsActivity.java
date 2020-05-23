package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    ListView notification_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notification_list = findViewById(R.id.notification_list);

        Notification notification1 = new Notification(1, "User Milan S. passed you on weekly leaderboard. Go on a walk now and have\nyour revenge!", "30 minutes ago");
        Notification notification2 = new Notification(2, "User Milica V. just left something near you!\n" +
                "Go on a walk now to find out what!", "45 minutes ago");
        Notification notification3 = new Notification(1, "User Sandra S. passed you on weekly leaderboard. Go on a walk now and have\nyour revenge!", "1 day ago");
        Notification notification4 = new Notification(2, "User Milena V. just left something near you!\n" +
                "Go on a walk now to find out what!", "2 days ago");
        Notification notification5 = new Notification(1, "User Nikola S. passed you on weekly leaderboard. Go on a walk now and have\nyour revenge!", "1 month ago");
        Notification notification6 = new Notification(2, "User Nevena V. just left something near you!\n" +
                "Go on a walk now to find out what!", "2 years ago");
        Notification notification7 = new Notification(2, "User Nina V. just left something near you!\n" +
                "Go on a walk now to find out what!", "2 years ago");

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.add(notification1);
        notifications.add(notification2);
        notifications.add(notification3);
        notifications.add(notification4);
        notifications.add(notification5);
        notifications.add(notification6);
        notifications.add(notification7);

        NotificationsListAdapter listAdapter = new NotificationsListAdapter(this, R.layout.notification_view, notifications);

        notification_list.setAdapter(listAdapter);
    }
}
