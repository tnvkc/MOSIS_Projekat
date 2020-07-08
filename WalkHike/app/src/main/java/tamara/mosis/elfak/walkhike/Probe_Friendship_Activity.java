package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.IntentService;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.Position;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class Probe_Friendship_Activity extends AppCompatActivity  {

    Button btnDodajUser;

    Button btnPrikaziUsere;
    Button btnDodajFriendship;

    Button btnPrikaziFriends;
    Button btnAcceptFreidns;
    TextView prikaz;

    TextInputEditText username;
    TextInputEditText user1Index;
    TextInputEditText user2Index;

    Button btnDodajScores;
    Button btnShowScores;
    Button btnChangeScores;
    TextInputEditText scoreNew;
    TextInputEditText scoreUser;

    UserData userdata;
    FriendshipData friendshipData;
    ScoresData scoresData;
    String user = "UserProba1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userdata.getInstance().getUsers();
        friendshipData.getInstance().getFriendships();
        setContentView(R.layout.activity_probe__friendship_);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);





        username =  (TextInputEditText) findViewById(R.id.probefriend_name_edit);
        user1Index =  (TextInputEditText) findViewById(R.id.probefriend_user1_edit);
        user2Index =  (TextInputEditText) findViewById(R.id.probefriend_user2_edit);


        btnDodajUser = (Button) findViewById(R.id.probefriend_add_user);
        btnDodajUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User p = new User(username.getText().toString());

                userdata.getInstance().AddUser(p);
            }
        });

        btnPrikaziUsere =  (Button) findViewById(R.id.probefriends_btn_show_users);
        btnPrikaziUsere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<User> probepos = new ArrayList<>();


                probepos = userdata.getInstance().getUsers();
                prikaz = (TextView) findViewById(R.id.probefriend_show_text);
                String prikaziii = "";



                for (int i = 0; i < probepos.size(); i++) {
                    prikaziii += probepos.get(i).username + " ";
                    prikaziii += "\n";

                }

                prikaz.setText(prikaziii);
            }
        });


