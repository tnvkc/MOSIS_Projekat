package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class FriendRequestsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView list;
    String[] friendRequests={"Friend1","Friend2","friend3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_friend_requests);

        toolbar = (Toolbar) findViewById(R.id.friend_requests_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Friend requests");


    }
}
