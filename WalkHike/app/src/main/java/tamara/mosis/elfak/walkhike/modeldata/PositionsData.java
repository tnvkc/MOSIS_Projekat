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

    public ArrayList<Position> getMyPlaces() {
        return positions;
    }

    ListUpdatedEventListener updateListener;
    public void setEventListener(ListUpdatedEventListener listener) {
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

            if (!PositionsMapping.containsKey(myPlaceKey)) {
                Position myPlace = dataSnapshot.getValue(Position.class);
                myPlace.key = myPlaceKey;
                positions.add(myPlace);
                PositionsMapping.put(myPlaceKey, positions.size() - 1);
                if (updateListener != null)
                    updateListener.onListUpdated();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
}
