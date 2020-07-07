package tamara.mosis.elfak.walkhike.modeldata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavGraph;

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

    public static FriendshipData getInstance() {
        return FriendshipData.SingletonHolder.instance;
    }

    public ArrayList<Friendship> getFriendships() {
        return Friendships;
    }

    FriendshipData.ListUpdatedEventListener updateListener;
    public void setEventListener(FriendshipData.ListUpdatedEventListener listener) {
        updateListener = listener;
    }
    public interface ListUpdatedEventListener {
        void onListUpdatedFreidns();
    }

    ///listener za prihvatanje prijatevljstva
    FriendshipData.PrihvatanjePrijateljaEventListener novoPrijateljstvo;
    public void setPrihvatanjePrijateljaEventListener(FriendshipData.PrihvatanjePrijateljaEventListener listener) {
        novoPrijateljstvo = listener;
    }
    public interface PrihvatanjePrijateljaEventListener {
        void onNovoPrijateljstvo( Friendship f);
    }


    // za servis, da bi stigla notifikacija
    FriendshipData.NewItemListUpdatedEventListener NewUpdateListener;
    public void setNewItemEventListener(FriendshipData.NewItemListUpdatedEventListener listener) {
        NewUpdateListener = listener;
    }
    public interface NewItemListUpdatedEventListener {
        void onListUpdatedNewFriends(Friendship newF);
    }

    ReadyEventListener probaList;
    public void setReadyList(ReadyEventListener listener) {
        probaList = listener;
    }
    public interface ReadyEventListener {
        void onReady();
    }
//////////////////////////

    ValueEventListener parentEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (updateListener != null)
                updateListener.onListUpdatedFreidns();

            if(probaList != null)
            {
                probaList.onReady();
            }
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
                    updateListener.onListUpdatedFreidns();

                if(NewUpdateListener != null){
                    NewUpdateListener.onListUpdatedNewFriends(myFriendship);
                }
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
                updateListener.onListUpdatedFreidns();

            if(novoPrijateljstvo!= null)
                novoPrijateljstvo.onNovoPrijateljstvo(myFriendship);
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
                updateListener.onListUpdatedFreidns();
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

    public ArrayList<User> GetUserFriends(String userEmail)
    {
        ArrayList<Friendship> probepos;
        probepos =  this.Friendships;

        ArrayList<User> friendss = new ArrayList<>();



        for (int i = 0; i < probepos.size(); i++) {
            String a = probepos.get(i).toUser.email;
            String b = probepos.get(i).fromUser.email;
            if (a.compareTo(userEmail) == 0 )
            {
                if(probepos.get(i).accepted == true) {

                    friendss.add(probepos.get(i).fromUser);
                }
            }
            if(b.compareTo(userEmail) == 0 )
            {
                if(probepos.get(i).accepted == true) {
                    friendss.add(probepos.get(i).toUser);
                }
            }
        }

        return friendss;
    }

    public Friendship getFriendship(int index) {
        return Friendships.get(index);
    }

    public void deleteFriendship(int index) {

        db.child(FIREBASE_CHILD).child(Friendships.get(index).key).removeValue();
        Friendships.remove(index);
        recreateKeyIndexMapping();
    }

    public boolean areUsersFriedns(String user1, String user2)
    {
        int indexx = -1;
        for(int i =0; i< Friendships.size(); i++)
        {

            if (user1.compareTo(Friendships.get(i).fromUser.email) == 0
                    && user2.compareTo(Friendships.get(i).toUser.email) == 0)
                indexx = i;
            else if (user2.compareTo(Friendships.get(i).fromUser.email) == 0
                    && user1.compareTo(Friendships.get(i).toUser.email) == 0)
                indexx = i;
        }

        if(indexx != -1)
            return true;
        return false;

    }

    public void deleteFriendship(String mailFrom, String mailTo) {

        int indexx = -1;
        for(int i =0; i< Friendships.size(); i++)
        {

            if (mailFrom.compareTo(Friendships.get(i).fromUser.email) == 0
                    && mailTo.compareTo(Friendships.get(i).toUser.email) == 0)
                indexx = i;

            else if (mailFrom.compareTo(Friendships.get(i).toUser.email) == 0
                    && mailTo.compareTo(Friendships.get(i).fromUser.email) == 0)
                indexx = i;


        }

        if(indexx != -1) {
            db.child(FIREBASE_CHILD).child(Friendships.get(indexx).key).removeValue();
            Friendships.remove(indexx);
            recreateKeyIndexMapping();
        }
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
