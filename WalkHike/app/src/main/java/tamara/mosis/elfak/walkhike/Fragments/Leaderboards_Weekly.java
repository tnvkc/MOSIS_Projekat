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
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

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

        /*Leaderboard_Entry entry1;
        Leaderboard_Entry entry2;
        Leaderboard_Entry entry3;
        Leaderboard_Entry entry4;
        Leaderboard_Entry entry5;
        Leaderboard_Entry entry6;
        Leaderboard_Entry entry7;
        Leaderboard_Entry entry8;
        Leaderboard_Entry entry9;
        Leaderboard_Entry entry10;

        if (activity_or_distance == 1) {
            //activity tab
            entry1 = new Leaderboard_Entry_Activity("Nina Vukic", 26580);
            entry2 = new Leaderboard_Entry_Activity("Nevena Velickovic", 26002);
            entry3 = new Leaderboard_Entry_Activity("Stefan Milic", 5922);
            entry4 = new Leaderboard_Entry_Activity("Dusica Nikolic", 1890);
            entry5 = new Leaderboard_Entry_Activity("Petar Vukadinovic", 1400);
            entry6 = new Leaderboard_Entry_Activity("Jelena Jelic", 1020);
            entry7 = new Leaderboard_Entry_Activity("Dragan Dragic", 814);
            entry8 = new Leaderboard_Entry_Activity("Sara Minic", 532);
            entry9 = new Leaderboard_Entry_Activity("Luka Vukic",  122);
            entry10 = new Leaderboard_Entry_Activity("Petar Nakic", 10);
        } else {
            //distance tab
            entry1 = new Leaderboard_Entry_Distance("Milena Vukic", 23580.0);
            entry2 = new Leaderboard_Entry_Distance("Jovan Milic", 12390.2);
            entry3 = new Leaderboard_Entry_Distance("Nina Velickovic", 2222.93);
            entry4 = new Leaderboard_Entry_Distance("Dusica Nikolic", 2105.0);
            entry5 = new Leaderboard_Entry_Distance("Sara Minic", 140.9);
            entry6 = new Leaderboard_Entry_Distance("Jelena Jelic", 108.8);
            entry7 = new Leaderboard_Entry_Distance("Dusan Jovic", 107.0);
            entry8 = new Leaderboard_Entry_Distance("Sandra Nikolic", 44.4);
            entry9 = new Leaderboard_Entry_Distance("Lela Rakic", 40.9);
            entry10 = new Leaderboard_Entry_Distance("Petar Nakic", 0);
        }

        ArrayList<Leaderboard_Entry> entries = new ArrayList<>();
        entries.add(entry1);
        entries.add(entry2);
        entries.add(entry3);
        entries.add(entry4);
        entries.add(entry5);
        entries.add(entry6);
        entries.add(entry7);
        entries.add(entry8);
        entries.add(entry9);
        entries.add(entry10);
*/
        ArrayList<Leaderboard_Entry> entries = new ArrayList<>();

        for(int i =0; i<skorovi.size(); i++)
        {
            User referringTo = UserData.getInstance().getUserByUsername(skorovi.get(i).useer);
            if(activity_or_distance == 1)
            {
                entries.add(new Leaderboard_Entry_Activity(referringTo.username, skorovi.get(i).weeklyActivity, referringTo.image));
            }
            else
            {
                entries.add(new Leaderboard_Entry_Distance(referringTo.username, skorovi.get(i).weeklyDistance, referringTo.image));
            }
        }
        return entries;
    }

}
