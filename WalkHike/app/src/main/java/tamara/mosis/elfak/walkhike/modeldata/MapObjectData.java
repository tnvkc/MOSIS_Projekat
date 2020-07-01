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

public class MapObjectData {

    private ArrayList<MapObject> MapObjects;
    private HashMap<String, Integer> MapObjectsMapping;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "MapObjects";


    private MapObjectData()
    {
        MapObjects = new ArrayList<>();
        MapObjectsMapping  = new HashMap<String, Integer>();

        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);
    }

    private static class SingletonHolder {
        public static final tamara.mosis.elfak.walkhike.modeldata.MapObjectData instance = new tamara.mosis.elfak.walkhike.modeldata.MapObjectData();
    }

    public static MapObjectData getInstance() {
        return MapObjectData.SingletonHolder.instance;
    }

    public ArrayList<MapObject> getMapObjects() {
        return MapObjects;
    }

    public ArrayList<MapObject> getFriendsMapObjects() {

        //override to return objects added by user or his friends
        return MapObjects;
    }

    MapObjectData.ListUpdatedEventListener updateListener;
    public void setEventListener(MapObjectData.ListUpdatedEventListener listener) {
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
            String myMapObjectKey = dataSnapshot.getKey();

            if (!MapObjectsMapping.containsKey(myMapObjectKey)) {
                MapObject myMapObject = dataSnapshot.getValue(MapObject.class);
                myMapObject.key = myMapObjectKey;
                MapObjects.add(myMapObject);
                MapObjectsMapping.put(myMapObjectKey, MapObjects.size() - 1);
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

    public void AddMapObject(MapObject m)
    {
        String key = db.push().getKey();
        MapObjects.add(m);
        MapObjectsMapping.put(key, MapObjects.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(m);
        m.key = key;
        if (updateListener != null)
            updateListener.onListUpdated();
    }
}
