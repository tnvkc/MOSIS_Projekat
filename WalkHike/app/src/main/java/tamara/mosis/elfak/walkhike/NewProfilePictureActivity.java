package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class NewProfilePictureActivity extends AppCompatActivity {

    Integer[] imgid={R.drawable.ic_photo_camera_black_24dp,R.drawable.ic_image_black_24dp};
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar;

        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_new_profile_picture);

        toolbar = (Toolbar) findViewById(R.id.new_profile_picture_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert profile picture");

        list=(ListView) findViewById(android.R.id.list);
        Context context=getApplicationContext();
        CustomListView adapter=new CustomListView(this,context.getResources().getStringArray(R.array.new_profile_image_options),
                imgid,"newProfilePicture");
        list.setAdapter(adapter);

    }
}
