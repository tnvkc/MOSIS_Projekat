package tamara.mosis.elfak.walkhike.modeldata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PositionsData {

    private ArrayList<Position> positions;
    private HashMap<String, Integer> PositionsMapping;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "Positions";

    private PositionsData()
    {
        positions = new ArrayList<>();
        PositionsMapping  = new HashMap<String, Integer>();

        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);

    }

    private static class SingletonHolder {
        public static final PositionsData instance = new PositionsData();
    }

    public static PositionsData getInstance() {
        return SingletonHolder.instance;
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    ListUpdatedEventListener updateListener;
    public void setEventListener(ListUpdatedEventListener listener) {
        updateListener = listener;
    }
    public interface ListUpdatedEventListener {
        void onListUpdated();
    }

    ReadyEventListener probaList;
    public void setReadyList(ReadyEventListener listener) {
        probaList = listener;
    }
    public interface ReadyEventListener {
        void onReady();
    }

    ValueEventListener parentEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (updateListener != null)
                updateListener.onListUpdated();

            if(probaList != null)
                probaList.onReady();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myPositionKey = dataSnapshot.getKey();

            if (!PositionsMapping.containsKey(myPositionKey)) {
                Position myPosition = dataSnapshot.getValue(Position.class);
                myPosition.key = myPositionKey;
                positions.add(myPosition);
                PositionsMapping.put(myPositionKey, positions.size() - 1);
                if (updateListener != null)
                    updateListener.onListUpdated();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myPositionKey = dataSnapshot.getKey();
            Position myPosition = dataSnapshot.getValue(Position.class);
            myPosition.key = myPositionKey;
            if (PositionsMapping.containsKey(myPositionKey)) {
                int index = PositionsMapping.get(myPositionKey);
                positions.set(index, myPosition);
            } else {
                positions.add(myPosition);
                PositionsMapping.put(myPositionKey, positions.size() - 1);
            }
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myPositionKey = dataSnapshot.getKey();
            if (PositionsMapping.containsKey(myPositionKey)) {
                int index = PositionsMapping.get(myPositionKey);
                positions.remove(index);
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

    public void AddPosition(Position p)
    {
        String key = db.push().getKey();
        positions.add(p);
        PositionsMapping.put(key, positions.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(p);
        p.key = key;
    }

    public Position getPosition(int index) {
        return positions.get(index);
    }

    public void deletePosition(int index) {

        db.child(FIREBASE_CHILD).child(positions.get(index).key).removeValue();
        positions.remove(index);
        recreateKeyIndexMapping();
    }

    public void updatePosition(int index,  String desc, String lng, String lat)
    {
        Position myPosition=positions.get(index);
        myPosition.desc=desc;
        myPosition.latitude=lat;
        myPosition.longitude=lng;

        db.child(FIREBASE_CHILD).child(myPosition.key).setValue(myPosition);

    }

    private void recreateKeyIndexMapping()
    {
        PositionsMapping.clear();
        for (int i=0;i<positions.size();i++)
            PositionsMapping.put(positions.get(i).key,i);
    }
}
