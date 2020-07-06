package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;

public class ObjectInteractionActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    private ImageView obj_interaction_reaction_great;
    private ImageView obj_interaction_reaction_meh;
    private ImageView obj_interaction_reaction_boo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_object_interaction);

        TextView obj_interaction_text = findViewById(R.id.object_interaction_text);
        TextView obj_interaction_lat = findViewById(R.id.object_interaction_lat);
        TextView obj_interaction_lon = findViewById(R.id.object_interaction_lon);
        TextView obj_interaction_desc = findViewById(R.id.object_interaction_desc);
        ImageView obj_interaction_icon = findViewById(R.id.object_interaction_icon);
        obj_interaction_reaction_great = findViewById(R.id.object_interaction_great);
        obj_interaction_reaction_meh = findViewById(R.id.object_interaction_meh);
        obj_interaction_reaction_boo = findViewById(R.id.object_interaction_boo);

        obj_interaction_reaction_great.setOnClickListener(this);
        obj_interaction_reaction_meh.setOnClickListener(this);
        obj_interaction_reaction_boo.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.object_interaction_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent callerIntent = getIntent();
        MapObject obj = (MapObject) callerIntent.getSerializableExtra("object");

        if (obj != null) {
            int objectType = obj.objectType;

            String text = String.format("User %s was here on %s\n and left this ", obj.createdBy.username, obj.date);

            String desc = "";

            if (objectType == 1) {
                getSupportActionBar().setTitle("Message");
                text += "message:";
                desc += "message: ";
                obj_interaction_icon.setImageResource(R.drawable.ic_message);
            } else if (objectType == 2) {
                getSupportActionBar().setTitle("Checkpoint");
                text += "checkpoint:";
                desc += "place name: ";
                obj_interaction_icon.setImageResource(R.drawable.ic_marker);
            } else if (objectType == 3) {
                getSupportActionBar().setTitle("Photo");
                text += "photo:";
                desc += "photo description: ";

                Glide.with(this).load(obj.photo).into(obj_interaction_icon);
                /*Glide.with(this).asBitmap().load(obj.photo).into(new SimpleTarget<Bitmap>(obj_interaction_icon.getWidth(), obj_interaction_icon.getHeight()) {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        obj_interaction_icon.setImageBitmap(resource);
                    }
                });*/

            } else if (objectType == 4) {
                getSupportActionBar().setTitle("Emoji");
                text += "emoji:";
                desc += "reaction: ";
                obj_interaction_icon.setImageResource(R.drawable.ic_heart);
            }

            desc += obj.desc;

            obj_interaction_text.setText(text);
            obj_interaction_lat.setText("latitude: " + obj.position.latitude);
            obj_interaction_lon.setText("longitude: " + obj.position.longitude);
            obj_interaction_desc.setText(desc);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.object_interaction_great) {

        } else if (v.getId() == R.id.object_interaction_meh) {

        } else if (v.getId() == R.id.object_interaction_boo) {

        }

    }
}
