package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.Position;
import tamara.mosis.elfak.walkhike.modeldata.Scores;
import tamara.mosis.elfak.walkhike.modeldata.ScoresData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class SignUpActivity extends AppCompatActivity {

    Button Signin;
    EditText txtUsername;
    EditText txtEmail;
    EditText txtPassword;
    TextView orLogIn;

    UserData userData;
    ScoresData scoresData;

    private ProgressDialog progress;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    //private FirebaseStorage.getInstance().get.. --za slike
    private boolean successfull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_sign_up);

        userData.getInstance().getUsers();
       firebaseAuth = FirebaseAuth.getInstance();
       firestore=FirebaseFirestore.getInstance();

       progress = new ProgressDialog(this);

        txtPassword = (EditText)   findViewById(R.id.signup_password);
        txtEmail = (EditText)   findViewById(R.id.signup_email);
        txtUsername=(EditText) findViewById(R.id.signup_username);

        Signin = (Button) findViewById(R.id.signup_button_done);
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                registerUser();
            }
        });

        orLogIn = (TextView) findViewById(R.id.textViewLogin);
        orLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void registerUser()
    {
        String email = txtEmail.getText().toString();
        String pass = txtPassword.getText().toString();

        final String name=txtUsername.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "empty email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(getApplicationContext(), "pass empty", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.setMessage("registering...");
        progress.show();


        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {


                        User u = new User();
                        u.username =name;
                        u.email = email;
                        u.UserPosition = new Position("-5000", "-5000");
                        u.desc = "Add a description";
                        u.image = "";

                        userData.getInstance().AddUser(u);
                        Scores s ;
                        s = new Scores();
                        s.useer = email;
                        Scores ss = scoresData.getInstance().getScore(0);
                        if(ss != null)
                            s.datetimeWeek = ss.datetimeWeek;
                        else
                            s.datetimeWeek = new SimpleDateFormat("ddMMyyyyhhmmss").format(Calendar.getInstance().getTime());

                        scoresData.getInstance().AddScore(s);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Could not register! ", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> probepos = new ArrayList<>();
                    int indexx = -1;


                    probepos = userData.getInstance().getUsers();
                    for (int i = 0; i < probepos.size(); i++) {
                        String a = probepos.get(i).email;
                        if (a.compareTo(email) == 0) {
                            indexx = i;
                        }
                    }
                    User uu;
                    if (indexx != -1) {
                        uu = probepos.get(indexx);

                        Context context = getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                "Userdata", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.loggedUser_email), uu.email);
                        editor.putString(getString(R.string.loggedUser_username), uu.username);
                        editor.putString(getString(R.string.loggedUser_image), uu.image);

                        editor.putInt(getString(R.string.loggedUser_index), indexx);
                        editor.commit();



                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Could not login! ", Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();
            }
        });

    }
}
