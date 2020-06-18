package tamara.mosis.elfak.walkhike.Fragments;

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


public class OtherUsersFragment extends Fragment {

    private RecyclerView usersListView;
    private List<Users> usersList;
    private OtherUsersRecyclerAdapter usersRecyclerAdapter;
    private FirebaseFirestore firebaseFirestore;

    public OtherUsersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_other_users, container, false);

        firebaseFirestore=firebaseFirestore.getInstance();
        usersListView=view.findViewById(R.id.other_users_list_rv) ;
        usersList=new ArrayList<>();
        usersRecyclerAdapter=new OtherUsersRecyclerAdapter(getContext(),usersList);

        usersListView.setHasFixedSize(true);
        usersListView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListView.setAdapter(usersRecyclerAdapter);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        });
    }
}
