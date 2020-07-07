package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Activity;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Activity_Distance_ViewPagerAdapter;
import tamara.mosis.elfak.walkhike.Fragments.SavedRoutesGroupsFragment;
import tamara.mosis.elfak.walkhike.Fragments.Savedroutes_group_items;
import tamara.mosis.elfak.walkhike.FriendRequestsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.GroupSavedRecyclerAdapter;
import tamara.mosis.elfak.walkhike.GroupsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.Notification;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;

public class CompletedRoutesActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation_menu;
    RecyclerView rec;
    TextView prikaz;

    private View viewvpager_groups;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_completed_routes);

        //prikaz = findViewById(R.id.savedroutes_textview);
        //
        //popuniObjekte();

        viewvpager_groups = (View) findViewById(R.id.savedroutes_viewpager);
        SavedRoutesGroupsFragment firstFragment = new SavedRoutesGroupsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.savedroutes_viewpager, firstFragment).commit();

       /* */

        bottom_navigation_menu = findViewById(R.id.bottom_navigation_menu);
        bottom_navigation_menu.setSelectedItemId(R.id.completed_routes);
        bottom_navigation_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notifications: {
                        Intent intent=new Intent(getApplicationContext(), NotificationsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
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

    }



    void onGroupClick()
    {

    }


}
