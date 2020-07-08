package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.Map;

import tamara.mosis.elfak.walkhike.CustomListView;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

import java.lang.Object;

public class ProfileActivity extends AppCompatActivity {

    String username;

    Integer[] imgid={R.drawable.ic_edit_black_24dp,R.drawable.ic_settings_black_24dp,R.drawable.ic_help_outline_black_24dp,
            R.drawable.ic_logout__black_24dp};
    ListView list;
    Toolbar toolbar;
    TextView textViewName;
    TextView textTotalDistance;
    TextView textTotalActivity;
    private TextView textViewEmail;
    TextView textBio;

    CircularImageView profilePic;
    Button Logout;

    SharedPreferences sharedPref;

    private ProgressDialog progress;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore firestore;//yt
    private String userID;//yt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_profile);


        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Profile");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewName=findViewById(R.id.textViewName);
        textTotalDistance = findViewById(R.id.profile_total_points);
        textTotalActivity = findViewById(R.id.profile_activity_total);
        profilePic = findViewById(R.id.imageViewProfilePic);
        textViewEmail = findViewById(R.id.textViewMail);
        textBio = findViewById(R.id.profile_bioo);

        username = getIntent().getStringExtra("user");

        User user = UserData.getInstance().getUserByUsername(username);

        textViewName.setText(username);
        textViewEmail.setText(user.email);

        if (user.desc != null) {
            textBio.setText(user.desc);
        }

        Scores s = ScoresData.getInstance().getScore(username);
        if(s != null)
        {
            textTotalDistance.setText(s.alltimeDistance + "m");
            textTotalActivity.setText(s.alltimeActivity + " points");
        }

        if (user.image != null && !(user.image.compareTo("") == 0)) {
            Glide.with(this).load(user.image).into(profilePic);
        } else {
            profilePic.setImageResource(R.drawable.ic_account);
        }

        mfirebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = mfirebaseAuth.getCurrentUser();

        list = (ListView) findViewById(R.id.listview_profile_options);
        Context context = getApplicationContext();
        CustomListView adapter = new CustomListView(this, context.getResources().getStringArray(R.array.profile_options), imgid, false);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        Intent editProf =new Intent(getApplicationContext(), EditProfileActivity.class);
                        editProf.putExtra("username", username);
                        startActivityForResult(editProf,123);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(intent);
                        break;
                    case 3:

                        if(firebaseUser != null)
                        {
                            sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.remove(getString(R.string.loggedUser_email));
                            editor.remove(getString(R.string.loggedUser_username));
                            editor.remove("userImage");
                            editor.remove(getString(R.string.loggedUser_index));
                            editor.commit();

                            clearAllShared();

                            mfirebaseAuth.signOut();



                            Toast.makeText(getApplicationContext(), "LOGGED OUT", Toast.LENGTH_SHORT).show();

                            intent=new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "could not log out", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (123) : {
                if (resultCode == Activity.RESULT_OK) {
                    String imgUrl = data.getStringExtra("img");
                    if (imgUrl != null && !(imgUrl.compareTo("") == 0))
                        Glide.with(this).load(imgUrl).into(profilePic);

                    String newBio = data.getStringExtra("bio");
                    textBio.setText(newBio);

                }
                break;
            }
        }
    }

    void clearAllShared()
    {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.NotificationsFriend), Context.MODE_PRIVATE);
        int numOfNotis = sharedPref.getInt(getString(R.string.NotificationsNumber), 0);

        SharedPreferences.Editor editor = sharedPref.edit();
        for(int i = 0; i< numOfNotis; i++)
        {
            editor.remove(getString(R.string.NotificationsFromUser) + i);
            editor.remove(getString(R.string.NotificationsDate) + i);

        }
        editor.putInt(getString(R.string.NotificationsNumber), 0);
        editor.commit();


        SharedPreferences sharedPref2 = getApplicationContext().getSharedPreferences(
                getString(R.string.NotiObjects), Context.MODE_PRIVATE);
        numOfNotis = sharedPref.getInt(getString(R.string.NotiObjectsNumber), 0);
        editor = sharedPref2.edit();

        for(int i = 0; i< numOfNotis; i++)
        {

            editor.remove(getString(R.string.NotiObjectsFromUser) + i);
            editor.remove(getString(R.string.NotiObjectsDate) + i);
        }
        editor.putInt(getString(R.string.NotiObjectsNumber), 0);

        numOfNotis = sharedPref2.getInt(getString(R.string.NotiObjectsNumber) + "reactions", 0);

        for(int i = 0; i< numOfNotis; i++)
        {

            editor.remove(getString(R.string.NotiObjectsDate) + "reactions" + i);
        }
        editor.putInt(getString(R.string.NotiObjectsNumber) + "reactions", 0);
        editor.commit();


        sharedPref = getSharedPreferences(
                getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);

        int numm= sharedPref.getInt( getString(R.string.NumberOfSavedTotal), 0);
        editor = sharedPref.edit();

        for(int j = 0; j < numm; j++)
        {
            String s = sharedPref.getString(getString(R.string.SavedRoutesGroup) + j, "EMPTY");
            if(s.compareTo("EMPTY") != 0){
                int num = sharedPref.getInt( getString(R.string.NumberOfSavedGroup) + s, 0);
                for(int i = 0; i<num; i++)
                {
                    editor.remove(getString(R.string.SavedRoute) + s + i);

                }

                editor.remove(getString(R.string.SavedRoutesGroup) + j);
                editor.remove(getString(R.string.NumberOfSavedGroup) + s);


            }

        }

        editor.remove( getString(R.string.NumberOfSavedTotal));


        editor.commit();


    }
}
