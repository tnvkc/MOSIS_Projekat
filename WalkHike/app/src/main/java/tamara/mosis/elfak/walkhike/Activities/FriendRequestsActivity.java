package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import tamara.mosis.elfak.walkhike.R;

public class FriendRequestsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView list;

    Button buttonDone;
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


        buttonDone = findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "heloo", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
