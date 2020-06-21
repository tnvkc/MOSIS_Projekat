package tamara.mosis.elfak.walkhike.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.HashMap;
import java.util.Map;

import tamara.mosis.elfak.walkhike.CustomListView;
import tamara.mosis.elfak.walkhike.R;

public class ProfileActivity extends AppCompatActivity {

    Integer[] imgid={R.drawable.ic_edit_black_24dp,R.drawable.ic_settings_black_24dp,R.drawable.ic_help_outline_black_24dp,
            R.drawable.ic_logout__black_24dp};
    ListView list;
    Toolbar toolbar;
    TextView textViewName;

    Button Logout;

    private ProgressDialog progress;
    private FirebaseAuth firebaseAuth;
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

       textViewName=findViewById(R.id.textViewName);
       firebaseAuth = FirebaseAuth.getInstance();//i yt
        firestore=FirebaseFirestore.getInstance();//yt
        //Donsfl2GlXYkLvPvrhsWSAZAzvg2
        userID=firebaseAuth.getCurrentUser().getUid();//yt
        firestore.collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String username=documentSnapshot.getString("name");
                    textViewName.setText(username);
              }
           }
        );

       final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

       toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Profile");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
       list = (ListView) findViewById(R.id.listview_profile_options);
        Context context = getApplicationContext();
       CustomListView adapter = new CustomListView(this, context.getResources().getStringArray(R.array.profile_options), imgid, false);
       list.setAdapter(adapter);

       /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                        startActivity(intent);
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
                        //intent=new Intent(getApplicationContext(), EditProfileActivity.class);
                        // startActivity(intent);
                        Map<String,Object> tokenMapRemove=new HashMap<>();
                        tokenMapRemove.put("token_id", FieldValue.delete());
                        firestore.collection("Users").document(userID).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firebaseAuth.signOut();
                                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        if(firebaseUser != null)
                        {

                            Toast.makeText(getApplicationContext(), "LOGGED OUT", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "could not log out", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                }
            }
        });*/


    }
}
