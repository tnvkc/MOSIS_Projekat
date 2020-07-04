package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import tamara.mosis.elfak.walkhike.Fragments.GroupsFragment;
import tamara.mosis.elfak.walkhike.GroupsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.R;
public class FindGroupActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottom_navigation_menu;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_find_group);

        toolbar = (Toolbar)findViewById(R.id.find_group_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Groups");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                     GroupsFragment gf = (GroupsFragment)
                            getSupportFragmentManager().findFragmentById(R.id.fragment_find_group);
                    //GroupsRecyclerAdapter ura = gf.groupsRecyclerAdapter;
                   // ura.filterUsersList(newText);
                }
                return false;

            }


        });

        bottom_navigation_menu = findViewById(R.id.bottom_navigation_menu);
        bottom_navigation_menu.setSelectedItemId(R.id.map);
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
                       // Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(intent);
                       // overridePendingTransition(0, 0);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
