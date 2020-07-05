package tamara.mosis.elfak.walkhike.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.GroupItemsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.GroupSavedRecyclerAdapter;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Savedroutes_group_items#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Savedroutes_group_items extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    TextView prikaz;

    ArrayList<String> objekti;

   // MapObjectData mapObjectData;


    RecyclerView rec;
    private GroupItemsRecyclerAdapter recAdapter;
    String groupName;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Savedroutes_group_items() {
        // Required empty public constructor
    }

    public Savedroutes_group_items(String groupName) {
        // Required empty public constructor
        this.groupName = groupName;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Savedroutes_group_items.
     */
    // TODO: Rename and change types and number of parameters
    public static Savedroutes_group_items newInstance(String param1, String param2) {
        Savedroutes_group_items fragment = new Savedroutes_group_items();
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
        view =  inflater.inflate(R.layout.fragment_savedroutes_group_items, container, false);

        prikaz = view.findViewById(R.id.savedroutes_groupitems_textview);
        popuniObjekte();
        rec = view.findViewById(R.id.saved_routes_groupitems_recView);
       //prikaz.setText("OVO JE FRAGMENT");

        recAdapter=new GroupItemsRecyclerAdapter(view.getContext(), objekti , groupName);


        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(view.getContext()));

        rec.setAdapter(recAdapter);

        return view;
    }

    void popuniObjekte()
    {
        String prikazz = "\n\n\n\n";
        objekti = new ArrayList<>();
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);
        int num = sharedPref.getInt(getString(R.string.NumberOfSavedTotal), 0);
        prikazz+=groupName;

        int numm = sharedPref.getInt(getString(R.string.NumberOfSavedGroup) + groupName, -1);
        if(numm != -1)
        {
            prikazz += groupName + "\n";
            for(int j= 0; j< numm; j++) {
                String objectId = sharedPref.getString(getString(R.string.SavedRoute) + groupName + j, "EMPTY");
                //MapObject m = mapObjectData.getInstance().getObjectWithDatetimeUser(objectId) ;
                if(objectId.compareTo("EMPTY") != 0)
                    objekti.add(objectId);
            }
        }



        /*for(int i = 0; i< objekti.size(); i++)
        {
            MapObject curr = objekti.get(i);
            prikazz += curr.desc + " date: "+ curr.date + "\n";
        }*/

        //prikaz.setText(prikazz);

    }
}
