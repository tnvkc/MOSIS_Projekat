package tamara.mosis.elfak.walkhike.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.Users;
import tamara.mosis.elfak.walkhike.UsersRecyclerAdapter;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;


public class FriendsFragment extends Fragment {

    private RecyclerView usersListView;
    private List<User> usersList;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private FirebaseFirestore firebaseFirestore;

    MenuItem buttonAddFriend;
    FriendshipData friendshipData;
    UserData userdata;
    private List<User> users;
    TextView prikaz;

    public FriendsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friends, container, false);
        //friendshipData.getInstance().getFriendships();
        firebaseFirestore=firebaseFirestore.getInstance();

        SharedPreferences sharedPref = getContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);

        String index1 = email; //"email1@email.com";


        usersListView=view.findViewById(R.id.friends_list_rv) ;
        usersList= friendshipData.getInstance().GetUserFriends(email);//new ArrayList<>();
        usersRecyclerAdapter=new UsersRecyclerAdapter(getContext(),usersList, email);

        usersListView.setHasFixedSize(true);
        usersListView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListView.setAdapter(usersRecyclerAdapter);


        //usersList = new ArrayList<>();
        //buttonAddFriend=view.findViewById(R.id.friends_requests_item);
        //prikaz = (TextView) view.findViewById(R.id.friendsfrag_textview);
        //showFriendships();
        return view;
    }

    void showFriendships()
    {

        // FirebaseUser currentUser=mfirebaseAuth.getCurrentUser();








        //for(int i = 0; i< usersList.size(); i++)
        //{
         //   prikaziii += usersList.get(i).toString() + " ";
          //  prikaziii += "\n";
       // }

        //prikaz.setText(prikaziii);
    }

    @Override
    public void onStart()
    {
        super.onStart();
       /*firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType()==DocumentChange.Type.ADDED)
                    {
                        String user_id=doc.getDocument().getId();
                        Users users=doc.getDocument().toObject(Users.class).withId(user_id);
                        usersList.add(users);
                        usersRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });*/
    }
}
