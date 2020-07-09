package tamara.mosis.elfak.walkhike;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.protobuf.DescriptorProtos;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import tamara.mosis.elfak.walkhike.Activities.FriendRequestsActivity;
import tamara.mosis.elfak.walkhike.Activities.FriendslistActivity;
import tamara.mosis.elfak.walkhike.Activities.MainActivity;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;
import tamara.mosis.elfak.walkhike.modeldata.Position;
import tamara.mosis.elfak.walkhike.modeldata.PositionsData;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class NotificationService extends IntentService implements MapObjectData.MapObjectAddedListener,FriendshipData.NewItemListUpdatedEventListener,
        ScoresData.ListUpdatedEventListener,    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener , UserData.onUpdateUserListener{

    boolean running = false;
    int timeee = 5;
    String promena = "";
    LocationManager locationManager;
    boolean isGpsProvider;
    boolean isNetworkProvider;
    GoogleApiClient googleApiClient;


    User LoggedUser;
    Scores scoreLoggedUser;
    Location location;
    LatLng latLng;
    LatLng lastPos;


    int index;
    int numberOfFriendNotis;
    int numberOfObjectNotis;

    float meters;


    MapObjectData MoD;
    PositionsData PD;
    FriendshipData friendshipData;
    UserData userData;
    ScoresData scoresData;

    private ArrayList<Position> positions;
    private boolean[] poossss;
    private ArrayList<Position> DonePositions;
    ArrayList<String> friendsUsers;


    private ArrayList<MapObject> objects;
    private ArrayList<String> doneObjects;

    private ArrayList<User> users;
    private ArrayList<String> doneUsers;

    private NotificationManager mNotificationManager;




    public NotificationService()
    {
        super("Noti service");
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId){
        super.onStartCommand(i, flags, startId);
        PD.getInstance().getPositions();
        return START_STICKY;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
        latLng = new LatLng(1, 1);
        MoD.getInstance().setNewObejctListener(this);
        friendshipData.getInstance().setNewItemEventListener(this);
        scoresData.getInstance().setEventListener(this);
        userData.getInstance().setUserPostListener(this);

        Log.v("timer", "service started");
        meters = 0;

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        int indexx = sharedPref.getInt(getString(R.string.loggedUser_index), -1);

        SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences(
                getString(R.string.NotificationsFriend), Context.MODE_PRIVATE);
        int numOfNotis = sharedPref1.getInt(getString(R.string.NotificationsNumber), 0);
        numberOfFriendNotis = numOfNotis;



        SharedPreferences sharedPref2 = getApplicationContext().getSharedPreferences(
                getString(R.string.NotiObjects), Context.MODE_PRIVATE);
        numOfNotis = sharedPref2.getInt(getString(R.string.NotiObjectsNumber), 0);
        numberOfObjectNotis = numOfNotis;



        LoggedUser = new User();
        LoggedUser.username = username;
        LoggedUser.email = email;

        positions = PD.getInstance().getPositions();
        users = friendshipData.getInstance().GetUserFriends(email);
        doneUsers = new ArrayList<>();
        //doneUsers.add(email);
        //DonePositions = new ArrayList<>();

        objects = MapObjectData.getInstance().getFriendsMapObjects(username);
        doneObjects = new ArrayList<>();

        numberOfObjectNotis = 100;

        ArrayList<User> friendsUserss = FriendshipData.getInstance().GetUserFriends(LoggedUser.email);

       friendsUsers = new ArrayList<>();

        for (int i = 0; i < friendsUserss.size(); i++) {
            friendsUsers.add(friendsUserss.get(i).username);
        }

    }

    void writeObjectInSharedPrefs()  //treba da se preradi da radi za MApObject a ne Pos tj friend
    {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.NotificationsFriend), Context.MODE_PRIVATE);
        int numOfNotis = sharedPref.getInt(getString(R.string.NotificationsNumber), 0);
        if(numberOfFriendNotis == numOfNotis + 1) {


            SharedPreferences.Editor editor = sharedPref.edit();


           // editor.putString(getString(R.string.NotificationsFromUser) + numOfNotis, newF.fromUser.email);
            editor.putString(getString(R.string.NotificationsDate) + numOfNotis, Calendar.getInstance().getTime().toString());

            numOfNotis++;
            editor.putInt(getString(R.string.NotificationsNumber), numOfNotis);

            editor.commit();
        }
        if(myObjectListener != null)
            notifyObjectNoti();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        MoD.getInstance().setNewObejctListener(null);
        friendshipData.getInstance().setNewItemEventListener(null);
        scoresData.getInstance().setEventListener(null);

        userData.getInstance().setUserPostListener(null);
        Log.v("timer", "service destroyed");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int time = 0;//intent.getIntExtra("timer", 5);
        getDeviceLocation();

        while(running)
        {

            time++;
            try
            {
                Thread.sleep(5000);
            }catch (Exception e)
            {

            }
        }
    }

    private void getDeviceLocation() {
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGpsProvider && !isNetworkProvider) {
            //showing setting for enable gps

            return;

        } else {

            GetLocationData();
        }
    }

    private void GetLocationData() {
        googleApiClient = new GoogleApiClient.Builder(NotificationService.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(NotificationService.this)
                .addOnConnectionFailedListener(NotificationService.this)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        location=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location!=null){


            latLng=new LatLng(location.getLatitude(),location.getLongitude());
            Position p = new Position();
            p.latitude = "" + latLng.latitude;
            p.longitude = "" + latLng.longitude;
            p.desc = "User is here: ";
            userData.getInstance().updateUserPosition(LoggedUser.email, p);
                //Toast.makeText(this, "Usao u latlong", Toast.LENGTH_SHORT );


            findNearbyUsers();

           // findNearbyObjects();

        }
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //10 sec
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,NotificationService.this);
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(running) {
            lastPos =latLng;
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Position p = new Position();
            p.latitude = "" + latLng.latitude;
            p.longitude = "" + latLng.longitude;
            p.desc = "User is here: ";
            promena = p.toString();



            float dist[] = new float[5];
            Location.distanceBetween(latLng.latitude, latLng.longitude, lastPos.latitude, lastPos.longitude, dist);
           // dist[0] *= 0.000621371192f;

            meters +=dist[0];
            if(meters > 1) {
                userData.getInstance().updateUserPosition(LoggedUser.email, p);
                scoresData.getInstance().updateScoreDistance((int)meters, LoggedUser);
                meters = 0;

                findNearbyUsers();

                findNearbyObjects();
            }


            //findNearbyUsers();
            //PD.getInstance().updatePlace(1, p.desc, p.longitude, p.latitude);
        }
    }





    @Override
    public void onListUpdatedNewFriends(Friendship newF) {

        if(LoggedUser.email.compareTo(newF.toUser.email) == 0 && newF.accepted == false)
        {
            numberOfFriendNotis++;


            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.NotificationsFriend), Context.MODE_PRIVATE);
            int numOfNotis = sharedPref.getInt(getString(R.string.NotificationsNumber), 0);
            if(numberOfFriendNotis == numOfNotis + 1) {


                SharedPreferences.Editor editor = sharedPref.edit();


                editor.putString(getString(R.string.NotificationsFromUser) + numOfNotis, newF.fromUser.email);
                editor.putString(getString(R.string.NotificationsDate) + numOfNotis, Calendar.getInstance().getTime().toString());

                numOfNotis++;
                editor.putInt(getString(R.string.NotificationsNumber), numOfNotis);

                editor.commit();

                SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                Boolean sound = sharedPref1.getBoolean(getString(R.string.loggedUser_sound), true);
                Boolean notii = sharedPref1.getBoolean(getString(R.string.loggedUser_notifications), true);


                if(notii) {
                    sendNotif("notify_" + "newfriend", "walkhikwe_friends", 200, "Someone wants to be your friend",
                            "Request", "friend request", "friend request",
                            new Intent(getApplicationContext(), FriendRequestsActivity.class), sound);
                }


                if(myListener != null)
                    doWork();
            }

        }
        else if(LoggedUser.email.compareTo(newF.fromUser.email) == 0 && newF.accepted == false)
        {
            users.add(userData.getInstance().getUser(newF.toUser.email));
        }
        else if(LoggedUser.email.compareTo(newF.toUser.email) == 0  && newF.accepted == false)
        {
            users.add(userData.getInstance().getUser(newF.fromUser.email));
        }
    }

    private void findNearbyUsers() {
        if(users.size() != 0) {
            boolean close = false;

            for (int i = 0; i < users.size(); i++) {

                close = false;
                float res[] = new float[5];
                Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(users.get(i).UserPosition.latitude),
                        Double.parseDouble(users.get(i).UserPosition.longitude), res);

                for(int j = 0; j< doneUsers.size(); j++) {
                    if(users.get(i).email.compareTo(doneUsers.get(j)) == 0)
                    {
                        close = true;
                        if (res[0] > 1000) {
                            doneUsers.remove(j);
                        }
                    }
                }
                if(!close && res[0] < 1000)
                {


                    index = i;
                    doneUsers.add(users.get(i).email);

                    Log.v("Position", users.get(i).UserPosition.toString() + " !");


                    SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                    Boolean sound = sharedPref1.getBoolean(getString(R.string.loggedUser_sound), true);
                    Boolean notii = sharedPref1.getBoolean(getString(R.string.loggedUser_notifications), true);
                    if(notii) {
                        sendNotif("notify_" + users.get(i).username, "usersnear", 750, "User " + users.get(i).username + " near you!",
                                "User " + users.get(i).username + " near you!", "Near you", "User " + users.get(i).username + " near you!",
                                new Intent(getApplicationContext(), MainActivity.class), sound);
                    }

                   // numberOfObjectNotis++;
                    //users.remove(i);

                }
            }
        }
    }

    private void findNearbyObjects() {
        if(objects.size() != 0) {
            boolean close = false;

            for (int i = 0; i < objects.size(); i++) {

                MapObject mo = objects.get(i);
                if(  (mo.isPublic && friendsUsers.contains(mo.createdBy)) ||
                        (!mo.isPublic && mo.sharedWith.compareTo(LoggedUser.username) == 0))
                    {
                        close = false;
                        float res[] = new float[5];
                        Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(objects.get(i).position.latitude),
                                Double.parseDouble(objects.get(i).position.longitude), res);

                        for (int j = 0; j < doneObjects.size(); j++) {
                            if ((objects.get(i).datetime + objects.get(i).createdBy).compareTo(doneObjects.get(j)) == 0) {
                                close = true;
                                if (res[0] > 1000) {
                                    doneObjects.remove(j);
                                }
                            }
                        }
                        if (!close && res[0] < 1000) {
/////////////////////////////

                            index = i;
                            doneObjects.add(objects.get(i).datetime + objects.get(i).createdBy);

                            Log.v("MapObject", objects.get(i).datetime + " !");

                            Intent ii =  new Intent(getApplicationContext(), MainActivity.class);
                            ii.putExtra("objekaat", mo);

                            SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                            Boolean sound = sharedPref1.getBoolean(getString(R.string.loggedUser_sound), true);
                            Boolean notii = sharedPref1.getBoolean(getString(R.string.loggedUser_notifications), true);
                            if(notii) {
                                sendNotif("notify_" + objects.get(i), "objectsnear", 700, "Object near you!",
                                        "Object near you!", "Near you", "Object near you!",
                                        ii, sound);
                            }


                        }
                    }
                }
        }
    }

    private void findNearby()
    {

        int removea[] = new int[positions.size()];
        int n = 0;
        for(int i =0; i< positions.size(); i++)
        {

            float[] res = new float[10];
            Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(positions.get(i).latitude), Double.parseDouble(positions.get(i).longitude), res);
            res[0] *= 0.000621371192f;
            if (res[0] < 1000)//res[1] < 10 && res[2] <10)
            {

                index = i;
                removea[n] = i;
                n++;
                Log.v("Position" ,  positions.get(i).toString() + " !");

                SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                Boolean sound = sharedPref1.getBoolean(getString(R.string.loggedUser_sound), true);
                Boolean notii = sharedPref1.getBoolean(getString(R.string.loggedUser_notifications), true);

                if(notii) {
                    sendNotif("notify_", "walkhikw_usersnear", 100, "Positon near you!",
                            "Positon near you!", "Near you", "Positon near you!", new Intent(getApplicationContext(), MainActivity.class), sound);

                }

            }

        }

        for(int i =0; i< n; i++) {
            positions.remove(removea[i]);
        }

    }



    private void sendNotif(String chanelid, String andr8ID, int iid, String bigTextt, String bigTitle, String cTitle, String cText, Intent whereNext , boolean sound)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), chanelid);
        //Intent ii = ;
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext().getApplicationContext(), 0, whereNext, 0);


        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(bigTextt);
        bigText.setBigContentTitle(bigTitle);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(cTitle);
        mBuilder.setContentText(cText);
        mBuilder.setPriority(android.app.Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = andr8ID;
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "walkhike_"+andr8ID,
                    NotificationManager.IMPORTANCE_HIGH);


            //if(!sound)
              //  channel.setSound(null, null);


            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        else {
            //if(!sound)
               // mBuilder.setSound(null);
        }

        mNotificationManager.notify(iid, mBuilder.build());
    }






    @Override
    public void onScoreUpdated(Scores ss) {
        if(friendshipData.getInstance().areUsersFriedns(LoggedUser.email, ss.useer))
            Log.v("updateScore", "User " + ss.useer + " score " + ss.weeklyDistance );
    }

    @Override
    public void onMapObjectAdded(MapObject obj, boolean novi) {

       // numberOfObjectNotis++;
        Position p = obj.position;
        if(novi) {
            float res[] = new float[5];
            Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(obj.position.latitude), Double.parseDouble(obj.position.longitude), res);

            if(  (obj.isPublic && friendsUsers.contains(obj.createdBy)) ||
                    (!obj.isPublic && obj.sharedWith.compareTo(LoggedUser.username) == 0)) {
                if (res[0] < 1000) {

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                            getString(R.string.NotiObjects), Context.MODE_PRIVATE);
                    int numOfNotis = sharedPref.getInt(getString(R.string.NotiObjectsNumber), 0);

                    SharedPreferences.Editor editor = sharedPref.edit();


                    editor.putString(getString(R.string.NotiObjectsFromUser) + numOfNotis, obj.createdBy);
                    editor.putString(getString(R.string.NotiObjectsDate) + numOfNotis, Calendar.getInstance().getTime().toString());

                    numOfNotis++;
                    editor.putInt(getString(R.string.NotiObjectsNumber), numOfNotis);

                    editor.commit();

                    if (myObjectListener != null)
                        notifyObjectNoti();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("objekaat", obj);

                    SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                    Boolean sound = sharedPref1.getBoolean(getString(R.string.loggedUser_sound), true);
                    Boolean notii = sharedPref1.getBoolean(getString(R.string.loggedUser_notifications), true);

                    if (notii) {
                        sendNotif("notify_" + "mapobjects", "walkhikw_mapobjects", 500, "Someone added an object near you!",
                                "Obejct near you!", "Near you", "Object near you!", i, sound);
                    }

                }
            }
        }
        else
        {
            if(obj.createdBy.compareTo(LoggedUser.username) == 0)
            {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                        getString(R.string.NotiObjects), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                int numOfNotis = sharedPref.getInt(getString(R.string.NotiObjectsNumber) + "reactions", 0);
                editor.putString(getString(R.string.NotiObjectsDate)+ "reactions" + numOfNotis, Calendar.getInstance().getTime().toString());
                numOfNotis++;

                editor.putInt(getString(R.string.NotiObjectsNumber)+ "reactions", numOfNotis);

                editor.commit();

                Intent i =  new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("objekaat", obj);


                SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                Boolean sound = sharedPref1.getBoolean(getString(R.string.loggedUser_sound), true);
                Boolean notii = sharedPref1.getBoolean(getString(R.string.loggedUser_notifications), true);

                if(notii) {
                    sendNotif("notify_" + "mapobjects", "walkhikw_mapobjects", 600, "Someone reacted to your object!",
                            "Someone reacted to your object!", "Someone reacted to your object!", "Someone reacted to your object!", i, sound);
                }

            }
        }
    }








    private static ListenerForFriendNotis  myListener;

    public static void setUpListenerFriendNoti(ListenerForFriendNotis Listener) {
        myListener = Listener;
    }

    public void doWork() { //View view
        myListener.onNewFriendship();
    }



    public interface ListenerForFriendNotis{
        void onNewFriendship();
    }


    private static ListenerForObjectNotis  myObjectListener;

    public static void setUpListenerObjectNoti(ListenerForObjectNotis Listener) {
        myObjectListener = Listener;
    }



    public void notifyObjectNoti()
    { //View view
        if(myObjectListener != null)
            myObjectListener.onNewObject();
    }

    public interface ListenerForObjectNotis{
        void onNewObject();
    }






    @Override
    public void onUserUpdated() {
        findNearbyUsers();
    }









}


