package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class ObjectInteractionActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    MapObject obj;
    String currentUser;

    private ImageView obj_interaction_reaction_great;
    private ImageView obj_interaction_reaction_meh;
    private ImageView obj_interaction_reaction_boo;

    private TextView reactions_great;
    private TextView reactions_meh;
    private TextView reactions_boo;

    int great;
    int meh;
    int boo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_object_interaction);

        Button obj_interaction_close = findViewById(R.id.object_interaction_close);
        obj_interaction_close.setOnClickListener(this);

        TextView obj_interaction_text = findViewById(R.id.object_interaction_text);
        TextView obj_interaction_lat = findViewById(R.id.object_interaction_lat);
        TextView obj_interaction_lon = findViewById(R.id.object_interaction_lon);
        TextView obj_interaction_desc = findViewById(R.id.object_interaction_desc);
        ImageView obj_interaction_icon = findViewById(R.id.object_interaction_icon);
        obj_interaction_reaction_great = findViewById(R.id.object_interaction_great);
        obj_interaction_reaction_meh = findViewById(R.id.object_interaction_meh);
        obj_interaction_reaction_boo = findViewById(R.id.object_interaction_boo);
        reactions_great = findViewById(R.id.reactions_great);
        reactions_meh = findViewById(R.id.reactions_meh);
        reactions_boo = findViewById(R.id.reactions_boo);

        obj_interaction_reaction_great.setOnClickListener(this);
        obj_interaction_reaction_meh.setOnClickListener(this);
        obj_interaction_reaction_boo.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.object_interaction_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent callerIntent = getIntent();
        obj = (MapObject) callerIntent.getSerializableExtra("object");
        currentUser = callerIntent.getStringExtra("username");

        if (obj != null) {

            int objectType = obj.objectType;

            String text = String.format("User %s was here on %s\n and left this ", obj.createdBy, obj.date);

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

            } else if (objectType == 4) {
                getSupportActionBar().setTitle("Emoji");
                text += "emoji:";
                desc += "reaction: ";
                obj_interaction_icon.setImageResource(R.drawable.ic_heart);
            }

            desc += obj.desc;

            obj_interaction_text.setText(text);

            String lat_text = "latitude: " + obj.position.latitude;
            String lon_text = "longitude: " + obj.position.longitude;
            obj_interaction_lat.setText(lat_text);
            obj_interaction_lon.setText(lon_text);

            obj_interaction_desc.setText(desc);

            great = obj.reactionsGreat;
            meh = obj.reactionsMeh;
            boo = obj.reactionsBoo;

            SetReactionStrings();

            if (currentUser.compareTo(obj.createdBy) == 0) {

                obj_interaction_reaction_great.setClickable(false);
                obj_interaction_reaction_great.setFocusable(false);
                obj_interaction_reaction_meh.setClickable(false);
                obj_interaction_reaction_meh.setFocusable(false);
                obj_interaction_reaction_boo.setClickable(false);
                obj_interaction_reaction_boo.setFocusable(false);

            } else {

                String object_key = obj.createdBy + obj.datetime;

                SharedPreferences sharedPref = getApplicationContext()
                        .getSharedPreferences(getString(R.string.ReactedObjectsPrefs), Context.MODE_PRIVATE);

                String s = sharedPref.getString(object_key, "EMPTY");

                if(s.compareTo("EMPTY") != 0) {

                    if (s.compareTo("great") == 0) {
                        obj.reactionsGreat--;
                        ReactGreat();
                    } else if (s.compareTo("meh") == 0) {
                        obj.reactionsMeh--;
                        ReactMeh();
                    } else {
                        obj.reactionsBoo--;
                        ReactBoo();
                    }

                }

            }

        }
    }

    private void ReactGreat() {

        obj_interaction_reaction_great.setBackgroundColor(getColor(R.color.colorAccent));
        obj_interaction_reaction_meh.setBackgroundColor(getColor(android.R.color.transparent));
        obj_interaction_reaction_boo.setBackgroundColor(getColor(android.R.color.transparent));

        great = obj.reactionsGreat + 1;
        meh = obj.reactionsMeh;
        boo = obj.reactionsBoo;

    }

    private void ReactMeh() {

        obj_interaction_reaction_great.setBackgroundColor(getColor(android.R.color.transparent));
        obj_interaction_reaction_meh.setBackgroundColor(getColor(R.color.colorAccent));
        obj_interaction_reaction_boo.setBackgroundColor(getColor(android.R.color.transparent));

        great = obj.reactionsGreat;
        meh = obj.reactionsMeh + 1;
        boo = obj.reactionsBoo;

    }

    private void ReactBoo() {

        obj_interaction_reaction_great.setBackgroundColor(getColor(android.R.color.transparent));
        obj_interaction_reaction_meh.setBackgroundColor(getColor(android.R.color.transparent));
        obj_interaction_reaction_boo.setBackgroundColor(getColor(R.color.colorAccent));

        great = obj.reactionsGreat;
        meh = obj.reactionsMeh;
        boo = obj.reactionsBoo + 1;

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

            ReactGreat();
            SetReactionStrings();

        } else if (v.getId() == R.id.object_interaction_meh) {

            ReactMeh();
            SetReactionStrings();

        } else if (v.getId() == R.id.object_interaction_boo) {

            ReactBoo();
            SetReactionStrings();

        } else if (v.getId() == R.id.object_interaction_close) {

            String reaction = "";
            if (obj.reactionsGreat != great)
                reaction = "great";
            else if (obj.reactionsMeh != meh)
                reaction = "meh";
            else
                reaction = "boo";

            obj.reactionsGreat = great;
            obj.reactionsMeh = meh;
            obj.reactionsBoo = boo;

            MapObjectData.getInstance().UpdateMapObjectReaction(obj); //to do

            String object_key = obj.createdBy + obj.datetime;

            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    getString(R.string.ReactedObjectsPrefs), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(object_key, reaction);
            editor.apply();

            //update user activity scores
            ScoresData.getInstance().updateScoreActivity(100, currentUser);

            Toast.makeText(this, "Current reactions " + great + ", " + meh + ", " + boo, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void SetReactionStrings() {
        String reactions_text_great = "(" + great +")";
        String reactions_text_meh = "(" + meh +")";
        String reactions_text_boo = "(" + boo +")";

        reactions_great.setText(reactions_text_great);
        reactions_meh.setText(reactions_text_meh);
        reactions_boo.setText(reactions_text_boo);
    }
}
