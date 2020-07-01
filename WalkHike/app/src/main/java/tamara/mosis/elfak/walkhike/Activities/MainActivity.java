package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.location.Location;
import android.location.LocationManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.NotificationService;
import tamara.mosis.elfak.walkhike.Probe;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.ShowArObjectActivity;

import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.Position;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, View.OnClickListener, 
        BottomNavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener {

    private FirebaseAuth mfirebaseAuth;
    private static final int PERMISSION_CODE = 1;
    Location location;
    LocationManager locationManager;
    private GoogleMap map;
    boolean isGpsProvider;
    boolean isNetworkProvider;
    GoogleApiClient googleApiClient;

    BottomNavigationView bottom_navigation_menu;
    FloatingActionButton addNewFloating;
    FloatingActionButton objectInteraction;
    FloatingActionButton startServiceButton;
    Toolbar toolbar;
    MenuItem profileItem;

    boolean startedService = false;
    UserData userData;
    FriendshipData friendshipData;

    LinearLayout info_window_container;
    ImageView info_window_icon;
    TextView info_window_username;
    TextView info_window_lat;
    TextView info_window_lon;
    TextView info_window_see_details;

    Marker lastSelected;

    Marker userMarker;

    User loggedUser;

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_main);
        mfirebaseAuth=FirebaseAuth.getInstance();
        userData.getInstance().getUsers();
        friendshipData.getInstance().getFriendships();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        } else {
            //map.setMyLocationEnabled(true);
        }

        getDeviceLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map_fragment);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        addNewFloating = (FloatingActionButton) findViewById(R.id.main_addnewObject);
        addNewFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info_window_container.setVisibility(View.GONE);

                Intent intent=new Intent(getApplicationContext(), AddNewObjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", location.getLatitude());
                bundle.putDouble("lon", location.getLongitude());
                bundle.putSerializable("user", loggedUser);
                intent.putExtras(bundle);

                startActivityForResult(intent, 1);
            }
        });
        objectInteraction = (FloatingActionButton) findViewById(R.id.main_showArObject);
        objectInteraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ShowArObjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("object_id", 3); //1 za trophy, 2 za emoji, 3 za marker
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        startServiceButton = findViewById(R.id.main_startService);
        startServiceButton.setOnClickListener(this);

        bottom_navigation_menu = findViewById(R.id.bottom_navigation_menu);
        bottom_navigation_menu.setSelectedItemId(R.id.map);
        bottom_navigation_menu.setOnNavigationItemSelectedListener(this);

        info_window_container = findViewById(R.id.info_window_container);
        info_window_container.setVisibility(View.GONE);

        info_window_icon = findViewById(R.id.info_window_icon);
        info_window_username = findViewById(R.id.info_window_username);
        info_window_lat = findViewById(R.id.info_window_lat);
        info_window_lon = findViewById(R.id.info_window_lon);
        info_window_see_details = findViewById(R.id.info_window_see_details);
        info_window_see_details.setOnClickListener(this);

        lastSelected = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            //vracanje iz AddNewObjectActivity-ja
            if (resultCode == RESULT_OK) {
                MapObject result = (MapObject) data.getSerializableExtra("map_object");
                AddMarkerObject(result);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void AddMarkerObject(MapObject mapObject) {

        double lat, lon;

        Position position = mapObject.position;
        lat = Double.parseDouble(position.latitude);
        lon = Double.parseDouble(position.longitude);

        LatLng loc = new LatLng(lat, lon);

        int objectType = mapObject.objectType;
        int resId = 0;

        if (objectType == 1) {
            resId = R.drawable.ic_message;
        } else if (objectType == 2) {
            resId = R.drawable.ic_marker;
        } else if (objectType == 3) {
            resId = R.drawable.ic_photo;
        } else if (objectType == 4) {
            resId = R.drawable.ic_heart;
        }

        if(map!=null){
            Marker m = map.addMarker(new MarkerOptions()
                    .position(loc)
                    .icon(BitmapDescriptorFactory.fromResource(resId))
            );

            m.setTag(mapObject);
            map.moveCamera(CameraUpdateFactory.newLatLng(loc));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,10f));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_profile, menu);


        FirebaseUser currentUser=mfirebaseAuth.getCurrentUser();
        if(currentUser==null)
        {
            Intent loginIntent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }


