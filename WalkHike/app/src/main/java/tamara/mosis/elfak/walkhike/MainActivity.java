package tamara.mosis.elfak.walkhike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {



    Button btn_notification;
    private static final int PERMISSION_CODE = 1;
    Location location;
    LocationManager locationManager;
    Button btn_friendslist;
    Button btn_addobject;
    Button btn_completedroutes;
    Button btn_leaderboards;
    private GoogleMap map;
    boolean isGpsProvider;
    boolean isNetworkProvider;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        } else {
            map.setMyLocationEnabled(true);


        }
        getDeviceLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map_fragment);
        mapFragment.getMapAsync(this);


        btn_addobject = findViewById(R.id.main_addObjects);
        btn_addobject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNewObjectActivity.class);
                startActivity(intent);
            }
        });

        btn_friendslist = findViewById(R.id.main_friends);
        btn_friendslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendslistActivity.class);
                startActivity(intent);
            }
        });

        btn_notification = findViewById(R.id.main_notifications);
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
                startActivity(intent);
            }
        });

        btn_completedroutes = findViewById(R.id.main_completedroutes);
        btn_completedroutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompletedRoutesActivity.class);
                startActivity(intent);
            }
        });

        btn_leaderboards = findViewById(R.id.main_leaderboards);
        btn_leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaderboardsActivity.class);
                startActivity(intent);
            }
        });

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
                map.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
                Toast.makeText(this, "Usao u latlong", Toast.LENGTH_SHORT );
            }
        }
        //startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
    //Toast.makeText(MapWithPlayServiceLocationActivity.this, "Lat : "+location.getLatitude()+" Lng "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        if(map!=null){
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
        }
    }



}
