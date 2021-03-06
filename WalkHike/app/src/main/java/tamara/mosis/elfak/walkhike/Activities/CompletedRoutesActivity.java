package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Activity;
import tamara.mosis.elfak.walkhike.Fragments.Leaderboards_Activity_Distance_ViewPagerAdapter;
import tamara.mosis.elfak.walkhike.Fragments.SavedRoutesGroupsFragment;
import tamara.mosis.elfak.walkhike.Fragments.Savedroutes_group_items;
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

    private View viewvpager_groups;
    FloatingActionButton floatingActionButton;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_completed_routes);
        toolbar = (Toolbar) findViewById(R.id.savedroutes_toolbar);
        floatingActionButton = findViewById(R.id.savedroutes_addGroup_actiionButtor);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText adinput = new EditText(getApplicationContext());
                adinput.setInputType(InputType.TYPE_CLASS_TEXT );

                AlertDialog.Builder builder=new AlertDialog.Builder(CompletedRoutesActivity.this);
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

                                    myObjectListener.onNewObject();
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
        });


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Saved objects");

        getSupportActionBar().setDisplayShowTitleEnabled(true);


        //prikaz = findViewById(R.id.savedroutes_textview);
        //
        //popuniObjekte();

        viewvpager_groups = (View) findViewById(R.id.savedroutes_viewpager);
        SavedRoutesGroupsFragment firstFragment = new SavedRoutesGroupsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.savedroutes_viewpager, firstFragment).commit();

       /* */

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
                        finish();
                        return true;
                    }
                    case R.id.friends: {
                        Intent intent=new Intent(getApplicationContext(), FriendslistActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    case R.id.map: {
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    case R.id.leaderboard: {
                        Intent intent=new Intent(getApplicationContext(), LeaderboardsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });

    }


    private static ListenerForNewGroups  myObjectListener;

    public static void setUpListenerObjectNoti(ListenerForNewGroups Listener) {
        myObjectListener = Listener;
    }



    public void notifyNewGroup()
    { //View view
        if(myObjectListener != null)
            myObjectListener.onNewObject();
    }

    public interface ListenerForNewGroups{
        void onNewObject();
    }


    void onGroupClick()
    {

    }
    /*@Override
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
    }*/

}
