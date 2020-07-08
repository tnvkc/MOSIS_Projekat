package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Activity;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Activity_Distance_ViewPagerAdapter;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Distance;
import tamara.mosis.elfak.walkhike.R;

public class LeaderboardsActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation_menu;

    private TabLayout leaderboards_tablayout_activity_distance;
    private ViewPager leaderboards_viewpager_activity_distance;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_leaderboards);

        toolbar = (Toolbar) findViewById(R.id.leaderboards_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Leaderboards");

        getSupportActionBar().setDisplayShowTitleEnabled(true);


        bottom_navigation_menu = findViewById(R.id.bottom_navigation_menu);
        bottom_navigation_menu.setSelectedItemId(R.id.leaderboard);
        bottom_navigation_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notifications: {
                        Intent intent=new Intent(getApplicationContext(), NotificationsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
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
                }
                return false;
            }
        });


        //activity/distance tab-ovi:

        leaderboards_tablayout_activity_distance = (TabLayout) findViewById(R.id.tablayout_activity_distance);
        leaderboards_viewpager_activity_distance = (ViewPager) findViewById(R.id.viewpager_acivity_distance);
        Leaderboards_Activity_Distance_ViewPagerAdapter viewPagerAdapter = new Leaderboards_Activity_Distance_ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.AddFragment(new Leaderboards_Activity(), "Activity");
        viewPagerAdapter.AddFragment(new Leaderboards_Distance(), "Distance");

        leaderboards_viewpager_activity_distance.setAdapter(viewPagerAdapter);
        leaderboards_tablayout_activity_distance.setupWithViewPager(leaderboards_viewpager_activity_distance);
    }
}
