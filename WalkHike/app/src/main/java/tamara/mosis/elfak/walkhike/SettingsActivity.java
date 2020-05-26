package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class SettingsActivity extends AppCompatActivity {
    Integer[] imgid={R.drawable.ic_music_note_black_24dp,R.drawable.ic_notifications_black_24dp,R.drawable.ic_location_on_black_24dp,
            R.drawable.ic_dark_mode_24dp,R.drawable.ic_unit_24dp};
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        list=(ListView) findViewById(R.id.list_settings);
        Context context=getApplicationContext();
        CustomListView adapter=new CustomListView(this,context.getResources().getStringArray(R.array.settings_options),
                imgid,true);
        list.setAdapter(adapter);

    }
}
