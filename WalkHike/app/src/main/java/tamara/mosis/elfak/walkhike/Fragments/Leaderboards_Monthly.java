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

public class Leaderboards_Monthly extends Fragment {

    ListView leaderboard_list;
    View view;

    private int activity_or_distance;

    public int getActivity_or_distance() {
        return activity_or_distance;
    }

    public void setActivity_or_distance(int activity_or_distance) {
        this.activity_or_distance = activity_or_distance;
    }

    public Leaderboards_Monthly() {}

    public Leaderboards_Monthly(int activ_dist) {
        this.activity_or_distance = activ_dist;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leaderboards_monthly, container, false);

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
            entry1 = new Leaderboard_Entry_Activity("Josif Pancic", 30080);
            entry2 = new Leaderboard_Entry_Activity("Nadezda Petrovic", 21002);
            entry3 = new Leaderboard_Entry_Activity("Nikola Tesla", 2220);
            entry4 = new Leaderboard_Entry_Activity("Albert Ajnstajn", 1890);
            entry5 = new Leaderboard_Entry_Activity("Fridrih Nice", 114);
            entry6 = new Leaderboard_Entry_Activity("Mihajlo Petrovic", 102);
            entry7 = new Leaderboard_Entry_Activity("Milutin Milankovic", 14);
            entry8 = new Leaderboard_Entry_Activity("Ivo Andric", 0);
            entry9 = new Leaderboard_Entry_Activity("Desanka Maksimovic",  0);
            entry10 = new Leaderboard_Entry_Activity("Aristotel", 0);
        } else {
            //distance tab
            entry1 = new Leaderboard_Entry_Distance("Jelena Nikolic", 39580.0);
            entry2 = new Leaderboard_Entry_Distance("Dragana Miletic", 25390.2);
            entry3 = new Leaderboard_Entry_Distance("Nina Velickovic", 22222.93);
            entry4 = new Leaderboard_Entry_Distance("Dusica Nikolic", 10005.0);
            entry5 = new Leaderboard_Entry_Distance("Sara Minic", 1480.9);
            entry6 = new Leaderboard_Entry_Distance("Jelena Jelic", 508.8);
            entry7 = new Leaderboard_Entry_Distance("Dusan Jovic", 507.0);
            entry8 = new Leaderboard_Entry_Distance("Sandra Nikolic", 44.4);
            entry9 = new Leaderboard_Entry_Distance("Lela Rakic", 40.9);
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
