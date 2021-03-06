package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Comparator;

import tamara.mosis.elfak.walkhike.Fragments.FriendRequestsFragment;
import tamara.mosis.elfak.walkhike.Notification;
import tamara.mosis.elfak.walkhike.NotificationService;
import tamara.mosis.elfak.walkhike.NotificationsListAdapter;
import tamara.mosis.elfak.walkhike.R;

public class NotificationsActivity extends AppCompatActivity implements NotificationService.ListenerForObjectNotis {

    ListView notification_list;
    BottomNavigationView bottom_navigation_menu;
    ArrayList<Notification> notifications;
    TextView prikaz;

    Toolbar toolbar;
    Button clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_notifications);
        toolbar = (Toolbar) findViewById(R.id.notifications_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Notifications");

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        prikaz = findViewById(R.id.title_notifications);
        prikaz.setText("");
        notification_list = findViewById(R.id.notification_list);

        NotificationService.setUpListenerObjectNoti(this);
        FriendRequestsFragment.setUpListener(new FriendRequestsFragment.MyListener() {
            @Override
            public void onListUpdated() {
                Log.d("Hello", "Hello World");
            }

        });

       NotificationService.setUpListenerFriendNoti(new NotificationService.ListenerForFriendNotis() {
           @Override
           public void onNewFriendship() {
                notifications = prepareNotificationTestData();
               NotificationsListAdapter listAdapter = new NotificationsListAdapter(getApplicationContext(), R.layout.notification_view, notifications);
               notification_list.setAdapter(listAdapter);
           }
       });

       clearBtn = findViewById(R.id.notification_clear_all_notis);
       clearBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ArrayList<Notification> notifications = new ArrayList<>();
               SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                       getString(R.string.NotificationsFriend), Context.MODE_PRIVATE);
               int numOfNotis = sharedPref.getInt(getString(R.string.NotificationsNumber), 0);

               SharedPreferences.Editor editor = sharedPref.edit();
               for(int i = 0; i< numOfNotis; i++)
               {
                    editor.remove(getString(R.string.NotificationsFromUser) + i);
                    editor.remove(getString(R.string.NotificationsDate) + i);

               }
               editor.putInt(getString(R.string.NotificationsNumber), 0);
               editor.commit();


               SharedPreferences sharedPref2 = getApplicationContext().getSharedPreferences(
                       getString(R.string.NotiObjects), Context.MODE_PRIVATE);
               numOfNotis = sharedPref.getInt(getString(R.string.NotiObjectsNumber), 0);
               editor = sharedPref2.edit();

               for(int i = 0; i< numOfNotis; i++)
               {

                   editor.remove(getString(R.string.NotiObjectsFromUser) + i);
                   editor.remove(getString(R.string.NotiObjectsDate) + i);
               }
               editor.putInt(getString(R.string.NotiObjectsNumber), 0);

               numOfNotis = sharedPref2.getInt(getString(R.string.NotiObjectsNumber) + "reactions", 0);

               for(int i = 0; i< numOfNotis; i++)
               {

                   editor.remove(getString(R.string.NotiObjectsDate) + "reactions" + i);
               }
               editor.putInt(getString(R.string.NotiObjectsNumber) + "reactions", 0);
               editor.commit();


               notifications = prepareNotificationTestData();

               NotificationsListAdapter listAdapter = new NotificationsListAdapter(getApplicationContext(), R.layout.notification_view, notifications);
               ListView notification_list = findViewById(R.id.notification_list);
               notification_list.setAdapter(listAdapter);
           }



       });




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
                        finish();
                        return true;
                    }
                    case R.id.map: {
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    case R.id.completed_routes: {
                        Intent intent=new Intent(getApplicationContext(), CompletedRoutesActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    case R.id.leaderboard: {
                        Intent intent=new Intent(getApplicationContext(), LeaderboardsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });
        notifications = prepareNotificationTestData();

        NotificationsListAdapter listAdapter = new NotificationsListAdapter(this, R.layout.notification_view, notifications);
        notification_list.setAdapter(listAdapter);
    }

    private ArrayList<Notification> prepareNotificationTestData() {

       /* Notification notification1 = new Notification(1, "User Milan S. passed you on weekly leaderboard. Go on a walk now and have\nyour revenge!", "30 minutes ago");
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
        notifications.add(notification7);*/

        ArrayList<Notification> notifications = new ArrayList<>();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.NotificationsFriend), Context.MODE_PRIVATE);
        int numOfNotis = sharedPref.getInt(getString(R.string.NotificationsNumber), 0);

        for(int i = 0; i< numOfNotis; i++)
        {

            String emailFromUser = sharedPref.getString(getString(R.string.NotificationsFromUser) + i, "EMPTY");
            String dateNoti = sharedPref.getString(getString(R.string.NotificationsDate) + i, "EMPTY");
            Notification notification1 = new Notification(2, "User " + emailFromUser+
                    " wants to be your friend!", dateNoti);
            notifications.add(notification1);
        }

        SharedPreferences sharedPref2 = getApplicationContext().getSharedPreferences(
                getString(R.string.NotiObjects), Context.MODE_PRIVATE);
        numOfNotis = sharedPref.getInt(getString(R.string.NotiObjectsNumber), 0);

        for(int i = 0; i< numOfNotis; i++)
        {

            String emailFromUser = sharedPref.getString(getString(R.string.NotiObjectsFromUser) + i, "EMPTY");
            String dateNoti = sharedPref.getString(getString(R.string.NotiObjectsDate) + i, "EMPTY");
            Notification notification1 = new Notification(2, "User " + emailFromUser+
                    " left an object near you!", dateNoti);
            notifications.add(notification1);
        }

        numOfNotis = sharedPref.getInt(getString(R.string.NotiObjectsNumber) + "reactions", 0);

        for(int i = 0; i< numOfNotis; i++)
        {

            String dateNoti = sharedPref.getString(getString(R.string.NotiObjectsDate) + "reactions" + i, "EMPTY");
            Notification notification1 = new Notification(2, "Someone rected to your object!", dateNoti);
            notifications.add(notification1);
        }


        if(notifications.size() == 0)
        {
            prikaz.setText("You have no notifications");
        }
        /////////////
        notifications.sort(new Comparator<Notification>() {
            @Override
            public int compare(Notification o1, Notification o2) {
                return o1.getNotification_time().compareTo(o2.getNotification_time());
            }
        });

        return notifications;
    }


    @Override
    public void onNewObject() {
        notifications = prepareNotificationTestData();
    }

   /* @Override
    public void onBackPressed() {

        if (bottom_navigation_menu.getSelectedItemId() == R.id.map)
        {
            super.onBackPressed();
            finish();
        }
        else
        {
            bottom_navigation_menu.setSelectedItemId(R.id.map);
        }
    }*/
}
