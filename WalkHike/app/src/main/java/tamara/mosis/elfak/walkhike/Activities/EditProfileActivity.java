package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import tamara.mosis.elfak.walkhike.R;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit profile");

        edit=findViewById(R.id.imageViewEditPicture);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insertProfPic =new Intent(getApplicationContext(),NewProfilePictureActivity.class);
                startActivity(insertProfPic);
            }
        });
    }
}
