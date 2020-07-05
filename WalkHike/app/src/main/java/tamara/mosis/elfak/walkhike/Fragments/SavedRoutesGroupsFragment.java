package tamara.mosis.elfak.walkhike.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.GroupSavedRecyclerAdapter;
import tamara.mosis.elfak.walkhike.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedRoutesGroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedRoutesGroupsFragment extends Fragment implements GroupSavedRecyclerAdapter.ListenerOnGroupClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    ArrayList<String> grupe;
    View view;
    RecyclerView rec;
    private GroupSavedRecyclerAdapter recAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedRoutesGroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedRoutesGroupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedRoutesGroupsFragment newInstance(String param1, String param2) {
        SavedRoutesGroupsFragment fragment = new SavedRoutesGroupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_saved_routes_groups, container, false);

        rec = view.findViewById(R.id.saved_routes_recView);
        grupe = new ArrayList<>();
        SharedPreferences sharedPref = view.getContext().getSharedPreferences(
                getString(R.string.SavedRoutesShared), view.getContext().MODE_PRIVATE);
        int num = sharedPref.getInt(getString(R.string.NumberOfSavedTotal), 0);

        for(int i = 0; i< num; i++) {
            String groupName = sharedPref.getString(getString(R.string.SavedRoutesGroup) + i, "EMPTY");
            int numm = sharedPref.getInt(getString(R.string.NumberOfSavedGroup) + groupName, -1);
            grupe.add(groupName);
        }

        recAdapter=new GroupSavedRecyclerAdapter(view.getContext(), grupe );


        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(view.getContext()));

        rec.setAdapter(recAdapter);
        recAdapter.setUpListenerOnGroupClick(this);

        return  view;
    }


    @Override
    public void onGroupClick(String groupp) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.savedroutes_viewpager, new Savedroutes_group_items(groupp)).commit();
    }
}
