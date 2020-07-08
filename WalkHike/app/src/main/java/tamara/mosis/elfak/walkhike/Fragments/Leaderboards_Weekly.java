package tamara.mosis.elfak.walkhike.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Leaderboard_Entry;
import tamara.mosis.elfak.walkhike.Activities.Leaderboard_Entry_Activity;
import tamara.mosis.elfak.walkhike.Leaderboard_Entry_Distance;
import tamara.mosis.elfak.walkhike.LeaderboardsListAdapter;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.Scores;

public class Leaderboards_Weekly extends Fragment {

    ListView leaderboard_list;
    View view;
    private int activity_or_distance;
    private ArrayList<Scores> skorovi;

    public int getActivity_or_distance() {
        return activity_or_distance;
    }

    public void setActivity_or_distance(int activity_or_distance) {
        this.activity_or_distance = activity_or_distance;
    }

    public Leaderboards_Weekly() {}

    public Leaderboards_Weekly(int activ_dist, ArrayList<Scores> skorovi) {
        this.activity_or_distance = activ_dist;
        this.skorovi = skorovi;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leaderboards_weekly, container, false);

        //podesavanje liste
        leaderboard_list = view.findViewById(R.id.leaderboard_list);

        ArrayList<Leaderboard_Entry> entries = prepareTestData();

        LeaderboardsListAdapter listAdapter = new LeaderboardsListAdapter(getContext(), R.layout.leaderboard_entry_view, entries);
        leaderboard_list.setAdapter(listAdapter);

        return view;
    }

    private ArrayList<Leaderboard_Entry> prepareTestData() {

        ArrayList<Leaderboard_Entry> entries = new ArrayList<>();

        for(int i =0; i<skorovi.size(); i++)
        {
            if(activity_or_distance == 1)
            {
                entries.add(new Leaderboard_Entry_Activity(skorovi.get(i).useer, skorovi.get(i).weeklyActivity));
            }
            else
            {
                entries.add(new Leaderboard_Entry_Distance(skorovi.get(i).useer, skorovi.get(i).weeklyDistance));
            }
        }
        return entries;
    }

}