//da li nam treba ovaj deo?
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);



        Toast.makeText(getApplicationContext(), "Welcome " + username + ", " + email + "!", Toast.LENGTH_SHORT).show();

        if(indexx != -1) {
            loggedUser = userData.getInstance().getUser(indexx);
            if (loggedUser.email.compareTo(email) == 0)
                Toast.makeText(getApplicationContext(), "Welcome " + username + ", " + email + "!", Toast.LENGTH_SHORT).show();
        }

        /*User u;
        if(indexx != -1) {
            u = userData.getInstance().getUser(indexx);
            if(u.email.compareTo(email) == 0)
                Toast.makeText(getApplicationContext(), "Welcome " + username + ", " + email + "!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "There was a problem " + username + ", " + email + "!", Toast.LENGTH_SHORT).show();
        }*/
        return super.onCreateOptionsMenu(menu);
        //return false;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.main_menu_probe_item)
        {
            Intent intent=new Intent(getApplicationContext(), Probe.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.main_menu_profile_item) {

            Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
            //intent.putExtra("username",);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //map = googleMap;


        //if you need to diable rotation
        //googleMap.getUiSettings().setRotateGesturesEnabled(false);
        //if you need to disable zooming
        //googleMap.getUiSettings().setZoomGesturesEnabled(false);



        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.map=googleMap;

        map.setOnMarkerClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                } else {
                    return;
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
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        location=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location!=null){

            Toast.makeText(MainActivity.this, "Lat : "+location.getLatitude()+" Lng "+location.getLongitude(), Toast.LENGTH_SHORT).show();
            if(map!=null){
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                //map.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
               map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
               map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
                //Toast.makeText(this, "Usao u latlong", Toast.LENGTH_SHORT );
            }
        }
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //10 sec
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,MainActivity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {



        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onLocationChanged(Location location) {
    //Toast.makeText(MapWithPlayServiceLocationActivity.this, "Lat : "+location.getLatitude()+" Lng "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        if(map!=null){
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

            this.location = location;

            /*Marker m1 = map.addMarker(new MarkerOptions().position(latLng).alpha(0.5f));
            Marker m = map.addMarker(new MarkerOptions().position(latLng));
            m.setDraggable(true);
            m.setPosition(new LatLng(location.getLatitude() + 20, location.getLongitude() + 20));*/

            //map.clear();
            //map.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
            //map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notifications: {
                Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            case R.id.friends: {
                Intent intent = new Intent(getApplicationContext(), FriendslistActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            case R.id.completed_routes: {
                Intent intent = new Intent(getApplicationContext(), CompletedRoutesActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            case R.id.leaderboard: {
                Intent intent = new Intent(getApplicationContext(), LeaderboardsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
		//finish everywhere
        }
        return false;
	}		

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.main_startService)
        {

            if(!startedService) {
                Toast.makeText(getApplicationContext(), "Start service", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), NotificationService.class);
                i.putExtra("timer", 10);
                startService(i);
                startedService = true;
            }
            else {
                Toast.makeText(getApplicationContext(), "Stop service", Toast.LENGTH_SHORT).show();
                v.setActivated(false);

                Intent i = new Intent(getApplicationContext(), NotificationService.class);

                stopService(i);
                startedService = false;
            }

        } else if (v.getId() == R.id.info_window_see_details) {

            MapObject objectTag = (MapObject) v.getTag();

            String lat = objectTag.position.latitude;
            String lon = objectTag.position.longitude;

            Toast.makeText(this, "lat: " + lat + " lon: " + lon, Toast.LENGTH_SHORT).show();

        }
       
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (lastSelected == null || !lastSelected.equals(marker)) {

            info_window_container.setVisibility(View.VISIBLE);

            MapObject objectTag = (MapObject) marker.getTag();

            String lat = objectTag.position.latitude;
            String lon = objectTag.position.longitude;

            int objectType = objectTag.objectType;

            if (objectType == 1) {
                info_window_icon.setImageResource(R.drawable.ic_message);
            } else if (objectType == 2) {
                info_window_icon.setImageResource(R.drawable.ic_marker);
            } else if (objectType == 3) {
                info_window_icon.setImageResource(R.drawable.ic_photo);
            } else if (objectType == 4) {
                info_window_icon.setImageResource(R.drawable.ic_heart);
            }

            info_window_username.setText(String.format("Left on %s by\n%s", objectTag.date, objectTag.createdBy.username));
            info_window_lat.setText("lat: " + lat);
            info_window_lon.setText("lon: " + lon);
            info_window_see_details.setTag(objectTag);

            lastSelected = marker;
        } else {
            info_window_container.setVisibility(View.GONE);
            lastSelected = null;
        }

        return false;
    }
}
