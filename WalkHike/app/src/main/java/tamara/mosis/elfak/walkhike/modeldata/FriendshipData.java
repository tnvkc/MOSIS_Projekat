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

public class FriendshipData {


    private ArrayList<Friendship> Friendships;
    private HashMap<String, Integer> FriendshipsMapping;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "Friendships";

    private FriendshipData()
    {
        Friendships = new ArrayList<>();
        FriendshipsMapping  = new HashMap<String, Integer>();

        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);

    }

    private static class SingletonHolder {
        public static final tamara.mosis.elfak.walkhike.modeldata.FriendshipData instance = new tamara.mosis.elfak.walkhike.modeldata.FriendshipData();
    }

    public static tamara.mosis.elfak.walkhike.modeldata.FriendshipData getInstance() {
        return tamara.mosis.elfak.walkhike.modeldata.FriendshipData.SingletonHolder.instance;
    }

    public ArrayList<Friendship> getFriendships() {
        return Friendships;
    }

    tamara.mosis.elfak.walkhike.modeldata.FriendshipData.ListUpdatedEventListener updateListener;
    public void setEventListener(tamara.mosis.elfak.walkhike.modeldata.FriendshipData.ListUpdatedEventListener listener) {
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
            String myFriendshipKey = dataSnapshot.getKey();

            if (!FriendshipsMapping.containsKey(myFriendshipKey)) {
                Friendship myFriendship = dataSnapshot.getValue(Friendship.class);
                myFriendship.key = myFriendshipKey;
                Friendships.add(myFriendship);
                FriendshipsMapping.put(myFriendshipKey, Friendships.size() - 1);
                if (updateListener != null)
                    updateListener.onListUpdated();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myFriendshipKey = dataSnapshot.getKey();
            Friendship myFriendship = dataSnapshot.getValue(Friendship.class);
            myFriendship.key = myFriendshipKey;
            if (FriendshipsMapping.containsKey(myFriendshipKey)) {
                int index = FriendshipsMapping.get(myFriendshipKey);
                Friendships.set(index, myFriendship);
            } else {
                Friendships.add(myFriendship);
                FriendshipsMapping.put(myFriendshipKey, Friendships.size() - 1);
            }
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myFriendshipKey = dataSnapshot.getKey();
            if (FriendshipsMapping.containsKey(myFriendshipKey)) {
                int index = FriendshipsMapping.get(myFriendshipKey);
                Friendships.remove(index);
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

    public void AddFriendship(Friendship p)
    {
        String key = db.push().getKey();
        Friendships.add(p);
        FriendshipsMapping.put(key, Friendships.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(p);
        p.key = key;
    }

    public Friendship getFriendship(int index) {
        return Friendships.get(index);
    }

    public void deleteFriendship(int index) {

        db.child(FIREBASE_CHILD).child(Friendships.get(index).key).removeValue();
        Friendships.remove(index);
        recreateKeyIndexMapping();
    }

    public void updateFriendship(int index,  Friendship u)
    {
        Friendship uu =Friendships.get(index);
        uu.desc=u.desc;
        uu.accepted=u.accepted;


        db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

    }

    public void updateFriendshipTrue(int index,  boolean accepted)
    {
        Friendship uu =Friendships.get(index);
        uu.accepted=accepted;
        db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

    }

    public void updateFriendshipAccept(String mailFrom, String mailTo,  boolean accepted)
    {

        int indexx = -1;
        for(int i =0; i< Friendships.size(); i++)
        {

            if (mailFrom.compareTo(Friendships.get(i).fromUser.email) == 0
            && mailTo.compareTo(Friendships.get(i).toUser.email) == 0)
                indexx = i;
        }

        if(indexx != -1) {
            Friendship uu = Friendships.get(indexx);
            uu.accepted = accepted;
            db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);
        }

    }

    private void recreateKeyIndexMapping()
    {
        FriendshipsMapping.clear();
        for (int i=0;i<Friendships.size();i++)
            FriendshipsMapping.put(Friendships.get(i).key,i);
    }
}
