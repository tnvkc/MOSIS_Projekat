package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import tamara.mosis.elfak.walkhike.CustomListView;
import tamara.mosis.elfak.walkhike.R;

public class SettingsActivity extends AppCompatActivity {
    Integer[] imgid={R.drawable.ic_music_note_black_24dp,R.drawable.ic_notifications_black_24dp,R.drawable.ic_location_on_black_24dp,
            R.drawable.ic_dark_mode_24dp,R.drawable.ic_unit_24dp};
    ListView list;
    Button btnkm;
    Button btnmi;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences=getApplicationContext().getSharedPreferences(
                "Userdata", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        list=(ListView) findViewById(R.id.list_settings);
        Context context=getApplicationContext();
        CustomListView adapter=new CustomListView(this,context.getResources().getStringArray(R.array.settings_options),
                imgid,true);
        list.setAdapter(adapter);

        btnkm=findViewById(R.id.buttonkm);
        btnmi=findViewById(R.id.buttonmi);

        if(sharedPreferences.getString("userUnit","km")=="km")
        {
            btnkm.setBackgroundResource(R.drawable.btnaccent);
            btnmi.setBackgroundResource(android.R.drawable.btn_default);
        }
        else//mi
        {
            btnkm.setBackgroundResource(android.R.drawable.btn_default);
            btnmi.setBackgroundResource(R.drawable.btnaccent);
        }

        btnkm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("userUnit", "km");
                btnkm.setBackgroundResource(R.drawable.btnaccent);
                btnmi.setBackgroundResource(android.R.drawable.btn_default);
            }
        });

        btnmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("userUnit", "mi");
                btnkm.setBackgroundResource(android.R.drawable.btn_default);
                btnmi.setBackgroundResource(R.drawable.btnaccent);
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
