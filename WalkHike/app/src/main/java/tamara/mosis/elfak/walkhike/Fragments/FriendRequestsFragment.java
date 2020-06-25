package tamara.mosis.elfak.walkhike.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tamara.mosis.elfak.walkhike.FriendRequestsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.Users;
import tamara.mosis.elfak.walkhike.UsersRecyclerAdapter;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;


public class FriendRequestsFragment extends Fragment implements FriendshipData.ListUpdatedEventListener{

    private RecyclerView usersListView;
    private List<Users> usersList;
    private FriendRequestsRecyclerAdapter freqRecyclerAdapter;
    private FirebaseFirestore firebaseFirestore;


    FriendshipData friendshipData;
    UserData userdata;
    private List<Friendship> friendshipss;
    TextView prikaz;
    public FriendRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend_requests, container, false);

        //firebaseFirestore=FirebaseFirestore.getInstance();
        usersListView=view.findViewById(R.id.friend_requests_rv) ;
        //usersList=new ArrayList<>();
        //freqRecyclerAdapter=new FriendRequestsRecyclerAdapter(getContext(),usersList);

        //usersListView.setHasFixedSize(true);
       // usersListView.setLayoutManager(new LinearLayoutManager(getContext()));
        //usersListView.setAdapter(freqRecyclerAdapter);
        prikaz = (TextView) view.findViewById(R.id.friendrequest_textview);

        friendshipss=new ArrayList<>();
        freqRecyclerAdapter=new FriendRequestsRecyclerAdapter(friendshipss,getContext());

        usersListView.setHasFixedSize(true);
        usersListView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListView.setAdapter(freqRecyclerAdapter);


        showFriendships();
        return view;
    }

    void showFriendships()
    {

       // FirebaseUser currentUser=mfirebaseAuth.getCurrentUser();



        //friendshipss=new ArrayList<>();
        SharedPreferences sharedPref = getContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);
        ArrayList<Friendship> probepos = new ArrayList<>();
        String index1 = email; //"email1@email.com";

        probepos = friendshipData.getInstance().getFriendships();

        String prikaziii = "";



        for (int i = 0; i < probepos.size(); i++) {
            String a = probepos.get(i).toUser.email;
            if (a.compareTo(index1) == 0 && probepos.get(i).accepted == false && !friendshipss.contains(probepos.get(i))) {
                prikaziii += probepos.get(i).toString() + " ";
                friendshipss.add(probepos.get(i));
            }
            prikaziii += "\n";
        }

        prikaz.setText(prikaziii);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        friendshipData.getInstance().setEventListener(this);
       /* firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType()==DocumentChange.Type.ADDED)
                    {
                        String user_id=doc.getDocument().getId();
                        Users users=doc.getDocument().toObject(Users.class).withId(user_id);
                        usersList.add(users);
                        freqRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });*/
    }

    @Override
    public void onListUpdated() {
        showFriendships();
    }
}
