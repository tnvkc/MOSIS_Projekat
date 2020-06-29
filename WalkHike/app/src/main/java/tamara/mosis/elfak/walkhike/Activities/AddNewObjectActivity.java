package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tamara.mosis.elfak.walkhike.Fragments.AddObject_EditDesc;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_InsertPhoto;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_SelectType;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_SetDetails;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_ShowInAR;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;
import tamara.mosis.elfak.walkhike.modeldata.Position;
import tamara.mosis.elfak.walkhike.modeldata.User;

public class AddNewObjectActivity extends FragmentActivity implements View.OnClickListener {

    static MapObjectData md;

    private int state = 0;
    Button btnPrev, btnNext;
    ProgressBar progressBar;
    int objectType;
    boolean isPublic;
    String desc;
    Position position;
    User loggedUser;

    AddObject_EditDesc secondFragment;

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int type) {
        objectType = type;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_add_new_object);

        Intent callerIntent = getIntent();
        Bundle args = callerIntent.getExtras();
        double lat = args.getDouble("lat");
        double lon = args.getDouble("lon");
        position = new Position();
        position.latitude = String.valueOf(lat);
        position.longitude = String.valueOf(lon);
        loggedUser = (User) args.getSerializable("user");

        Toast.makeText(this, loggedUser.email, Toast.LENGTH_SHORT).show();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(300);

        btnPrev = findViewById(R.id.add_object_prev);
        btnNext = findViewById(R.id.add_object_next);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        btnPrev.setEnabled(false); //inicijalno

        AddObject_SelectType firstFragment = new AddObject_SelectType();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_object_prev: {
                //go to prev fragment
                btnNext.setEnabled(true);
                state--;
                if (state == 0) {
                    btnPrev.setEnabled(false);

                    AddObject_SelectType firstFragment = new AddObject_SelectType();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, firstFragment)
                            .addToBackStack(null).commit();

                    progressBar.setProgress(0);
                } else if (state == 1) {
                    secondFragment = new AddObject_EditDesc();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, secondFragment)
                            .addToBackStack(null).commit();

                    btnNext.setText("Next");
                    progressBar.setProgress(100);
                } else if (state == 2) {
                    AddObject_SetDetails thirdFragment = new AddObject_SetDetails();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, thirdFragment)
                            .addToBackStack(null).commit();

                    btnNext.setText("Next");
                    progressBar.setProgress(200);
                }
                break;
            }
            case R.id.add_object_next: {
                btnPrev.setEnabled(true);
                if (state == 0) {
                    state = 1;
                    Toast.makeText(this, "Object type is " + objectType, Toast.LENGTH_LONG).show();

                    secondFragment = new AddObject_EditDesc();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, secondFragment)
                            .addToBackStack(null).commit();

                    if (objectType == 1) {
                        progressBar.setMax(200);
                    } else {
                        progressBar.setMax(300);
                    }

                    progressBar.setProgress(100);
                } else if (state == 1) {
                    state = 2;

                    desc = secondFragment.getDesc();
                    Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();

                    AddObject_SetDetails thirdFragment = new AddObject_SetDetails();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, thirdFragment)
                            .addToBackStack(null).commit();

                    progressBar.setProgress(200);

                    if (objectType == 1) {
                        btnNext.setText("Done");
                        state++;
                    }

                } else if (state == 2) {
                    state = 3;

                    if (objectType == 3) {
                        AddObject_InsertPhoto fourthFragment = new AddObject_InsertPhoto();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fourthFragment)
                                .addToBackStack(null).commit();
                    } else {
                        AddObject_ShowInAR fourthFragment = new AddObject_ShowInAR();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fourthFragment)
                                .addToBackStack(null).commit();
                    }
                    btnNext.setText("Done");
                    progressBar.setProgress(300);
                } else if (state == 3) {
                    MapObject newMapObject = new MapObject();
                    newMapObject.createdBy = loggedUser;
                    newMapObject.objectType = objectType;
                    newMapObject.isPublic = false; //isPublic;
                    //newMapObject.shareWithUser = user to share with or null (if isPublic = true)
                    newMapObject.desc = desc;
                    //newMapObject.photo = inserted photo if objectType = 3 or null
                    newMapObject.position = position;
                    newMapObject.datetime = new SimpleDateFormat("ddMMyyyyhhmmss").format(Calendar.getInstance().getTime());
                    newMapObject.date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                    md.getInstance().AddMapObject(newMapObject);

                    Toast.makeText(this, "Object added", Toast.LENGTH_SHORT).show();

                    Intent callerIntent = getIntent();
                    callerIntent.putExtra("map_object", newMapObject);

                    setResult(RESULT_OK, callerIntent);
                    finish();
                }
                break;
            }
        }
    }
}
