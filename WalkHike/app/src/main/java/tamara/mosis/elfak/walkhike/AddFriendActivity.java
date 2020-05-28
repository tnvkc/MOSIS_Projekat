package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class AddFriendActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        toolbar = (Toolbar) findViewById(R.id.add_friend_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add friend");
    }
}
