package tamara.mosis.elfak.walkhike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    Button Signin;
    EditText txtUsername;
    EditText txtEmail;
    EditText txtPassword;
    private ProgressDialog progress;
    private FirebaseAuth firebaseAuth;
    private boolean successfull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_sign_up);

       firebaseAuth = FirebaseAuth.getInstance();

       progress = new ProgressDialog(this);

        txtPassword = (EditText)   findViewById(R.id.signup_password);
        txtEmail = (EditText)   findViewById(R.id.signup_email);

        Signin = (Button) findViewById(R.id.signup_button_done);
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                registerUser();
            }
        });
    }

    private void registerUser()
    {
        String email = txtEmail.getText().toString();
        String pass = txtPassword.getText().toString();

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
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Signed in! " + txtEmail.getText() +  txtPassword.getText(), Toast.LENGTH_SHORT).show();
                        successfull = true;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Could not register! ", Toast.LENGTH_SHORT).show();
                    }

            }
        });
        if(successfull) {
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "loged in! " + txtEmail.getText() +  txtPassword.getText(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Could not login! ", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }
            });
        }
    }
}
