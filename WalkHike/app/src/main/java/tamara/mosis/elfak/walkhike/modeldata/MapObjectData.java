package tamara.mosis.elfak.walkhike.modeldata;

import android.graphics.Bitmap;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    public ArrayList<MapObject> getFriendsMapObjects(String loggedUserUsername) {

        ArrayList<MapObject> list = new ArrayList<>();
        for(int i = 0; i< this.MapObjects.size(); i++)
        {
            if(this.MapObjects.get(i).objectType != 5 && ((this.MapObjects.get(i).createdBy.username.compareTo(loggedUserUsername) == 0) ||
                    (this.MapObjects.get(i).sharedWith.username.compareTo(loggedUserUsername) == 0)))
            {
                list.add(MapObjects.get(i));
            }
        }

        return list;
    }

    /*
    public ArrayList<MapObject> getMapObjectsByType(int type, String username) {

        ArrayList<MapObject> usersMarkers = getFriendsMapObjects(username);
        ArrayList<MapObject> list = new ArrayList<>();
        for(int i = 0; i< usersMarkers.size(); i++)
        {
            if (usersMarkers.get(i).objectType == type)
            {
                list.add(usersMarkers.get(i));
            }
        }

        return list;
    }

     */

    /*
    public MapObject getSearchedMapObject(String searchText, String username) {

        ArrayList<MapObject> usersMarkers = getFriendsMapObjects(username);

        for(int i = 0; i < usersMarkers.size(); i++)
        {
            if (usersMarkers.get(i).desc.toLowerCase().compareTo(searchText.toLowerCase()) == 0) {
                return usersMarkers.get(i);
            }
        }

        return null;
    }*/

    public ArrayList<MapObject> getSearchedMapObjects(String searchText, int searchFilter, int radius, String username) {

        ArrayList<MapObject> usersMarkers = getFriendsMapObjects(username);
        ArrayList<MapObject> list = new ArrayList<>();
        for(int i = 0; i< usersMarkers.size(); i++)
        {
            MapObject mo = usersMarkers.get(i);

            if ((searchFilter == 0 || mo.objectType == searchFilter)
                    && mo.desc.toLowerCase().contains(searchText.toLowerCase().subSequence(0, searchText.length()))
                    && isCloserThanRadius(mo.position, radius, username))
            {
                list.add(mo);
            }
        }

        return list;
    }

    private boolean isCloserThanRadius(Position position, int radius, String username) {

        Position currentUserPosition = UserData.getInstance().getUserByUsername(username).UserPosition;
        double current_lat = Double.parseDouble(currentUserPosition.latitude);
        double current_lon = Double.parseDouble(currentUserPosition.longitude);

        /*
        Position objectPosition;
        if (mo.objectType == 5) {
            objectPosition = UserData.getInstance().getUserByUsername(mo.desc).UserPosition;
            //za sada, ali trebace da se update-uje pozicija i u mapobject zbog prikaza na mapi!
        } else {
            objectPosition = mo.position;
        }*/

        double object_lat = Double.parseDouble(position.latitude);
        double object_lon = Double.parseDouble(position.longitude);

        float dist[] = new float[1];
        Location.distanceBetween(current_lat, current_lon,
                object_lat, object_lon, dist);

        if(dist[0] <= radius) {
            return true;
        } else
            return false;


        /*
        preko distanceTo
        Location startPoint = new Location("");
        startPoint.setLatitude(current_lat);
        startPoint.setLongitude(current_lon);

        Location endPoint = new Location("");
        endPoint.setLatitude(object_lat);
        endPoint.setLongitude(object_lon);

        float distance = startPoint.distanceTo(endPoint);

        if (distance <= radius) {
            return true;
        } else {
            return false;
        }

         */


    }

    public ArrayList<MapObject> getFilteredMapObjects(Byte filter, String username) {

        ArrayList<MapObject> usersMarkers = getFriendsMapObjects(username);
        ArrayList<MapObject> list = new ArrayList<>();
        for(int i = 0; i< usersMarkers.size(); i++)
        {
            MapObject mo = usersMarkers.get(i);

            if (
                    ((filter & 8) == 8 && mo.objectType == 4) ||
                    ((filter & 4) == 4 && mo.objectType == 3) ||
                    ((filter & 2) == 2 && mo.objectType == 2) ||
                    ((filter & 1) == 1 && mo.objectType == 1)
            ) {
                list.add(mo);
            }
        }

        return list;
    }

    public MapObject getObjectWithDatetimeUser(String datetimePlusUser)
    {
        MapObject mo = null;
        MapObject m = null;
        for(int i = 0; i< this.MapObjects.size(); i++) {

            mo = this.MapObjects.get(i);
            if(mo.createdBy != null) {
                String s = mo.datetime + mo.createdBy.email;

                if (s.compareTo(datetimePlusUser) == 0)
                    m = mo;
            }
        }
        return m;
    }

    public ArrayList<MapObject> getMapObjectsFromTimespan(int timespan, String username) {

        //1 - today, 7 - week, 30 - month
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = Calendar.getInstance().getTime();
        //Date currentDate = .format(Calendar.getInstance().getTime());

        ArrayList<MapObject> usersMarkers = getFriendsMapObjects(username);
        ArrayList<MapObject> list = new ArrayList<>();

        for(int i = 0; i< usersMarkers.size(); i++)
        {
            MapObject mo = usersMarkers.get(i);
            Date objectDate;
            try {
                objectDate = format.parse(mo.date);
            } catch (Exception e) {
                break;
            }

            long difference = currentDate.getTime() - objectDate.getTime();
            long dayDifference = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

            if (dayDifference < timespan) {
                list.add(usersMarkers.get(i));
            }
        }

        return list;
    }


    MapObjectData.ListUpdatedEventListener updateListener;
    public void setEventListener(MapObjectData.ListUpdatedEventListener listener) {
        updateListener = listener;
    }
    public interface ListUpdatedEventListener {
        void onListUpdated();
    }

    MapObjectAddedListener liistener;
    public void setNewObejctListener (MapObjectAddedListener lis)
    {
        this.liistener = lis;
    }
    public interface MapObjectAddedListener{
        void onMapObejctAdded(MapObject obj);
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
            String myMapObjectKey = dataSnapshot.getKey();

            if (!MapObjectsMapping.containsKey(myMapObjectKey)) {
                MapObject myMapObject = dataSnapshot.getValue(MapObject.class);
                myMapObject.key = myMapObjectKey;
                MapObjects.add(myMapObject);
                MapObjectsMapping.put(myMapObjectKey, MapObjects.size() - 1);
                if (updateListener != null)
                    updateListener.onListUpdated();

                if(liistener != null)
                {
                    liistener.onMapObejctAdded(myMapObject);
                }
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
