package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.location.Location;
import android.location.LocationManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import tamara.mosis.elfak.walkhike.GroupsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.NotificationService;
import tamara.mosis.elfak.walkhike.Probe;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.SearchResultsListAdapter;
import tamara.mosis.elfak.walkhike.ShowArObjectActivity;

import tamara.mosis.elfak.walkhike.UsersRecyclerAdapter;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;

import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;

import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;

import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.Position;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, TextWatcher {

    private FirebaseAuth mfirebaseAuth;
    private static final int PERMISSION_CODE = 1;

    public Location getLocation() {
        return location;
    }

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

    ScoresData scoresData;

    MapObjectData mapObjectData;
    ArrayList<MapObject> mapObjects;

    //filter
    boolean hide_users;
    boolean filter_opened;
    boolean filter_objects_opened;
    boolean filter_timespan_opened;
    boolean filter_by_distance_opened;
    boolean info_groups_opened;

    byte object_filter;
    int timespan;
    int radius;

    ImageView filter_radius;
    ImageView filter_users;
    ImageView filter_icon;
    RelativeLayout layout_filter_by_distance;
    RelativeLayout layout_filter_options;
    LinearLayout layout_filter_object_type;
    LinearLayout layout_filter_timespan;
    ImageView filter_object_type;
    ImageView filter_timespan;
    ImageView filter_today;
    ImageView filter_one_week;
    ImageView filter_one_month;
    ImageView remove_timespan_filter;
    ImageView filter_checkpoint;
    ImageView filter_emoji;
    ImageView filter_photo;
    ImageView filter_message;

    SeekBar filter_by_distance_seekbar;

    //search

    int search_filter_activated;
    int search_radius;

    SeekBar search_radius_seekBar;

    View search_fragment;
    ImageView search_close;
    EditText search_edit_text;
    Button search_users_only;
    Button search_messages_only;
    Button search_photos_only;
    Button search_checkpoints_only;
    Button search_emojis_only;
    Button search_remove_filters;


    FloatingActionButton info_add_group;

    ListView searchResultsListView;

    //info_window
    LinearLayout info_window_container;
    LinearLayout info_window_container_groups;

    ImageView info_window_icon;
    TextView info_window_username;
    TextView info_window_lat;
    TextView info_window_lon;
    ImageView info_window_delete_object;
    Button info_window_see_details;
    Button info_add_to_route;


    //mapObjects
    Marker lastSelected;
    Marker userMarker;

    Marker loggedUserMarker;
    ArrayList<Marker> usersMarkers;
    ArrayList<Marker> objectsMarkers;

    //logged user
    User loggedUser;
    String loggedUsername;

    //scores
    ArrayList<Scores> skorovi;



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

//        MapObjectData.getInstance().setDeleteEventListener(new MapObjectData.ObjectDeletedEventListener() {
//            @Override
//            public void onObjectDeleted() {
//                FilterMapObjects();
//            }
//        });

        MapObjectData.getInstance().setListUpdatedEventListener(new MapObjectData.ListUpdatedEventListener() {
            @Override
            public void onListUpdated() {
                FilterMapObjects();
            }
        });

        setContentView(R.layout.activity_main);
        mfirebaseAuth=FirebaseAuth.getInstance();
        ArrayList<User> users = userData.getInstance().getUsers();
        friendshipData.getInstance().getFriendships();
        mapObjects = MapObjectData.getInstance().getMapObjects();
        skorovi =  scoresData.getInstance().getScores();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        } else {
            //map.setMyLocationEnabled(true);
        }

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String emaill = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        String image = sharedPref.getString(getString(R.string.loggedUser_image), "EMPTY");
        Boolean sound=sharedPref.getBoolean(getString(R.string.loggedUser_sound),true);
        Boolean notifications=sharedPref.getBoolean(getString(R.string.loggedUser_notifications),true);
        Boolean locationSharing=sharedPref.getBoolean(getString(R.string.loggedUser_location_sharing),false);
        // Boolean darkTheme=sharedPref.getBoolean(getString(R.string.loggedUser_dark_theme),false);//ne treba nam jer imamo proveru u startu svakog act
        String unit=sharedPref.getString(getString(R.string.loggedUser_unit),"km");

        /////////
        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);
        skorovi = scoresData.getInstance().getScores();
        loggedUser = userData.getInstance().getUser(emaill);
       /*if(indexx != -1) {

            if (loggedUser.email.compareTo(emaill) == 0)
                Toast.makeText(getApplicationContext(), "Welcome " + username + ", " + emaill + "!", Toast.LENGTH_SHORT).show();
        }*/

        this.loggedUsername = username;

        getDeviceLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map_fragment);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        addNewFloating = (FloatingActionButton) findViewById(R.id.main_addnewObject);
        addNewFloating.setOnClickListener(this);

        objectInteraction = (FloatingActionButton) findViewById(R.id.main_showArObject);
        objectInteraction.setOnClickListener(this);

        startServiceButton = findViewById(R.id.main_startService);
        startServiceButton.setOnClickListener(this);


        //bottom_navigation_menu
        bottom_navigation_menu = findViewById(R.id.bottom_navigation_menu);
        bottom_navigation_menu.setSelectedItemId(R.id.map);
        bottom_navigation_menu.setOnNavigationItemSelectedListener(this);

        //filter
        hide_users = false;
        filter_opened = false;
        filter_objects_opened = false;
        filter_timespan_opened = false;
        filter_by_distance_opened = false;

        timespan = 0;
        radius = 100;
        object_filter = (byte) 0x0f;

        filter_users = findViewById(R.id.filter_users);
        filter_radius = findViewById(R.id.filter_by_distance_icon);
        filter_icon = findViewById(R.id.filter_icon);
        layout_filter_options = findViewById(R.id.layout_filter);
        layout_filter_by_distance = findViewById(R.id.filter_by_distance_layout);
        layout_filter_object_type = findViewById(R.id.layout_filter_object_type);
        layout_filter_timespan = findViewById(R.id.layout_filter_timespan);
        filter_object_type = findViewById(R.id.filter_object_type);
        filter_timespan = findViewById(R.id.filter_timespan);
        filter_today = findViewById(R.id.filter_today);
        filter_one_week = findViewById(R.id.filter_one_week);
        filter_one_month = findViewById(R.id.filter_one_month);
        remove_timespan_filter = findViewById(R.id.filter_remove_timespan);
        filter_checkpoint = findViewById(R.id.filter_checkpoint);
        filter_emoji = findViewById(R.id.filter_emoji);
        filter_photo = findViewById(R.id.filter_photo);
        filter_message = findViewById(R.id.filter_message);
        filter_by_distance_seekbar = findViewById(R.id.filter_by_distance_seekbar);

        filter_users.setOnClickListener(this);
        filter_radius.setOnClickListener(this);
        filter_icon.setOnClickListener(this);
        filter_object_type.setOnClickListener(this);
        filter_timespan.setOnClickListener(this);
        filter_today.setOnClickListener(this);
        filter_one_week.setOnClickListener(this);
        filter_one_month.setOnClickListener(this);
        remove_timespan_filter.setOnClickListener(this);
        filter_checkpoint.setOnClickListener(this);
        filter_emoji.setOnClickListener(this);
        filter_photo.setOnClickListener(this);
        filter_message.setOnClickListener(this);

        filter_by_distance_seekbar.setProgress(0);
        filter_by_distance_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (Math.abs(radius - (progress + 100)) > 250) {
                    radius = progress + 100;
                    Toast.makeText(MainActivity.this, "show objects from this radius: " + radius + "m", Toast.LENGTH_SHORT).show();

                    FilterMapObjects();
                    FilterUserObjects();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //search

        search_filter_activated = 0;
        search_radius = 100;

        search_radius_seekBar = findViewById(R.id.search_radius_seekbar);

        search_fragment = findViewById(R.id.search_fragment);
        search_close = findViewById(R.id.close_search);

        search_edit_text = findViewById(R.id.search_edit_text);
        search_users_only = findViewById(R.id.search_users_only);
        search_messages_only = findViewById(R.id.search_messages_only);
        search_photos_only = findViewById(R.id.search_photos_only);
        search_checkpoints_only = findViewById(R.id.search_checkpoints_only);
        search_emojis_only = findViewById(R.id.search_emojis_only);
        search_remove_filters = findViewById(R.id.search_remove_filters);

        searchResultsListView = (ListView) findViewById(R.id.search_results_list);

        search_close.setOnClickListener(this);
        search_users_only.setOnClickListener(this);
        search_messages_only.setOnClickListener(this);
        search_photos_only.setOnClickListener(this);
        search_checkpoints_only.setOnClickListener(this);
        search_emojis_only.setOnClickListener(this);
        search_remove_filters.setOnClickListener(this);

        search_radius_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                search_radius = progress + 100;
                Toast.makeText(MainActivity.this, "current radius: " + search_radius, Toast.LENGTH_SHORT).show();

                afterTextChanged(search_edit_text.getText());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        search_edit_text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){

                    Toast.makeText(MainActivity.this, "Enter pressed!", Toast.LENGTH_SHORT).show();

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(search_edit_text.getWindowToken(), 0);

                }

                return false;
            }
        });

        search_edit_text.addTextChangedListener(this);

        //info window:
        info_window_container = findViewById(R.id.info_window_container);
        info_window_container.setVisibility(View.GONE);

        info_window_container_groups = findViewById(R.id.info_window_container_groups);
        info_window_container_groups.setVisibility(View.GONE);

        info_window_icon = findViewById(R.id.info_window_icon);
        info_window_username = findViewById(R.id.info_window_username);
        info_window_lat = findViewById(R.id.info_window_lat);
        info_window_lon = findViewById(R.id.info_window_lon);
        info_window_delete_object = findViewById(R.id.info_delete_object);
        info_window_delete_object.setOnClickListener(this);
        info_window_see_details = findViewById(R.id.info_window_see_details);
        info_window_see_details.setOnClickListener(this);
        info_add_to_route = findViewById(R.id.info_add_to_savedroutes);
        info_add_to_route.setOnClickListener(this);
        info_add_group = findViewById(R.id.main_info_addgroup);
        info_add_group.setOnClickListener(this);
        info_groups_opened = false;
        lastSelected = null;

        usersMarkers = new ArrayList<>();
        objectsMarkers = new ArrayList<>();

        /*if(!startedService) {
            Toast.makeText(getApplicationContext(), "Start service", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getApplicationContext(), NotificationService.class);
            i.putExtra("timer", 10);
            startService(i);
            startedService = true;
        }*/

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

    protected void AddUserMarker(String username) {

        User user = UserData.getInstance().getUserByUsername(username);

        LatLng loc = new LatLng(Double.parseDouble(user.UserPosition.latitude),
                Double.parseDouble(user.UserPosition.longitude));

        String profilePhotoUri = user.image;

        if (!profilePhotoUri.equals("")) {

            Glide.with(this).asBitmap().load(profilePhotoUri).into(new SimpleTarget<Bitmap>(128, 128) {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    Marker m = map.addMarker(new MarkerOptions()
                            .position(loc)
                            .icon(BitmapDescriptorFactory.fromBitmap(resource)));

                    m.setTag(user.username); //////

                    if (user.username.compareTo(loggedUsername) != 0)
                        usersMarkers.add(m);
                    else
                        userMarker = m;

                }
            });

        } else {
            Marker m = map.addMarker(new MarkerOptions()
                    .position(loc)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user)));

            m.setTag(user.username);

            if (user.desc.compareTo(loggedUsername) != 0)
                usersMarkers.add(m);
            else
                userMarker = m;
        }

    }


    protected void AddMarkerObject(MapObject mapObject) {

        int objectType = mapObject.objectType;

        double lat, lon;

        Position position = mapObject.position;
        lat = Double.parseDouble(position.latitude);
        lon = Double.parseDouble(position.longitude);

        LatLng loc = new LatLng(lat, lon);

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

        Marker m = map.addMarker(new MarkerOptions()
                .position(loc)
                .icon(BitmapDescriptorFactory.fromResource(resId))
        );
        m.setTag(mapObject);

        objectsMarkers.add(m);

        //map.moveCamera(CameraUpdateFactory.newLatLng(loc));
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,10f));

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
        /*SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);

        skorovi = scoresData.getInstance().getScores();


        //Toast.makeText(getApplicationContext(), "Welcome " + username + ", " + email + "!", Toast.LENGTH_SHORT).show();

        if(indexx != -1) {
            loggedUser = userData.getInstance().getUser(indexx);
            if (loggedUser.email.compareTo(email) == 0)
                Toast.makeText(getApplicationContext(), "Welcome " + username + ", " + email + "!", Toast.LENGTH_SHORT).show();
        }*/

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
        } else if(item.getItemId() == R.id.main_menu_search_item) {

            Toast.makeText(this, "Search objects here!", Toast.LENGTH_SHORT).show();

            addNewFloating.setVisibility(View.GONE);
            objectInteraction.setVisibility(View.GONE);
            startServiceButton.setVisibility(View.GONE);
            bottom_navigation_menu.setVisibility(View.GONE);

            search_fragment.setVisibility(View.VISIBLE);

            search_users_only.setSelected(false);
            search_messages_only.setSelected(false);
            search_checkpoints_only.setSelected(false);
            search_emojis_only.setSelected(false);
            search_photos_only.setSelected(false);
            search_remove_filters.setSelected(false);
            search_edit_text.setText("");

            search_filter_activated = 0;
            search_radius_seekBar.setProgress(0);
            search_radius = 100;

            search_edit_text.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(search_edit_text, InputMethodManager.SHOW_IMPLICIT);
            }

            afterTextChanged(search_edit_text.getText());

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

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                info_window_container.setVisibility(View.GONE);
                layout_filter_object_type.setVisibility(View.GONE);
                filter_objects_opened = false;
                layout_filter_by_distance.setVisibility(View.GONE);
                filter_by_distance_opened = false;
                layout_filter_timespan.setVisibility(View.GONE);
                filter_timespan_opened = false;
                layout_filter_options.setVisibility(View.GONE);
                filter_opened = false;
            }
        });

        FilterMapObjects();
        FilterUserObjects();

        AddUserMarker(loggedUsername);

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
               map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20f));
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

            //map.clear();
            //map.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
            //map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));

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

        }
        else if (v.getId() == R.id.info_window_see_details) {

            info_window_container.setVisibility(View.GONE);
            if (v.getTag() instanceof MapObject) {

                MapObject objectTag = (MapObject) v.getTag();

                if (objectTag.objectType == 1 || objectTag.objectType == 3) {
                    Intent intent = new Intent(MainActivity.this, ObjectInteractionActivity.class);
                    intent.putExtra("object", objectTag);
                    intent.putExtra("username", loggedUsername);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, ARObjectInteractionActivity.class);
                    intent.putExtra("object", objectTag);
                    intent.putExtra("username", loggedUsername);
                    startActivity(intent);
                }

            } else {

                String username = (String) v.getTag();

                Intent intent = new Intent(MainActivity.this, FriendProfileActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);

            }


        } else if (v.getId() == R.id.info_delete_object) {

            //dialog box?

            AlertDialog.Builder areYouSure = new AlertDialog.Builder(MainActivity.this);

            areYouSure.setTitle("Delete object?");
            areYouSure.setMessage("Are you sure you want to delete this object?");

            areYouSure.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            areYouSure.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    info_window_container.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Deleting object", Toast.LENGTH_SHORT).show();
                    MapObjectData.getInstance().deleteMapObject((MapObject) v.getTag());


                }
            });

            AlertDialog dialog = areYouSure.create();
            dialog.show();

        } else if (v.getId() == R.id.main_addnewObject) {

            info_window_container.setVisibility(View.GONE);

            Intent intent=new Intent(getApplicationContext(), AddNewObjectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("lat", location.getLatitude());
            bundle.putDouble("lon", location.getLongitude());
            bundle.putSerializable("user", loggedUser);
            intent.putExtras(bundle);

            startActivityForResult(intent, 1);

        } else if (v.getId() == R.id.main_showArObject) {

            Intent intent=new Intent(getApplicationContext(), ShowArObjectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("object_id", 3); //1 za trophy, 2 za emoji, 3 za marker
            intent.putExtras(bundle);
            startActivity(intent);

        } else if (v.getId() == R.id.filter_users) {

            if (!hide_users) {

                Toast.makeText(this, "Hide friends", Toast.LENGTH_SHORT).show();

                HideUsers();

                hide_users = true;
                filter_users.setImageResource(R.drawable.ic_user);

            } else {

                Toast.makeText(this, "Show friends", Toast.LENGTH_SHORT).show();

                FilterUserObjects();

                hide_users = false;
                filter_users.setImageResource(R.drawable.ic_friends);
            }

        } else if (v.getId() == R.id.filter_by_distance_icon) {

            if (filter_by_distance_opened) {
                Toast.makeText(this, "Hide filter by distance options!", Toast.LENGTH_SHORT).show();
                layout_filter_by_distance.setVisibility(View.GONE);
                filter_by_distance_opened = false;
            } else {
                Toast.makeText(this, "Show filter by distance options!", Toast.LENGTH_SHORT).show();
                layout_filter_by_distance.setVisibility(View.VISIBLE);
                layout_filter_options.setVisibility(View.GONE);
                layout_filter_timespan.setVisibility(View.GONE);
                layout_filter_object_type.setVisibility(View.GONE);
                filter_by_distance_opened = true;
                filter_timespan_opened = false;
                filter_objects_opened = false;
                filter_opened = false;
                //open filter, set seekbar progress to current radius
            }

        } else if (v.getId() == R.id.filter_icon) {

            if (filter_opened) {
                Toast.makeText(this, "Hide filter options!", Toast.LENGTH_SHORT).show();
                layout_filter_options.setVisibility(View.GONE);
                layout_filter_object_type.setVisibility(View.GONE);
                layout_filter_timespan.setVisibility(View.GONE);
                filter_opened = false;
                filter_objects_opened = false;
                filter_timespan_opened = false;
            } else {
                Toast.makeText(this, "Show filter options!", Toast.LENGTH_SHORT).show();
                layout_filter_options.setVisibility(View.VISIBLE);
                layout_filter_by_distance.setVisibility(View.GONE);
                filter_opened = true;
                filter_by_distance_opened = false;
            }

        } else if (v.getId() == R.id.filter_object_type) {

            if (filter_objects_opened)
            {
                Toast.makeText(this, "Hide object filters!", Toast.LENGTH_SHORT).show();
                layout_filter_object_type.setVisibility(View.GONE);
                filter_objects_opened = false;
            } else {
                Toast.makeText(this, "Show object filters!", Toast.LENGTH_SHORT).show();
                layout_filter_object_type.setVisibility(View.VISIBLE);
                layout_filter_timespan.setVisibility(View.GONE);
                filter_timespan_opened = false;
                filter_objects_opened = true;
            }

        } else if (v.getId() == R.id.filter_timespan) {

            if (filter_timespan_opened)
            {
                Toast.makeText(this, "Hide timespan filters!", Toast.LENGTH_SHORT).show();
                layout_filter_timespan.setVisibility(View.GONE);
                filter_timespan_opened = false;
            } else {
                Toast.makeText(this, "Show timespan filters!", Toast.LENGTH_SHORT).show();
                layout_filter_timespan.setVisibility(View.VISIBLE);
                layout_filter_object_type.setVisibility(View.GONE);
                filter_timespan_opened = true;
                filter_objects_opened = false;
            }

        } else if (v.getId() == R.id.filter_remove_timespan) {

            Toast.makeText(this, "Remove timespan filters!", Toast.LENGTH_SHORT).show();
            layout_filter_timespan.setVisibility(View.GONE);
            filter_timespan_opened = false;

            timespan = 0;

            FilterMapObjects();

        } else if (v.getId() == R.id.filter_today) {

            Toast.makeText(this, "Show today's markers!", Toast.LENGTH_SHORT).show();
            layout_filter_timespan.setVisibility(View.GONE);
            filter_timespan_opened = false;

            timespan = 1;

            FilterMapObjects();

        } else if (v.getId() == R.id.filter_one_week) {

            Toast.makeText(this, "Show this week's markers!", Toast.LENGTH_SHORT).show();
            layout_filter_timespan.setVisibility(View.GONE);
            filter_timespan_opened = false;

            timespan = 7;
            FilterMapObjects();

        } else if (v.getId() == R.id.filter_one_month) {

            Toast.makeText(this, "Show this month's markers!", Toast.LENGTH_SHORT).show();
            layout_filter_timespan.setVisibility(View.GONE);
            filter_timespan_opened = false;

            timespan = 30;
            FilterMapObjects();

        } else if (v.getId() == R.id.filter_checkpoint) {

            if ((object_filter & 0x02) == 0x02) {
                //filter je ukljucen, treba ga iskljuciti
                object_filter &= 0x0d;
                Toast.makeText(this, "Hide checkpoints!", Toast.LENGTH_SHORT).show();
            } else {
                object_filter |= 0x02;
                Toast.makeText(this, "Show checkpoints!", Toast.LENGTH_SHORT).show();
            }

            FilterMapObjects();

        } else if (v.getId() == R.id.filter_emoji) {

            if ((object_filter & 0x08) == 0x08) {
                object_filter &= 0x07;
                Toast.makeText(this, "Hide hearts!", Toast.LENGTH_SHORT).show();
            } else {
                object_filter |= 0x08;
                Toast.makeText(this, "Show hearts!", Toast.LENGTH_SHORT).show();
            }

            FilterMapObjects();

        } else if (v.getId() == R.id.filter_photo) {

            if ((object_filter & 0x04) == 0x04) {
                object_filter &= 0x0b;
                Toast.makeText(this, "Hide photo!", Toast.LENGTH_SHORT).show();
            } else {
                object_filter |= 0x04;
                Toast.makeText(this, "Show photo!", Toast.LENGTH_SHORT).show();
            }

            FilterMapObjects();

        } else if (v.getId() == R.id.filter_message) {

            if ((object_filter & 0x01) == 0x01) {
                //filter je ukljucen, treba ga iskljuciti
                object_filter &= 0x0e;
                Toast.makeText(this, "Hide messages!", Toast.LENGTH_SHORT).show();
            } else {
                object_filter |= 0x01;
                Toast.makeText(this, "Show messages!", Toast.LENGTH_SHORT).show();
            }

            FilterMapObjects();
        } else if (v.getId() == R.id.close_search) {

            Toast.makeText(MainActivity.this, "Close search", Toast.LENGTH_SHORT).show();
            search_fragment.setVisibility(View.GONE);
            addNewFloating.setVisibility(View.VISIBLE);
            objectInteraction.setVisibility(View.VISIBLE);
            startServiceButton.setVisibility(View.VISIBLE);
            bottom_navigation_menu.setVisibility(View.VISIBLE);

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(search_edit_text.getWindowToken(), 0);

        } else if (v.getId() == R.id.search_users_only) {

            if (search_filter_activated == 5) {
                search_filter_activated = 0;
                search_users_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Remove users filter", Toast.LENGTH_SHORT).show();

            } else {
                search_filter_activated = 5; //search users

                search_users_only.setSelected(true);
                search_messages_only.setSelected(false);
                search_photos_only.setSelected(false);
                search_checkpoints_only.setSelected(false);
                search_emojis_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Add users filter", Toast.LENGTH_SHORT).show();

            }

            this.afterTextChanged(search_edit_text.getText());

        } else if (v.getId() == R.id.search_messages_only) {

            if (search_filter_activated == 1) {
                search_filter_activated = 0;
                search_messages_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Remove messages filter", Toast.LENGTH_SHORT).show();

            } else {
                search_filter_activated = 1; //search messages

                search_users_only.setSelected(false);
                search_messages_only.setSelected(true);
                search_photos_only.setSelected(false);
                search_checkpoints_only.setSelected(false);
                search_emojis_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Add messages filter", Toast.LENGTH_SHORT).show();
            }

            this.afterTextChanged(search_edit_text.getText());

        } else if (v.getId() == R.id.search_photos_only) {

            if (search_filter_activated == 2) {
                search_filter_activated = 0;
                search_photos_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Remove photos filter", Toast.LENGTH_SHORT).show();

            } else {
                search_filter_activated = 2; //search photos

                search_users_only.setSelected(false);
                search_messages_only.setSelected(false);
                search_photos_only.setSelected(true);
                search_checkpoints_only.setSelected(false);
                search_emojis_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Add photos filter", Toast.LENGTH_SHORT).show();
            }

            this.afterTextChanged(search_edit_text.getText());

        } else if (v.getId() == R.id.search_checkpoints_only) {

            if (search_filter_activated == 3) {
                search_filter_activated = 0;
                search_checkpoints_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Remove checkpoints filter", Toast.LENGTH_SHORT).show();

            } else {
                search_filter_activated = 3; //search checkpoints

                search_users_only.setSelected(false);
                search_messages_only.setSelected(false);
                search_photos_only.setSelected(false);
                search_checkpoints_only.setSelected(true);
                search_emojis_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Add checkpoints filter", Toast.LENGTH_SHORT).show();

            }

            this.afterTextChanged(search_edit_text.getText());

        } else if (v.getId() == R.id.search_emojis_only) {

            if (search_filter_activated == 4) {
                search_filter_activated = 0;
                search_emojis_only.setSelected(false);

                Toast.makeText(MainActivity.this, "Remove emojis filter", Toast.LENGTH_SHORT).show();

            } else {
                search_filter_activated = 4; //search emojis

                search_users_only.setSelected(false);
                search_messages_only.setSelected(false);
                search_photos_only.setSelected(false);
                search_checkpoints_only.setSelected(false);
                search_emojis_only.setSelected(true);

                Toast.makeText(MainActivity.this, "Add emojis filter", Toast.LENGTH_SHORT).show();
            }

            this.afterTextChanged(search_edit_text.getText());

        } else if (v.getId() == R.id.search_remove_filters) {

            Toast.makeText(MainActivity.this, "Remove all search filters", Toast.LENGTH_SHORT).show();

            search_filter_activated = 0;

            search_users_only.setSelected(false);
            search_messages_only.setSelected(false);
            search_photos_only.setSelected(false);
            search_checkpoints_only.setSelected(false);
            search_emojis_only.setSelected(false);

            this.afterTextChanged(search_edit_text.getText());

        }
        else if(v.getId() == R.id.info_add_to_savedroutes)
        {
            if(!info_groups_opened) {


                info_groups_opened = true;
                MapObject objectTag = (MapObject) v.getTag();
                info_window_container_groups.setVisibility(View.VISIBLE);
                ArrayList<String> groupList = new ArrayList<>();


                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                        getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();

                int num = sharedPref.getInt(getString(R.string.NumberOfSavedTotal), 0);
                String prikazz = "";
                for (int i = 0; i < num; i++) {
                    String groupName = sharedPref.getString(getString(R.string.SavedRoutesGroup) + i, "EMPTY");
                    int numm = sharedPref.getInt(getString(R.string.NumberOfSavedGroup) + groupName, -1);
                    groupList.add(groupName);
                }

                String id = objectTag.datetime + objectTag.createdBy; //is this ok
                GroupsRecyclerAdapter groupsRecyclerAdapter = new GroupsRecyclerAdapter(getApplicationContext(), groupList, id);

                RecyclerView groupsListView = findViewById(R.id.main_info_listview_grupe);
                groupsListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                groupsListView.setAdapter(groupsRecyclerAdapter);
            }
            else
            {
                info_groups_opened = false;
                info_window_container_groups.setVisibility(View.GONE);
            }

           /*MapObject objectTag = (MapObject) v.getTag();
            String imeGrupe = "Prva grupa";
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();

            int num = sharedPref.getInt(getString(R.string.NumberOfSavedTotal), 0);

            //dodavanje grupe
            if(num == 0) {
                editor.putString(getString(R.string.SavedRoutesGroup) + num, imeGrupe);
                editor.putInt(getString(R.string.NumberOfSavedTotal), 1);
            }


            num = sharedPref.getInt(getString(R.string.NumberOfSavedGroup) + imeGrupe, 0);

            String id = objectTag.datetime + objectTag.createdBy.email;
            editor.putString(getString(R.string.SavedRoute) + imeGrupe + num, id);
            num++;
            editor.putInt(getString(R.string.NumberOfSavedGroup) + imeGrupe, num);

            editor.commit();*/
        }
        else if(v.getId() == R.id.main_info_addgroup)
        {
            final EditText adinput = new EditText(getApplicationContext());
            adinput.setInputType(InputType.TYPE_CLASS_TEXT );

            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setView(adinput);
            builder.setTitle("Make a new group")
                    .setMessage("Choose group's name")
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String groupname=adinput.getText().toString();
                            if(groupname.equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert group's name", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Group created " + groupname, Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                                        getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPref.edit();

                                int num = sharedPref.getInt(getString(R.string.NumberOfSavedTotal), 0);

                                editor.putString(getString(R.string.SavedRoutesGroup) + num, groupname);
                                num++;
                                editor.putInt(getString(R.string.NumberOfSavedTotal), num);

                                editor.commit();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }

    }

    private void HideUsers() {

        for (int i = 0; i < usersMarkers.size(); i++) {
            usersMarkers.get(i).remove();
        }

    }

    protected void FilterMapObjects() {

        //filter: 0000****, if * = 1 then show certain marker type
        //timespan: 1 - today, 7 - a week, 30 - a month, 0 - not filtered by timespan

        ArrayList<MapObject> filteredObjects = MapObjectData.getInstance().getFilteredMapObjects(object_filter, timespan, radius, loggedUsername);

        for (int i = 0; i < objectsMarkers.size(); i++) {
            Marker m = objectsMarkers.get(i);
            m.remove();
        }

        objectsMarkers = new ArrayList<>();

        for (int i = 0; i < filteredObjects.size(); i++) {
            AddMarkerObject(filteredObjects.get(i));
        }
    }

    protected void FilterUserObjects() {

        HideUsers();

        ArrayList<User> friendsUsers = FriendshipData.getInstance().GetUserFriends(loggedUser.email);
        ArrayList<User> closeUsers = new ArrayList<>();

        for (int i = 0; i < friendsUsers.size(); i++) {

            User friendUser = UserData.getInstance().getUserByUsername(friendsUsers.get(i).username);

            Position currentUserPosition = loggedUser.UserPosition;
            double current_lat = Double.parseDouble(currentUserPosition.latitude);
            double current_lon = Double.parseDouble(currentUserPosition.longitude);

            Position friendUserPosition = friendUser.UserPosition;
            double friend_lat = Double.parseDouble(friendUserPosition.latitude);
            double friend_lon = Double.parseDouble(friendUserPosition.longitude);

            float dist[] = new float[1];
            Location.distanceBetween(current_lat, current_lon, friend_lat, friend_lon, dist);

            if (dist[0] <= radius) {
                closeUsers.add(friendUser);
            }
        }

        for (int i = 0; i < closeUsers.size(); i++) {
            AddUserMarker(closeUsers.get(i).username);
        }
    }

    /*protected void FilterMapObjectsByTimespan() {

        ArrayList<MapObject> filteredObjects = MapObjectData.getInstance().getFilteredMapObjects(object_filter, timespan, loggedUsername);

        for (int i = 0; i < objectsMarkers.size(); i++) {
            objectsMarkers.get(i).remove();
        }

        for (int i = 0; i < filteredObjects.size(); i++) {
            AddMarkerObject(filteredObjects.get(i));
        }
    }*/

    @Override
    public boolean onMarkerClick(Marker marker) {
        ShowMarkerInfoWindow(marker);
        return false;
    }

    public void FindObjectOnMap(MapObject obj) {

        search_fragment.setVisibility(View.GONE);
        addNewFloating.setVisibility(View.VISIBLE);
        objectInteraction.setVisibility(View.VISIBLE);
        startServiceButton.setVisibility(View.VISIBLE);
        bottom_navigation_menu.setVisibility(View.VISIBLE);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(search_edit_text.getWindowToken(), 0);

        double lat = Double.parseDouble(obj.position.latitude);
        double lon = Double.parseDouble(obj.position.longitude);

        LatLng latLng = new LatLng(lat, lon);

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));

        Marker m = FindMapMarker(obj);

        ShowMarkerInfoWindow(m);

    }

    protected void ShowMarkerInfoWindow(Marker marker) {
        if (lastSelected == null || !lastSelected.equals(marker)) {

            info_window_container.setVisibility(View.VISIBLE);

            if (marker.getTag() instanceof MapObject) {

                info_window_see_details.setVisibility(View.VISIBLE);

                MapObject objectTag = (MapObject) marker.getTag();

                if (objectTag.createdBy.compareTo(loggedUsername) == 0) {
                    info_window_delete_object.setVisibility(View.VISIBLE);
                    info_window_delete_object.setTag(objectTag);
                } else {
                    info_window_delete_object.setVisibility(View.GONE);
                }

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

                info_window_username.setText(String.format("Left on %s by\n%s", objectTag.date, objectTag.createdBy));
                info_window_lat.setText("lat: " + lat);
                info_window_lon.setText("lon: " + lon);
                info_window_see_details.setTag(objectTag);
                info_window_see_details.setText("see details");
                info_add_to_route.setTag(objectTag);
                info_add_to_route.setVisibility(View.VISIBLE);

            } else {

                info_window_delete_object.setVisibility(View.GONE);

                String username = (String) marker.getTag();
                User referringTo = UserData.getInstance().getUserByUsername(username);

                String profilePhotoUri = referringTo.image;

                if (!profilePhotoUri.equals("")) {

                    Glide.with(this).asBitmap().load(profilePhotoUri).into(new SimpleTarget<Bitmap>(128, 128) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            info_window_icon.setImageBitmap(resource);
                        }
                    });
                } else {
                    info_window_icon.setImageResource(R.drawable.ic_user);
                }

                info_window_username.setText(username);
                info_window_lat.setText("lat: " + referringTo.UserPosition.latitude);
                info_window_lon.setText("lon: " + referringTo.UserPosition.longitude);

                if (username.compareTo(loggedUsername) == 0)
                    info_window_see_details.setVisibility(View.GONE);
                else
                    info_window_see_details.setVisibility(View.VISIBLE);

                info_window_see_details.setText("go to profile");
                info_window_see_details.setTag(username);
                info_add_to_route.setVisibility(View.GONE);

            }

            lastSelected = marker;



        } else {
            info_window_container.setVisibility(View.GONE);
            lastSelected = null;
        }
    }

    protected Marker FindMapMarker(MapObject mapObject) {

        for (int i = 0; i < objectsMarkers.size(); i++) {
            if (((MapObject) objectsMarkers.get(i).getTag()).equals(mapObject))
                return objectsMarkers.get(i);
        }
        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String insertedText = s.toString();

        ArrayList<MapObject> objs =
                MapObjectData.getInstance().getSearchedMapObjects(insertedText, search_filter_activated, search_radius, loggedUsername);

        SearchResultsListAdapter listAdapter =
                new SearchResultsListAdapter(getApplicationContext(), R.layout.list_member_search_result, objs, MainActivity.this);

        searchResultsListView.setAdapter(listAdapter);

    }
}
