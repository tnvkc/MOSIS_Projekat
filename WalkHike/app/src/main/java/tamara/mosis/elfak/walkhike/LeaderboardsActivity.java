package tamara.mosis.elfak.walkhike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_AllTime;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Monthly;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_ViewPagerAdapter;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Weekly;

public class LeaderboardsActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation_menu;

    private TabLayout leaderboards_tablayout;
    private ViewPager leaderboards_viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

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
                    case R.id.completed_routes: {
                        Intent intent=new Intent(getApplicationContext(), CompletedRoutesActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });

        /*
        leaderboards_tablayout = (TabLayout) findViewById(R.id.tablayout_leaderboards);
        leaderboards_viewpager = (ViewPager) findViewById(R.id.viewpager_leaderboards);
        Leaderboards_ViewPagerAdapter adapter = new Leaderboards_ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.AddFragment(new Leaderboards_Weekly(), "Weekly");
        adapter.AddFragment(new Leaderboards_Monthly(), "Monthly");
        adapter.AddFragment(new Leaderboards_AllTime(), "All time");

        leaderboards_viewpager.setAdapter(adapter);
        leaderboards_tablayout.setupWithViewPager(leaderboards_viewpager);
         */

    }
}
