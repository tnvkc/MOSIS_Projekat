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


public class FriendsFragment extends Fragment implements FriendshipData.PrihvatanjePrijateljaEventListener  {

    private RecyclerView usersListView;
    private List<User> usersList;
    public UsersRecyclerAdapter usersRecyclerAdapter;
    private FirebaseFirestore firebaseFirestore;
    User loggedUser;

    MenuItem buttonAddFriend;
    FriendshipData friendshipData;
    UserData userdata;
    private List<User> users;
    TextView prikaz;
    View view;
    String email;

    public FriendsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_friends, container, false);
        //friendshipData.getInstance().getFriendships();
        firebaseFirestore=firebaseFirestore.getInstance();


        FriendshipData.getInstance().setPrihvatanjePrijateljaEventListener(this);
        SharedPreferences sharedPref = getContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");

        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);

        String index1 = email; //"email1@email.com";
        loggedUser = UserData.getInstance().getUserByUsername(username);


        usersListView=view.findViewById(R.id.friends_list_rv) ;
        usersList= friendshipData.getInstance().GetUserFriends(email);//new ArrayList<>();
        usersRecyclerAdapter=new UsersRecyclerAdapter(view.getContext(),usersList, email);

        usersListView.setHasFixedSize(true);
        usersListView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        usersListView.setAdapter(usersRecyclerAdapter);


        //usersList = new ArrayList<>();
        //buttonAddFriend=view.findViewById(R.id.friends_requests_item);
        //prikaz = (TextView) view.findViewById(R.id.friendsfrag_textview);
        //showFriendships();
        return view;
    }

    @Override
    public void onDestroy() {
        FriendshipData.getInstance().setPrihvatanjePrijateljaEventListener(null);
        super.onDestroy();
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
    public void onNovoPrijateljstvo(Friendship f) {

        if(f.accepted == true) {
            if (f.toUser.username.compareTo(loggedUser.username) == 0 ) {
                usersList.add(UserData.getInstance().getUser(f.fromUser.email));//new ArrayList<>();
            }
            else if( f.fromUser.username.compareTo(loggedUser.username) == 0)
            {
                usersList.add(UserData.getInstance().getUser(f.toUser.email));
            }
            usersRecyclerAdapter=new UsersRecyclerAdapter(view.getContext(),usersList, email);

            usersListView.setHasFixedSize(true);
            usersListView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            usersListView.setAdapter(usersRecyclerAdapter);
        }
    }

}
