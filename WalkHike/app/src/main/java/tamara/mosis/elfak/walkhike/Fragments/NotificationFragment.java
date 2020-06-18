package tamara.mosis.elfak.walkhike.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tamara.mosis.elfak.walkhike.Notification;
import tamara.mosis.elfak.walkhike.Notifications;
import tamara.mosis.elfak.walkhike.NotificationsAdapter;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.Users;

public class NotificationFragment extends Fragment {
    private RecyclerView notifListView;
    private List<Notifications> notifList;
    private NotificationsAdapter notificationsadapter;
    private FirebaseFirestore firebaseFirestore;

    public NotificationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);

        firebaseFirestore=firebaseFirestore.getInstance();
        notifListView=view.findViewById(R.id.other_users_list_rv) ;
        notifList=new ArrayList<>();
        notificationsadapter=new NotificationsAdapter(getContext(),notifList);

        notifListView.setHasFixedSize(true);
        notifListView.setLayoutManager(new LinearLayoutManager(getContext()));
        notifListView.setAdapter(notificationsadapter);

        String currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore.collection("Users").document(currentUserId).collection("Notifications").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges())
                {
                    Notifications notifications=doc.getDocument().toObject(Notifications.class);
                    notifList.add(notifications);
                    notificationsadapter.notifyDataSetChanged();

                }
            }
        });
        return view;
    }


}
