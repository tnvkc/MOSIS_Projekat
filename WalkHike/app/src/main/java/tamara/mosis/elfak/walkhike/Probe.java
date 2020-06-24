package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Map;

import tamara.mosis.elfak.walkhike.Activities.LoginActivity;
import tamara.mosis.elfak.walkhike.Activities.MainActivity;
import tamara.mosis.elfak.walkhike.Activities.SignUpActivity;

import tamara.mosis.elfak.walkhike.modeldata.*;

public class Probe extends AppCompatActivity {

    Button mainBtn;
    Button probeFriends;

    Button signupB;
    Button loginB;

    Button btnDodaj;
    Button btnPrikazi;
    TextView prikaz;

    Button btnStartService;
    Button btnStopService;


    TextInputEditText lon;
    TextInputEditText lat;
    TextInputEditText descc;


    Button btnGetIndex;
    Button btnDeleteIndex;
    Button btnUpdateIndex;
    TextInputEditText textIndex;

    static PositionsData w;
    static MapObjectData md;
    ArrayList<Position> probepos;
    ArrayList<MapObject> mapObjcs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        w.getInstance().getMyPlaces();

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_probe);


        mainBtn = (Button) findViewById(R.id.probe_btn_main);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        probeFriends = (Button) findViewById(R.id.probe_friends);
        probeFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Probe_Friendship_Activity.class);
                startActivity(intent);
            }
        });


        signupB = (Button) findViewById(R.id.proba_signup);
        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        loginB = (Button) findViewById(R.id.proba_login);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        lon =  (TextInputEditText) findViewById(R.id.probe_long_edit);
        lat =  (TextInputEditText) findViewById(R.id.probe_lat_edit);
        descc =  (TextInputEditText) findViewById(R.id.probe_desc_edit);


        btnDodaj = (Button) findViewById(R.id.probe_add_longlat);
        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position p = new Position(descc.getText().toString());
                p.latitude = lat.getText().toString();
                p.longitude = lon.getText().toString();
                w.getInstance().AddPosition(p);


                MapObject m = new MapObject();
                m.desc = descc.getText().toString();
                m.position = p;
                md.getInstance().AddMapObject(m);
            }
        });

        btnPrikazi =  (Button) findViewById(R.id.probe_btn_show);
        btnPrikazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                probepos = new ArrayList<>();


                probepos = w.getInstance().getMyPlaces();
                prikaz = (TextView) findViewById(R.id.probe_show_text);
                String prikaziii ="";

                mapObjcs = new ArrayList<>();
                mapObjcs = md.getInstance().getMyPlaces();



                for(int i = 0; i<probepos.size(); i++)
                {
                    prikaziii+= probepos.get(i).desc + probepos.get(i).longitude + probepos.get(i).latitude + " ";
                    prikaziii += "\n";

                }
                prikaziii += "\n";
                for(int i =0; i< mapObjcs.size(); i++)
                {
                    prikaziii += mapObjcs.get(i).toString();
                }
                prikaz.setText(prikaziii);
            }
        });




        ///////get 1 delete 1
        textIndex =  (TextInputEditText) findViewById(R.id.probe_index_edit);


        btnGetIndex = (Button) findViewById(R.id.probe_btn_getIndex);
        btnGetIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position p ;
                String prikaziii ="";
                int ii = Integer.parseInt(  textIndex.getText().toString());
                p = w.getInstance().getPlace(ii);
                prikaziii += p.toString();

                prikaz.setText(prikaziii);

            }
        });

        btnUpdateIndex = (Button) findViewById(R.id.probe_btn_updateIndex);
        btnUpdateIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position p = new Position(descc.getText().toString());
                p.latitude = lat.getText().toString();
                p.longitude = lon.getText().toString();

                String prikaziii ="";
                int ii = Integer.parseInt(  textIndex.getText().toString());
                w.getInstance().updatePlace(ii, p.desc, p.longitude, p.latitude);
                prikaziii += "updated: " + p.toString();

                prikaz.setText(prikaziii);

            }
        });


        btnDeleteIndex = (Button) findViewById(R.id.probe_btn_deleteIndex);
        btnDeleteIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ii = Integer.parseInt(  textIndex.getText().toString());
                w.getInstance().deletePlace(ii);

            }
        });


        ///////service


        btnStartService = (Button) findViewById(R.id.probe_btn_start_service);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Start service! ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), NotificationService.class);
                i.putExtra("timer", 10);
                startService(i);
            }
        });


        btnStopService = (Button) findViewById(R.id.probe_btn_stop_service);
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Stop service! ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), NotificationService.class);

                stopService(i);
            }
        });

    }
}
