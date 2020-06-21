package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Notification;
import tamara.mosis.elfak.walkhike.NotificationsListAdapter;
import tamara.mosis.elfak.walkhike.R;

public class NotificationsActivity extends AppCompatActivity {

    ListView notification_list;
    BottomNavigationView bottom_navigation_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_notifications);

        notification_list = findViewById(R.id.notification_list);

        bottom_navigation_menu = findViewById(R.id.bottom_navigation_menu);
        bottom_navigation_menu.setSelectedItemId(R.id.notifications);
        bottom_navigation_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.friends: {
                        Intent intent=new Intent(getApplicationContext(), FriendslistActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.map: {
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.completed_routes: {
                        Intent intent=new Intent(getApplicationContext(), CompletedRoutesActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.leaderboard: {
                        Intent intent=new Intent(getApplicationContext(), LeaderboardsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });
        ArrayList<Notification> notifications = prepareNotificationTestData();

        NotificationsListAdapter listAdapter = new NotificationsListAdapter(this, R.layout.notification_view, notifications);
        notification_list.setAdapter(listAdapter);
    }

    private ArrayList<Notification> prepareNotificationTestData() {

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

        return notifications;
    }
}
