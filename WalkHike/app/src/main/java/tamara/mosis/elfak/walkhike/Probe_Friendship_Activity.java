package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.IntentService;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

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

        btnDodajUser = (Button) findViewById(R.id.probefriend_btn_add_scores);
        btnDodajUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scores p = new Scores();
                int index1 = Integer.parseInt( scoreNew.getText().toString());
                String userEmail = scoreUser.getText().toString();

                ArrayList<User> probepos = new ArrayList<>();


                probepos = userdata.getInstance().getUsers();
                for(int i =0; i<probepos.size(); i++)
                {
                    String a = probepos.get(i).email;
                    if( a.compareTo(userEmail) == 0)
                    {
                        p.user = probepos.get(i);
                    }
                }


                p.monthlyActivity += index1;
                scoresData.getInstance().AddScore(p);

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

                User u = userdata.getInstance().getUser(scoreUser.getText().toString());
                if(u != null)
                {
                    scoresData.getInstance().updateScoreActivity(133, u);
                    scoresData.getInstance().updateScoreDistance(12, u);
                }

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
