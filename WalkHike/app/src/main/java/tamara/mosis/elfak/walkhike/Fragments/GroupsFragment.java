package tamara.mosis.elfak.walkhike.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Collator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import tamara.mosis.elfak.walkhike.GroupsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.OtherUsersRecyclerAdapter;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.Users;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;


public class GroupsFragment extends Fragment {


    private RecyclerView groupsListView;
    public GroupsRecyclerAdapter groupsRecyclerAdapter;
    private List<String> groupslist=new ArrayList<String>(){{
        add("Group1");
        add("Group2");
        add("Group3");
    }};

    public GroupsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_groups, container, false);

        SharedPreferences sharedPref = view.getContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

    }
}
