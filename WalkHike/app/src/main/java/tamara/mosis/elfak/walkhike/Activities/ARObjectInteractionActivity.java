package tamara.mosis.elfak.walkhike.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class ARObjectInteractionActivity extends AppCompatActivity implements View.OnClickListener {

    private ArFragment arFragment;
    private Scene scene;
    private Camera camera;

    private int objectType;

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
        setContentView(R.layout.activity_ar_object_interaction);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_object_interaction_ar_fragment);

        //hide hand animation
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        scene = arFragment.getArSceneView().getScene();
        camera = scene.getCamera();

        Button obj_interaction_close = findViewById(R.id.ar_object_interaction_close);
        obj_interaction_close.setOnClickListener(this);

        TextView obj_interaction_text = findViewById(R.id.ar_object_interaction_text);
        TextView obj_interaction_lat = findViewById(R.id.ar_object_interaction_lat);
        TextView obj_interaction_lon = findViewById(R.id.ar_object_interaction_lon);
        TextView obj_interaction_desc = findViewById(R.id.ar_object_interaction_desc);
        obj_interaction_reaction_great = findViewById(R.id.ar_object_interaction_great);
        obj_interaction_reaction_meh = findViewById(R.id.ar_object_interaction_meh);
        obj_interaction_reaction_boo = findViewById(R.id.ar_object_interaction_boo);
        reactions_great = findViewById(R.id.ar_reactions_great);
        reactions_meh = findViewById(R.id.ar_reactions_meh);
        reactions_boo = findViewById(R.id.ar_reactions_boo);

        obj_interaction_reaction_great.setOnClickListener(this);
        obj_interaction_reaction_meh.setOnClickListener(this);
        obj_interaction_reaction_boo.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.ar_object_interaction_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent callerIntent = getIntent();
        obj = (MapObject) callerIntent.getSerializableExtra("object");
        currentUser = callerIntent.getStringExtra("username");


        if (obj != null) {
            objectType = obj.objectType;

            String text = String.format("User %s was here on %s\n and left this ", obj.createdBy, obj.date);

            String desc = "";

            if (objectType == 2) {
                getSupportActionBar().setTitle("Checkpoint");
                text += "checkpoint:";
                desc += "place name: ";
            } else if (objectType == 4) {
                getSupportActionBar().setTitle("Emoji");
                text += "emoji:";
                desc += "reaction: ";
            }

            ShowArObject();

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

                obj_interaction_close.setText("Close");

            } else {

                obj_interaction_close.setText("Submit");
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

            if (currentUser.compareTo(obj.createdBy) == 0)
                return;

            ReactGreat();
            SetReactionStrings();

        } else if (v.getId() == R.id.object_interaction_meh) {

            if (currentUser.compareTo(obj.createdBy) == 0)
                return;

            ReactMeh();
            SetReactionStrings();

        } else if (v.getId() == R.id.object_interaction_boo) {

            if (currentUser.compareTo(obj.createdBy) == 0)
                return;

            ReactBoo();
            SetReactionStrings();

        } else if (v.getId() == R.id.ar_object_interaction_close) {

            if (currentUser.compareTo(obj.createdBy) != 0) {


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
            }

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

    private void ShowArObject() {

        switch(objectType) {
            case 5: {
                ModelRenderable
                        .builder()
                        .setSource(this, R.raw.trophy)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);

                            node.setWorldPosition(new Vector3(0.0f, 0.5f, -2.0f));
                        });
                break;
            }
            case 4: {
                ModelRenderable
                        .builder()
                        .setSource(this, R.raw.heart)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);

                            node.setWorldPosition(new Vector3(0.0f, -0.5f, -2.0f));

                        });
                break;
            }
            case 2: {
                ModelRenderable
                        .builder()
                        .setSource(this, R.raw.marker)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);


                            node.setWorldRotation(new Quaternion(new Vector3(0,1,0), 90f));
                            node.setWorldPosition(new Vector3(0.0f, -0.5f, -3.0f));
                        });
                break;
            }
        }

    }

}
