package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class FriendProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    User user;

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

        FillUserData();

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
        TextView messages = findViewById(R.id.friend_profile_messages);
        TextView pictures = findViewById(R.id.friend_profile_pictures);
        TextView bio = findViewById(R.id.friend_profile_bio_text);

        username.setText(user.username);
    }
}
