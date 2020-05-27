package tamara.mosis.elfak.walkhike.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Monthly;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_ViewPagerAdapter;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Weekly;

public class Leaderboards_Activity extends Fragment {

    private TabLayout leaderboards_tablayout;
    private ViewPager leaderboards_viewpager;

    View view;

    public Leaderboards_Activity() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leaderboards_activity, container, false);

        leaderboards_tablayout = (TabLayout) view.findViewById(R.id.tablayout_leaderboards_activity);
        leaderboards_viewpager = (ViewPager) view.findViewById(R.id.viewpager_leaderboards_activity);
        Leaderboards_ViewPagerAdapter adapter = new Leaderboards_ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.AddFragment(new Leaderboards_Weekly(1), "Weekly");
        adapter.AddFragment(new Leaderboards_Monthly(1), "Monthly");
        adapter.AddFragment(new Leaderboards_AllTime(1), "All time");

        leaderboards_viewpager.setAdapter(adapter);
        leaderboards_tablayout.setupWithViewPager(leaderboards_viewpager);

        return view;
    }
}
