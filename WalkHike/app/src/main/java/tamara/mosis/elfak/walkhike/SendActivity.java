package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SendActivity extends AppCompatActivity {

    private TextView user_id_view;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        user_id_view=findViewById(R.id.user_id_view);
        userId=getIntent().getStringExtra("user_id");
        user_id_view.setText(userId);
    }
}
