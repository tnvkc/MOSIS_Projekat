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

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tamara.mosis.elfak.walkhike.OtherUsersRecyclerAdapter;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.Users;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;


public class OtherUsersFragment extends Fragment {

    private RecyclerView usersListView;
    private List<User> usersList;
    private OtherUsersRecyclerAdapter usersRecyclerAdapter;
    private FirebaseFirestore firebaseFirestore;

    UserData userdata;
    FriendshipData friendshipData;
    User u;
    public OtherUsersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_other_users, container, false);

        //firebaseFirestore=firebaseFirestore.getInstance();
        SharedPreferences sharedPref = view.getContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);

        u = new User();
        u.email = email;
        u.username = username;

        if(userdata.getInstance().getUsers().get(indexx).email.compareTo(u.email) == 0)
            u = userdata.getInstance().getUsers().get(indexx);

        usersListView=view.findViewById(R.id.other_users_list_rv) ;
        usersList=new ArrayList<>();
        usersRecyclerAdapter=new OtherUsersRecyclerAdapter(getContext(),usersList, u);

        usersListView.setHasFixedSize(true);
        usersListView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListView.setAdapter(usersRecyclerAdapter);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //for(int i = 0; i<userdata.getInstance().getUsers().size(); i++)
             //usersList.add(userdata.getInstance().getUsers().get(i));



        ArrayList<Friendship> probepos = friendshipData.getInstance().getFriendships();



        ArrayList<User> usersList1 = friendshipData.getInstance().GetUserFriends(u.email); //new ArrayList<>();
        /*for (int i = 0; i < probepos.size(); i++) {
            String a = probepos.get(i).toUser.email;
            String b = probepos.get(i).fromUser.email;
            if (a.compareTo(u.email) == 0) {
                    usersList1.add(probepos.get(i).fromUser);
            }
            if (b.compareTo(u.email) == 0) {
                    usersList1.add(probepos.get(i).toUser);

            }
        }*/

        boolean nema= false;
        ArrayList<User> listaa = userdata.getInstance().getUsers();
        for(int i = 0; i<listaa.size(); i++) {
            nema= false;
            for (int j = 0; j < usersList1.size(); j++) {
                if (listaa.get(i).email.compareTo( usersList1.get(j).email) == 0)
                    nema = true;

                if(u.email.compareTo(listaa.get(i).email) == 0)
                {
                    nema = true;
                }
            }
            if(!nema)
                usersList.add(listaa.get(i));

        }
        //usersList.add(userdata.getInstance().getUsers().get(i));

    }
}
