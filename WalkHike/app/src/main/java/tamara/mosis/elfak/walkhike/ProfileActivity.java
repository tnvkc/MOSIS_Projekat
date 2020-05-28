package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    Integer[] imgid={R.drawable.ic_edit_black_24dp,R.drawable.ic_settings_black_24dp,R.drawable.ic_help_outline_black_24dp,
            R.drawable.ic_logout__black_24dp};
    ListView list;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        list=(ListView) findViewById(R.id.listview_profile_options);
        Context context=getApplicationContext();
        CustomListView adapter=new CustomListView(this,context.getResources().getStringArray(R.array.profile_options),
                imgid,false);
        list.setAdapter(adapter);


    }
}
