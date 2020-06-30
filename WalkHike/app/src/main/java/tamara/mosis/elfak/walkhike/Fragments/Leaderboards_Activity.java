package tamara.mosis.elfak.walkhike.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Monthly;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_ViewPagerAdapter;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Weekly;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.User;

public class Leaderboards_Activity extends Fragment {

    private TabLayout leaderboards_tablayout;
    private ViewPager leaderboards_viewpager;

    View view;

    ScoresData scoresData;
    FriendshipData friendshipData;
    ArrayList<Scores> skorovi;
    ArrayList<User> prijatelji;
    public Leaderboards_Activity() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leaderboards_activity, container, false);

        SharedPreferences sharedPref = getContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String emaill = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");

        User u = new User();
        u.email = emaill;

        skorovi = new ArrayList<>();
        scoresData.getInstance().getScores();
        Scores ss = null;
        ss = scoresData.getInstance().getScore(emaill);
        if(ss!=null)
            skorovi.add(ss);


        prijatelji= friendshipData.getInstance().GetUserFriends(emaill);
        findFriendsScores();



        leaderboards_tablayout = (TabLayout) view.findViewById(R.id.tablayout_leaderboards_activity);
        leaderboards_viewpager = (ViewPager) view.findViewById(R.id.viewpager_leaderboards_activity);
        Leaderboards_ViewPagerAdapter adapter = new Leaderboards_ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.AddFragment(new Leaderboards_Weekly(1, skorovi), "Weekly");
        adapter.AddFragment(new Leaderboards_Monthly(1, skorovi), "Monthly");
        adapter.AddFragment(new Leaderboards_AllTime(1, skorovi), "All time");


        leaderboards_viewpager.setAdapter(adapter);
        leaderboards_tablayout.setupWithViewPager(leaderboards_viewpager);

        return view;
    }

    void findFriendsScores()
    {
        for(int i = 0; i< prijatelji.size(); i++)
        {
            Scores s = null;
            s = scoresData.getInstance().getScore(prijatelji.get(i).email);
            if(s != null)
            {
                skorovi.add(s);
            }
        }
    }
}
