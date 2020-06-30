package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import tamara.mosis.elfak.walkhike.R;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView edit;
    CircularImageView profPic;
    Button doneBtn;

    String imgUrl;

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
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        doneBtn=findViewById(R.id.buttonDoneEdit);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("img", imgUrl);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        profPic=findViewById(R.id.imageViewProfPic);
        edit=findViewById(R.id.imageViewEditPicture);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insertProfPic =new Intent(getApplicationContext(),NewProfilePictureActivity.class);
                startActivityForResult(insertProfPic,1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    imgUrl = data.getStringExtra("img");
                    RequestOptions placeholderOpt = new RequestOptions();
                    placeholderOpt.placeholder(R.drawable.girl_1);
                    Glide.with(getApplicationContext()).setDefaultRequestOptions(placeholderOpt).load(imgUrl).into(profPic);
                }
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
