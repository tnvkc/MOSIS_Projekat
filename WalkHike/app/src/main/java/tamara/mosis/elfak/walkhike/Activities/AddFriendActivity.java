package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import tamara.mosis.elfak.walkhike.Fragments.OtherUsersFragment;
import tamara.mosis.elfak.walkhike.OtherUsersRecyclerAdapter;
import tamara.mosis.elfak.walkhike.R;

public class AddFriendActivity extends AppCompatActivity {

    String[] friends={"Zika","Pera","Mika"};
    ListView list;
    Toolbar toolbar;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_add_friend);

        toolbar = (Toolbar) findViewById(R.id.add_friend_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add friend");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchView=findViewById(R.id.searchView);
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
                    OtherUsersFragment fragmentfr = (OtherUsersFragment)
                            getSupportFragmentManager().findFragmentById(R.id.fragment_other_users);
                    OtherUsersRecyclerAdapter ura = fragmentfr.usersRecyclerAdapter;
                    ura.filterUsersList(newText);
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
