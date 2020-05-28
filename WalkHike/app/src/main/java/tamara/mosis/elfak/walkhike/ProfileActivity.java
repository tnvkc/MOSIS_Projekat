package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    Integer[] imgid={R.drawable.ic_edit_black_24dp,R.drawable.ic_settings_black_24dp,R.drawable.ic_help_outline_black_24dp,
            R.drawable.ic_logout__black_24dp};
    ListView list;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        list = (ListView) findViewById(R.id.listview_profile_options);
        Context context = getApplicationContext();
        CustomListView adapter = new CustomListView(this, context.getResources().getStringArray(R.array.profile_options),
                imgid, false);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        //intent=new Intent(getApplicationContext(), EditProfileActivity.class);
                        // startActivity(intent);
                        Toast.makeText(getApplicationContext(), "LOGED OUT", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }
        });


    }
}
