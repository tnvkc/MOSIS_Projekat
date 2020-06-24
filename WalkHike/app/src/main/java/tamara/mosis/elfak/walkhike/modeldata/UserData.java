package tamara.mosis.elfak.walkhike.modeldata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserData {

    private ArrayList<User> users;
    private HashMap<String, Integer> UsersMapping;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "Users";

    private UserData()
    {
        users = new ArrayList<>();
        UsersMapping  = new HashMap<String, Integer>();

        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);

    }

    private static class SingletonHolder {
        public static final tamara.mosis.elfak.walkhike.modeldata.UserData instance = new tamara.mosis.elfak.walkhike.modeldata.UserData();
    }

    public static tamara.mosis.elfak.walkhike.modeldata.UserData getInstance() {
        return tamara.mosis.elfak.walkhike.modeldata.UserData.SingletonHolder.instance;
    }

    public ArrayList<User> getMyPlaces() {
        return users;
    }

    tamara.mosis.elfak.walkhike.modeldata.UserData.ListUpdatedEventListener updateListener;
    public void setEventListener(tamara.mosis.elfak.walkhike.modeldata.UserData.ListUpdatedEventListener listener) {
        updateListener = listener;
    }
    public interface ListUpdatedEventListener {
        void onListUpdated();
    }

    ValueEventListener parentEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myPlaceKey = dataSnapshot.getKey();

            if (!UsersMapping.containsKey(myPlaceKey)) {
                User myPlace = dataSnapshot.getValue(User.class);
                myPlace.key = myPlaceKey;
                users.add(myPlace);
                UsersMapping.put(myPlaceKey, users.size() - 1);
                if (updateListener != null)
                    updateListener.onListUpdated();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myPlaceKey = dataSnapshot.getKey();
            User myPlace = dataSnapshot.getValue(User.class);
            myPlace.key = myPlaceKey;
            if (UsersMapping.containsKey(myPlaceKey)) {
                int index = UsersMapping.get(myPlaceKey);
                users.set(index, myPlace);
            } else {
                users.add(myPlace);
                UsersMapping.put(myPlaceKey, users.size() - 1);
            }
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myPlaceKey = dataSnapshot.getKey();
            if (UsersMapping.containsKey(myPlaceKey)) {
                int index = UsersMapping.get(myPlaceKey);
                users.remove(index);
                recreateKeyIndexMapping();
            }
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void AddUser(User p)
    {
        String key = db.push().getKey();
        users.add(p);
        UsersMapping.put(key, users.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(p);
        p.key = key;
    }

    public User getPlace(int index) {
        return users.get(index);
    }

    public void deletePlace(int index) {

        db.child(FIREBASE_CHILD).child(users.get(index).key).removeValue();
        users.remove(index);
        recreateKeyIndexMapping();
    }

    public void updatePlace(int index,  User u)
    {
        User uu =users.get(index);
        uu.desc=u.desc;
        uu.email=u.email;


        db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

    }

    private void recreateKeyIndexMapping()
    {
        UsersMapping.clear();
        for (int i=0;i<users.size();i++)
            UsersMapping.put(users.get(i).key,i);
    }
}