//////////friendships


        btnDodajFriendship = (Button) findViewById(R.id.probefriend_btn_add_friendship);
        btnDodajFriendship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Friendship p = new Friendship();
                int index1 = Integer.parseInt(user1Index.getText().toString());
                p.fromUser = userdata.getInstance().getUser(index1);
                index1 = Integer.parseInt(user2Index.getText().toString());
                p.toUser = userdata.getInstance().getUser(index1);

                friendshipData.getInstance().AddFriendship(p);
            }
        });

        btnPrikaziFriends =  (Button) findViewById(R.id.probefriend_btn_show_friends);
        btnPrikaziFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFriendships();
            }
        });


        btnAcceptFreidns = (Button) findViewById(R.id.probefriend_btn_accept);
        btnAcceptFreidns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String index1 =  user1Index.getText().toString();
                String index2 = user2Index.getText().toString();
                int indexx = -1;
                ArrayList<Friendship> probepos = new ArrayList<>();


                probepos = friendshipData.getInstance().getFriendships();
                for(int i =0; i<probepos.size(); i++)
                {
                    String a = probepos.get(i).fromUser.email;
                    if( a.compareTo(index1) == 0)
                    {
                        if(probepos.get(i).toUser.email.compareTo(index2) == 0)
                            indexx = i;
                    }
                }

                if(indexx != -1)
                    friendshipData.getInstance().updateFriendshipTrue(indexx, false);
            }
        });


        //////////scores
        prikaz = (TextView) findViewById(R.id.probefriend_show_text);
        scoreNew =  (TextInputEditText) findViewById(R.id.probefriend_scores_textedit);
        scoreUser =  (TextInputEditText) findViewById(R.id.probefriend_scores_user);
        //user2Index =  (TextInputEditText) findViewById(R.id.probefriend_user2_edit);

        btnDodajScores = (Button) findViewById(R.id.probefriend_btn_add_scores);
        btnDodajScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Scores p = new Scores();
                //ArrayList<User> probepos;


                //probepos = userdata.getInstance().getUsers();
                /*for(int i =0; i<probepos.size(); i++)
                {
                    p = scoresData.getInstance().getScore(probepos.get(i).email);
                    if(p == null)
                    {
                        Scores s ;
                        s = new Scores();
                        s.useer = probepos.get(i).email;
                        if(scoresData.getInstance().getScores().size() != 0) {
                            Scores ss = scoresData.getInstance().getScore(0);
                            s.datetimeWeek = ss.datetimeWeek;
                        }
                        else
                            s.datetimeWeek = "02022020000000";
                            s.datetimeMonth = "02022020000000";//new SimpleDateFormat("ddMMyyyyhhmmss").format(Calendar.getInstance().getTime());

                        scoresData.getInstance().AddScore(s);
                    }
                    else {
                        try {
                           // DateFormat format = new SimpleDateFormat("ddMMyyyy");
                           // Date objectDate = null;
                            //objectDate = format.parse("12022020");

                            scoresData.getInstance().updateScoreDistance(200, probepos.get(i));
                        }catch (Exception e)
                        {

                        }

                    }
                }*/

                scoresData.getInstance().updateScoresDates();



            }
        });

        btnShowScores =  (Button) findViewById(R.id.probefriend_btn_show_scores);
        btnShowScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Scores> skores = scoresData.getInstance().getScores();
                String prikaziii = "";
                for(int i =0; i<skores.size();i++)
                {
                    prikaziii += skores.get(i).toString() + "\n";
                }
                prikaz.setText(prikaziii);
            }
        });

        btnChangeScores = (Button) findViewById(R.id.probefriend_btn_scores_change);
        btnChangeScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Scores p = scoresData.getInstance().getScore(0);
                DateFormat format = new SimpleDateFormat("ddMMyyyyhhmmss");
                Date currentDate = Calendar.getInstance().getTime();


                Date objectDate = null;
                Date objectDateMonth = null;


                /*try {
                    objectDate = format.parse(p.datetimeWeek);


                    long difference = currentDate.getTime() - objectDate.getTime();
                    long dayDifference = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

                    if (dayDifference > 7) {
                        int dayOftheweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

                        if(dayOftheweek == 1) //pomera se jer je kod njih prvi dan u nedelji nedelja, pa je SUNDAY = 1, MONDAY = 2 itd
                            dayOftheweek = 7;
                        String newDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime().getTime() - (dayOftheweek - 2) * 24*60*60*1000);
                        newDate += "000000";
                        scoresData.getInstance().updateResetScoresWeekly(newDate);

                        objectDateMonth = format.parse(p.datetimeMonth);

                        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

                        int lastUpdateMonth = Integer.parseInt(p.datetimeMonth.substring(2, 3));
                        int lastUpdateYear =  Integer.parseInt(p.datetimeMonth.substring(4, 7));

                        String newUpdateDate = "";
                        boolean reset = false;

                        if (currentMonth != lastUpdateMonth || currentYear != lastUpdateYear) {

                            if (currentMonth < 10)
                                newUpdateDate = "010" + String.valueOf(currentMonth) + String.valueOf(currentYear) + "000000";
                            else
                                newUpdateDate = "01" + String.valueOf(currentMonth) + String.valueOf(currentYear) + "000000";

                            reset = true;

                        } else {
                            newUpdateDate =  new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime());
                            reset = false;
                        }

                        scoresData.getInstance().updateResetScoresMonthly(newUpdateDate, reset);

                    }*/



                scoresData.getInstance().updateScoresDates();

                /*ArrayList<User> useri = UserData.getInstance().getUsers();
                Scores s ;
                for(int i =0; i< useri.size(); i++) {
                    s = new Scores();
                    s.useer = useri.get(i).username;
                    s.datetimeWeek = new SimpleDateFormat("ddMMyyyyhhmmss").format(Calendar.getInstance().getTime());
                    s.datetimeMonth =new SimpleDateFormat("ddMMyyyyhhmmss").format(Calendar.getInstance().getTime());


                    scoresData.getInstance().AddScore(s);
                }*/




                   /* objectDate = format.parse(p.datetimeMonth);


                    difference = currentDate.getTime() - objectDate.getTime();
                    dayDifference = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

                    if (dayDifference > 7) {
                        String newDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
                        newDate += "000000";
                        scoresData.getInstance().updateResetScoresMonthly(newDate);

                    }*/

               /* } catch (Exception e) {

                }*/

            }
        });
    }

    void showFriendships()
    {
        ArrayList<Friendship> probepos = new ArrayList<>();
        String index1 = user1Index.getText().toString();

        probepos = friendshipData.getInstance().getFriendships();
        prikaz = (TextView) findViewById(R.id.probefriend_show_text);
        String prikaziii = "";



        for (int i = 0; i < probepos.size(); i++) {
            String a = probepos.get(i).toUser.username;
            if (a.compareTo(user) == 0)
                prikaziii += probepos.get(i).toString() + " ";
            prikaziii += "\n";
        }

        prikaz.setText(prikaziii);
    }

    /*@Override
    public void onListUpdated() {
        showFriendships();
    }*/
}
