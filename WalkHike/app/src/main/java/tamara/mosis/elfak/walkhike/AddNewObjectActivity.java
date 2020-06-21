
package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import tamara.mosis.elfak.walkhike.Fragments.AddCheckpoint_EditObject;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_SelectType;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_SetDetails;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_ShowInAR;

public class AddNewObjectActivity extends FragmentActivity implements View.OnClickListener {

    private int state = 0;
    Button btnPrev, btnNext;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_add_new_object);

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
                    AddCheckpoint_EditObject secondFragment = new AddCheckpoint_EditObject();
                    //bundle with arguments...

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, secondFragment)
                            .addToBackStack(null).commit();

                    progressBar.setProgress(100);
                } else if (state == 2) {
                    AddObject_SetDetails thirdFragment = new AddObject_SetDetails();
                    //bundle with arguments...

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
                    AddCheckpoint_EditObject secondFragment = new AddCheckpoint_EditObject();
                    //bundle with arguments...

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, secondFragment)
                            .addToBackStack(null).commit();
                    progressBar.setProgress(100);
                } else if (state == 1) {
                    state = 2;
                    AddObject_SetDetails thirdFragment = new AddObject_SetDetails();
                    //bundle with arguments...

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, thirdFragment)
                            .addToBackStack(null).commit();

                    progressBar.setProgress(200);

                    //if cbx_show_in_ar is not checked -> set next button text to done
                    //and change state to 3

                } else if (state == 2) {
                    state = 3;
                    AddObject_ShowInAR fourthFragment = new AddObject_ShowInAR();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fourthFragment)
                            .addToBackStack(null).commit();

                    btnNext.setText("Done");
                    progressBar.setProgress(300);
                } else if (state == 3) {
                    Toast.makeText(this, "Object added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }
}
