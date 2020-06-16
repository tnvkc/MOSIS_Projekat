package tamara.mosis.elfak.walkhike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button loginB;
    EditText txtEmail;
    EditText txtPassword;

    private ProgressDialog progress;
    private FirebaseAuth firebaseAuth;
    FirebaseUser logedInUser;

    @Override
    protected void onStart()
    {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        logedInUser = firebaseAuth.getCurrentUser();
        if(logedInUser != null)
        {
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);
        txtPassword = (EditText)   findViewById(R.id.login_edit_pass);
        txtEmail = (EditText)   findViewById(R.id.login_edit_email);


        loginB = (Button) findViewById(R.id.login_button_login);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "done login", Toast.LENGTH_SHORT).show();
                loginUser();
            }
        });
    }
    private void loginUser()
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

        progress.setMessage("Login in...");
        progress.show();

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "loged in! " + txtEmail.getText() +  txtPassword.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
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
