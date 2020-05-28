package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class AddFriendActivity extends AppCompatActivity {

    String[] friends={"Zika","Pera","Mika"};
    ListView list;
    Toolbar toolbar;

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

        list=(ListView) findViewById(R.id.add_friend_listview);
        Context context=getApplicationContext();
        CustomListView adapter=new CustomListView(this,friends,
                null,false);
        list.setAdapter(adapter);
    }
}
