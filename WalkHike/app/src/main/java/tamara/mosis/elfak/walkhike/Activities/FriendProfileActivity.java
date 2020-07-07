package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.GroupItemsRecyclerAdapter;
import tamara.mosis.elfak.walkhike.MapObjectRecyclerAdapter;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class FriendProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    User user;

    Scores score;
    ArrayList<MapObject> objekti;
    RecyclerView rec;
    private MapObjectRecyclerAdapter recAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_friend_profile);

        toolbar = (Toolbar) findViewById(R.id.friend_profile_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Profile");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        user = UserData.getInstance().getUserByUsername(username);
        score = ScoresData.getInstance().getScore(username);
        objekti = MapObjectData.getInstance().getFriendsMapObjects(username);

        FillUserData();

        rec = findViewById(R.id.friends_profile_listview);
        //prikaz.setText("OVO JE FRAGMENT");

        recAdapter=new MapObjectRecyclerAdapter(FriendProfileActivity.this, objekti );


        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(FriendProfileActivity.this));

        rec.setAdapter(recAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void FillUserData() {

        ImageView profile_photo = findViewById(R.id.friend_profile_profilePhoto);
        TextView username = findViewById(R.id.friend_profile_username);
        TextView total_distance = findViewById(R.id.friend_profile_total_distance);
        //TextView messages = findViewById(R.id.friend_profile_messages);
        TextView pictures = findViewById(R.id.friend_profile_pictures);
        TextView bio = findViewById(R.id.friend_profile_bio_text);

        total_distance.setText("Total: distance: "+ score.alltimeDistance + ", activity: " + score.alltimeActivity);
        pictures.setText("Total object: "+objekti.size());

        username.setText(user.username);
        bio.setText(user.desc);
    }
}
