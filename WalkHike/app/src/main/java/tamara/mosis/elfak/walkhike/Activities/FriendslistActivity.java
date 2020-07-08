package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Fragments.FriendsFragment;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.UsersRecyclerAdapter;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class FriendslistActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottom_navigation_menu;
    FloatingActionButton buttonAddFriend;
    SearchView searchView;
    User loggedUser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_to_friend_requests_menu, menu);
        return super.onCreateOptionsMenu(menu);
        //return false;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.friends_requests_item)
        {
            Intent intent=new Intent(getApplicationContext(), FriendRequestsActivity.class);
            startActivity(intent);

//            Toast.makeText(getApplicationContext(), "klik na add friend", Toast.LENGTH_SHORT);
        }
//        Toast.makeText(getApplicationContext(), "klik na menu", Toast.LENGTH_SHORT);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_friendslist);


        toolbar = (Toolbar)findViewById(R.id.friends_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        searchView=findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
           public boolean onQueryTextChange(String newText)
            {
                if (newText!=null)
                {
                    FriendsFragment fragmentfr = (FriendsFragment)
                            getSupportFragmentManager().findFragmentById(R.id.fragment_friends);
                    UsersRecyclerAdapter ura = fragmentfr.usersRecyclerAdapter;
                    ura.filterUsersList(newText);
                }
                return false;

            }


        });
        buttonAddFriend=findViewById(R.id.floating_ab_add_friend);
        buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        bottom_navigation_menu = findViewById(R.id.bottom_navigation_menu);
        bottom_navigation_menu.setSelectedItemId(R.id.friends);
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
                    case R.id.map: {
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.completed_routes: {
                        Intent intent=new Intent(getApplicationContext(), CompletedRoutesActivity.class);
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



   /* @Override
    public void onBackPressed() {

        if (bottom_navigation_menu.getSelectedItemId() == R.id.map)
        {
            super.onBackPressed();
            finish();
        }
        else
        {
            bottom_navigation_menu.setSelectedItemId(R.id.map);
        }
    }
*/


}
