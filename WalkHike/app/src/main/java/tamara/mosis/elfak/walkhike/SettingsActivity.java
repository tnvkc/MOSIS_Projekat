package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Integer[] imgid={R.drawable.ic_music_note_black_24dp,R.drawable.ic_notifications_black_24dp,R.drawable.ic_location_on_black_24dp,
            R.drawable.ic_dark_mode_24dp,R.drawable.ic_unit_24dp};
    ListView list;
    boolean dark = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar;

        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
           setTheme(R.style.AppThemeLight);


        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        list=(ListView) findViewById(R.id.list_settings);
        Context context=getApplicationContext();
        CustomListView adapter=new CustomListView(this,context.getResources().getStringArray(R.array.settings_options),
                imgid,"settings");
        list.setAdapter(adapter);


        Button b = (Button) findViewById(R.id.switch_theme);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dark = !dark;
                    Toast.makeText(getApplicationContext(), " " + dark, Toast.LENGTH_SHORT).show();
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                //finish();
                //startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
            }
        });



    }
}
