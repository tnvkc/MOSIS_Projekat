package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;
import android.os.Handler;

public class LauncherActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_launcher);

        firebaseAuth = FirebaseAuth.getInstance();
        userData.getInstance().getUsers();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        String emaill = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        String image = sharedPref.getString(getString(R.string.loggedUser_image), "EMPTY");
        int indexx  = sharedPref.getInt(getString(R.string.loggedUser_index), -1);

        Scores s = null;

        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null && emaill.compareTo("EMPTY") != 0)
        {

            User u = userData.getInstance().getUser(emaill);


            Intent loginIntent=new Intent(LauncherActivity.this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                }
            }, 1500);
        }
    }
}
