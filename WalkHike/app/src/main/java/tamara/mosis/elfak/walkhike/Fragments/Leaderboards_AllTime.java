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

public class Leaderboards_AllTime extends Fragment {

    ListView leaderboard_list;
    View view;

    private int activity_or_distance;

    public int getActivity_or_distance() {
        return activity_or_distance;
    }

    public void setActivity_or_distance(int activity_or_distance) {
        this.activity_or_distance = activity_or_distance;
    }

    public Leaderboards_AllTime() {}

    public Leaderboards_AllTime(int activ_or_dist) {
        this.activity_or_distance = activ_or_dist;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leaderboards_alltime, container, false);

        //podesavanje liste
        leaderboard_list = view.findViewById(R.id.leaderboard_list);

        ArrayList<Leaderboard_Entry> entries = prepareTestData();

        LeaderboardsListAdapter listAdapter = new LeaderboardsListAdapter(getContext(), R.layout.leaderboard_entry_view, entries);
        leaderboard_list.setAdapter(listAdapter);

        return view;
    }


    private ArrayList<Leaderboard_Entry> prepareTestData() {

        Leaderboard_Entry entry1;
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
            entry1 = new Leaderboard_Entry_Activity("Marko Rakic", 2226580);
            entry2 = new Leaderboard_Entry_Activity("Nina Velickovic", 2106002);
            entry3 = new Leaderboard_Entry_Activity("Jovan Milic", 592220);
            entry4 = new Leaderboard_Entry_Activity("Dusica Nikolic", 311890);
            entry5 = new Leaderboard_Entry_Activity("Stefan Vukadinovic", 11400);
            entry6 = new Leaderboard_Entry_Activity("Jelena Jelic", 10220);
            entry7 = new Leaderboard_Entry_Activity("Dragan Dragic", 8814);
            entry8 = new Leaderboard_Entry_Activity("Sara Minic", 1532);
            entry9 = new Leaderboard_Entry_Activity("Luka Vukic",  1222);
            entry10 = new Leaderboard_Entry_Activity("Petar Nakic", 10);
        } else {
            //distance tab
            entry1 = new Leaderboard_Entry_Distance("Milica Vukic", 2223580.0);
            entry2 = new Leaderboard_Entry_Distance("Jovan Milic", 1112390.2);
            entry3 = new Leaderboard_Entry_Distance("Nina Velickovic",  222222.93);
            entry4 = new Leaderboard_Entry_Distance("Dusica Nikolic",  121005.0);
            entry5 = new Leaderboard_Entry_Distance("Sara Minic",  1480.9);
            entry6 = new Leaderboard_Entry_Distance("Jelena Jelic", 1508.8);
            entry7 = new Leaderboard_Entry_Distance("Dusan Jovic", 1507.0);
            entry8 = new Leaderboard_Entry_Distance("Sandra Nikolic", 1444.4);
            entry9 = new Leaderboard_Entry_Distance("Lela Rakic", 1340.9);
            entry10 = new Leaderboard_Entry_Distance("Petar Nakic", 10);
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

        return entries;
    }

}
