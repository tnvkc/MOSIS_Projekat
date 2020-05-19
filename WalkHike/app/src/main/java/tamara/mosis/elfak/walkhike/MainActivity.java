package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    Button btn_notification;
    Button btn_friendslist;
    Button btn_addobject;
    Button btn_completedroutes;
    Button btn_leaderboards;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                //LatLng latlng = new LatLng(28.1,  68.1);
            }
        });


        btn_addobject = findViewById(R.id.main_addObjects);
        btn_addobject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AddNewObjectActivity.class);
                startActivity(intent);
            }
        });

        btn_friendslist = findViewById(R.id.main_friends);
        btn_friendslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FriendslistActivity.class);
                startActivity(intent);
            }
        });

        btn_notification = findViewById(R.id.main_notifications);
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), NotificationsActivity.class);
                startActivity(intent);
            }
        });

        btn_completedroutes = findViewById(R.id.main_completedroutes);
        btn_completedroutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CompletedRoutesActivity.class);
                startActivity(intent);
            }
        });

        btn_leaderboards = findViewById(R.id.main_leaderboards);
        btn_leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LeaderboardsActivity.class);
                startActivity(intent);
            }
        });

    }
}
