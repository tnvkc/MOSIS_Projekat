package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_AllTime;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Monthly;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_ViewPagerAdapter;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Weekly;

public class LeaderboardsActivity extends AppCompatActivity {

    private TabLayout leaderboards_tablayout;
    private ViewPager leaderboards_viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

        leaderboards_tablayout = (TabLayout) findViewById(R.id.tablayout_leaderboards);
        leaderboards_viewpager = (ViewPager) findViewById(R.id.viewpager_leaderboards);
        Leaderboards_ViewPagerAdapter adapter = new Leaderboards_ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.AddFragment(new Leaderboards_Weekly(), "Weekly");
        adapter.AddFragment(new Leaderboards_Monthly(), "Monthly");
        adapter.AddFragment(new Leaderboards_AllTime(), "All time");

        leaderboards_viewpager.setAdapter(adapter);
        leaderboards_tablayout.setupWithViewPager(leaderboards_viewpager);
    }
}
