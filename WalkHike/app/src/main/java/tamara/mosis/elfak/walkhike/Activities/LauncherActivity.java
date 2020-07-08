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
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;
import tamara.mosis.elfak.walkhike.modeldata.PositionsData;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;
import android.os.Handler;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LauncherActivity extends AppCompatActivity implements
        PositionsData.ReadyEventListener, UserData.ReadyEventListener, ScoresData.ReadyEventListener, MapObjectData.ReadyEventListener, FriendshipData.ReadyEventListener {

    FirebaseAuth firebaseAuth;
    UserData userData;
    MapObjectData mapObjectData;
    ScoresData scoresData;
    PositionsData positionsData;

    ArrayList<Scores> skorovi;
    boolean nesto = true;
    int sviDataok = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_launcher);

        firebaseAuth = FirebaseAuth.getInstance();



        PositionsData.getInstance().setReadyList(this);
        UserData.getInstance().setReadyList(this);
        FriendshipData.getInstance().setReadyList(this);
        MapObjectData.getInstance().setReadyList(this);
        ScoresData.getInstance().setReadyList(this);




        if(UserData.getInstance().getUsers().size() > 0 && FriendshipData.getInstance().getFriendships().size() >0
                && MapObjectData.getInstance().getMapObjects().size() >0 && ScoresData.getInstance().getScores().size() >0)
        {
            go();
        }





    }

    void go()
    {
        proveriScores();

        SharedPreferences  sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
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
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.remove(getString(R.string.loggedUser_email));
                    editor.remove(getString(R.string.loggedUser_username));
                    editor.remove("userImage");
                    editor.remove(getString(R.string.loggedUser_index));
                    editor.commit();
                    Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                }
            }, 500);
        }


    }

    @Override
    public void onReady() {
            sviDataok++;

            if(sviDataok == 5)
            {
                go();
                sviDataok = 0;
            }
    }

    void proveriScores()
    {
        DateFormat format = new SimpleDateFormat("ddMMyyyyhhmmss");
        Date currentDate = Calendar.getInstance().getTime();


        Date objectDate = null;
        Date objectDateMonth = null;
        Scores p = null;
        if(scoresData.getInstance().getScores().size() >0)
                p  = scoresData.getInstance().getScore(0);

        if(p != null) {
            try {
                objectDate = format.parse(p.datetimeWeek);


                long difference = currentDate.getTime() - objectDate.getTime();
                long dayDifference = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

                if (dayDifference > 7) {
                    int dayOftheweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

                    if (dayOftheweek == 1) //pomera se jer je kod njih prvi dan u nedelji nedelja, pa je SUNDAY = 1, MONDAY = 2 itd
                        dayOftheweek = 7;
                    String newDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime().getTime() - (dayOftheweek - 2) * 24 * 60 * 60 * 1000);
                    newDate += "000000";
                    scoresData.getInstance().updateResetScoresWeekly(newDate);
                }


                objectDateMonth = format.parse(p.datetimeMonth);

                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);

                int lastUpdateMonth = Integer.parseInt(p.datetimeMonth.substring(2, 3));
                int lastUpdateYear = Integer.parseInt(p.datetimeMonth.substring(4, 7));

                String newUpdateDate = "";
                boolean reset = false;

                if (currentMonth != lastUpdateMonth || currentYear != lastUpdateYear) {

                    if (currentMonth < 10)
                        newUpdateDate = "010" + String.valueOf(currentMonth) + String.valueOf(currentYear) + "000000";
                    else
                        newUpdateDate = "01" + String.valueOf(currentMonth) + String.valueOf(currentYear) + "000000";

                    reset = true;


                    scoresData.getInstance().updateResetScoresMonthly(newUpdateDate, reset);
                }

            } catch (Exception e) {

            }
        }
    }
}
