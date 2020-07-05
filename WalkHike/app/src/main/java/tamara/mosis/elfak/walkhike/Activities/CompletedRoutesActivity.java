package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.FriendRequestsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.GroupSavedRecyclerAdapter;
import tamara.mosis.elfak.walkhike.GroupsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.Notification;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;

public class CompletedRoutesActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation_menu;
    RecyclerView rec;
    TextView prikaz;
    ArrayList<MapObject> objekti;
    ArrayList<String> grupe;
    MapObjectData mapObjectData;

    private GroupSavedRecyclerAdapter recAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_completed_routes);

        prikaz = findViewById(R.id.savedroutes_textview);
        rec = findViewById(R.id.saved_routes_recView);
        //popuniObjekte();

        grupe = new ArrayList<>();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);
        int num = sharedPref.getInt(getString(R.string.NumberOfSavedTotal), 0);

        for(int i = 0; i< num; i++) {
            String groupName = sharedPref.getString(getString(R.string.SavedRoutesGroup) + i, "EMPTY");
            int numm = sharedPref.getInt(getString(R.string.NumberOfSavedGroup) + groupName, -1);
            grupe.add(groupName);
        }

        recAdapter=new GroupSavedRecyclerAdapter(getApplicationContext(), grupe );


        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rec.setAdapter(recAdapter);

        bottom_navigation_menu = findViewById(R.id.bottom_navigation_menu);
        bottom_navigation_menu.setSelectedItemId(R.id.completed_routes);
        bottom_navigation_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notifications: {
                        Intent intent=new Intent(getApplicationContext(), NotificationsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.friends: {
                        Intent intent=new Intent(getApplicationContext(), FriendslistActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.map: {
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.leaderboard: {
                        Intent intent=new Intent(getApplicationContext(), LeaderboardsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    void popuniObjekte()
    {
        String prikazz = "";
        objekti = new ArrayList<>();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);
        int num = sharedPref.getInt(getString(R.string.NumberOfSavedTotal), 0);

        for(int i = 0; i< num; i++)
        {
            String groupName = sharedPref.getString(getString(R.string.SavedRoutesGroup) + i, "EMPTY");
            int numm = sharedPref.getInt(getString(R.string.NumberOfSavedGroup) + groupName, -1);
            if(numm != -1)
            {
                prikazz += groupName + "\n";
                for(int j= 0; j< numm; j++) {
                    String objectId = sharedPref.getString(getString(R.string.SavedRoute) + groupName + j, "EMPTY");
                    MapObject m = mapObjectData.getInstance().getObjectWithDatetimeUser(objectId) ;
                    objekti.add(m);
                }
            }
        }


        for(int i = 0; i< objekti.size(); i++)
        {
            MapObject curr = objekti.get(i);
            prikazz += curr.desc + " date: "+ curr.date + "\n";
        }
        prikaz.setText(prikazz);
    }
}
