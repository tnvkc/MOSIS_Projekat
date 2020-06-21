package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Activities.LoginActivity;
import tamara.mosis.elfak.walkhike.Activities.SignUpActivity;

import tamara.mosis.elfak.walkhike.modeldata.*;

public class Probe extends AppCompatActivity {


    Button signupB;
    Button loginB;

    Button btnDodaj;
    Button btnPrikazi;
    TextView prikaz;

    TextInputEditText lon;
    TextInputEditText lat;
    TextInputEditText descc;

    static WalkHikeData w;
    ArrayList<Position> probepos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_probe);



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

                for(int i = 0; i<probepos.size(); i++)
                {
                    prikaziii+= probepos.get(i).desc + probepos.get(i).longitude + probepos.get(i).latitude + " ";
                }
                prikaz.setText(prikaziii);
            }
        });



    }
}
