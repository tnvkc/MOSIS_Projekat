package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.models.Position;
import tamara.mosis.elfak.walkhike.models.WalkHikeData;

public class Probe extends AppCompatActivity {

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
        setContentView(R.layout.activity_probe);

        lon =  (TextInputEditText) findViewById(R.id.probe_long);
        lat =  (TextInputEditText) findViewById(R.id.probe_lat);
        descc =  (TextInputEditText) findViewById(R.id.probe_desc);


        btnDodaj = (Button) findViewById(R.id.probe_dodaj);
        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position p = new Position(descc.getText().toString());
                p.latitude = lat.getText().toString();
                p.longitude = lon.getText().toString();
                w.getInstance().AddPosition(p);
            }
        });

        btnPrikazi =  (Button) findViewById(R.id.probe_prikazi);
        btnPrikazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                probepos = new ArrayList<>();
                probepos = w.getInstance().getMyPlaces();
                prikaz = (TextView) findViewById(R.id.probe_textview);
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
