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

import tamara.mosis.elfak.walkhike.Activities.FriendRequestsActivity;
import tamara.mosis.elfak.walkhike.Activities.FriendslistActivity;
import tamara.mosis.elfak.walkhike.Activities.MainActivity;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;
import tamara.mosis.elfak.walkhike.modeldata.Position;
import tamara.mosis.elfak.walkhike.modeldata.PositionsData;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class NotificationService extends IntentService implements MapObjectData.ListUpdatedEventListener,FriendshipData.NewItemListUpdatedEventListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    boolean running = false;
    int timeee = 5;
    String promena = "";
    LocationManager locationManager;
    boolean isGpsProvider;
    boolean isNetworkProvider;
    GoogleApiClient googleApiClient;


    User LoggedUser;
    Location location;
    LatLng latLng;
    LatLng lastPos;


    int index;
    int numberOfFriendNotis;
    int numberOfObjectNotis;


    MapObjectData MoD;
    PositionsData PD;
    FriendshipData friendshipData;
    UserData userData;
    ScoresData scoresData;

    private ArrayList<Position> positions;
    private ArrayList<Position> DonePositions;


    private ArrayList<MapObject> objects;

    private ArrayList<User> users;

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
        MoD.getInstance().setEventListener(this);
        friendshipData.getInstance().setNewItemEventListener(this);

        Log.v("timer", "service started");


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        int indexx = sharedPref.getInt(getString(R.string.loggedUser_index), -1);

        SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences(
                getString(R.string.NotificationsFriend), Context.MODE_PRIVATE);
        int numOfNotis = sharedPref1.getInt(getString(R.string.NotificationsNumber), 0);
        numberOfFriendNotis = numOfNotis;

        LoggedUser = new User();
        LoggedUser.username = username;
        LoggedUser.email = email;

        positions = PD.getInstance().getPositions();
        users = userData.getInstance().getUsers();
        //DonePositions = new ArrayList<>();

        numberOfObjectNotis = 100;
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
        Log.v("timer", "service destroyed");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int time = 0;//intent.getIntExtra("timer", 5);
        getDeviceLocation();

        while(running)
        {

            time++;
            CheckFriendNotis();
            try
            {
                Thread.sleep(5000);
            }catch (Exception e)
            {

            }
        }
    }
    void CheckFriendNotis(){



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
                //Toast.makeText(this, "Usao u latlong", Toast.LENGTH_SHORT );

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
    public void onListUpdated() {
        promena = "Stigla ";

        int sizee = MoD.getInstance().getMapObjects().size() - 1;
        MapObject m = MoD.getInstance().getMapObjects().get(sizee);
        promena += m.toString();

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
            int diiist = (int) dist[0];
            if(diiist > 0) {
                userData.getInstance().updateUserPosition(LoggedUser.email, p);
                scoresData.getInstance().updateScoreDistance(diiist, LoggedUser);
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




                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                Intent ii = new Intent(getApplicationContext(), FriendRequestsActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext().getApplicationContext(), 0, ii, 0);


                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText("Someone wants to be your friend");
                bigText.setBigContentTitle("New friend request");

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
                mBuilder.setContentTitle("Request");
                mBuilder.setContentText("friend request");
                mBuilder.setPriority(android.app.Notification.PRIORITY_MAX);
                mBuilder.setStyle(bigText);

                mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // === Removed some obsoletes
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    String channelId = "walkhikw_friends";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "walkhikw_friends",
                            NotificationManager.IMPORTANCE_HIGH);
                    mNotificationManager.createNotificationChannel(channel);
                    mBuilder.setChannelId(channelId);
                }

                mNotificationManager.notify(0, mBuilder.build());

                if(myListener != null)
                    doWork();
            }

        }
    }

    private void findNearbyUsers() {
        if(users.size() != 0) {
            int removea[] = new int[users.size()];
            int n = 0;
            for (int i = 0; i < users.size(); i++) {

                if(users.get(i).email.compareTo(LoggedUser.email) != 0) {
                    float[] res = new float[10];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(users.get(i).UserPosition.latitude),
                            Double.parseDouble(users.get(i).UserPosition.longitude), res);
                    res[0] *= 0.000621371192f;
                    if (res[0] < 1000)//res[1] < 10 && res[2] <10)
                    {

                        index = i;
                        removea[n] = i;

                        n++;

                        Log.v("Position", users.get(i).UserPosition.toString() + " !");


                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getApplicationContext(), "notify_"+ users.get(i).username);
                        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext().getApplicationContext(), 0, ii, 0);


                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                        bigText.bigText("User near you!");
                        bigText.setBigContentTitle("User " +users.get(i).username+ " near you!");

                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
                        mBuilder.setContentTitle("Near you");
                        mBuilder.setContentText("User " +users.get(i).username+ " near you!");
                        mBuilder.setPriority(android.app.Notification.PRIORITY_MAX);
                        mBuilder.setStyle(bigText);

                        mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        // === Removed some obsoletes
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            String channelId = "walkhikw_usersnear";
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "walkhikw_usersnear",
                                    NotificationManager.IMPORTANCE_HIGH);
                            mNotificationManager.createNotificationChannel(channel);
                            mBuilder.setChannelId(channelId);
                        }

                        mNotificationManager.notify(numberOfObjectNotis, mBuilder.build());
                        numberOfObjectNotis++;
                        users.remove(i);
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





                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), "notify_");
                Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext().getApplicationContext(), 0, ii, 0);


                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText("Positon near you!");
                bigText.setBigContentTitle("Positon near you!");

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
                mBuilder.setContentTitle("Near you");
                mBuilder.setContentText("Positon near you!");
                mBuilder.setPriority(android.app.Notification.PRIORITY_MAX);
                mBuilder.setStyle(bigText);

                mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // === Removed some obsoletes
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    String channelId = "walkhikw_usersnear";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "walkhikw_usersnear",
                            NotificationManager.IMPORTANCE_HIGH);
                    mNotificationManager.createNotificationChannel(channel);
                    mBuilder.setChannelId(channelId);
                }

                mNotificationManager.notify(100, mBuilder.build());

            }

        }

        for(int i =0; i< n; i++) {
            positions.remove(removea[i]);
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

    public void notifyObjectNoti() { //View view
        myListener.onNewFriendship();
    }

    public interface ListenerForObjectNotis{
        void onNewFriendship();
    }
















}


