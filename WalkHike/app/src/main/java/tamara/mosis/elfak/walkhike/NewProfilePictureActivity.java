package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class NewProfilePictureActivity extends AppCompatActivity {

    Integer[] imgid={R.drawable.ic_photo_camera_black_24dp,R.drawable.ic_image_black_24dp};
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_picture);

        list=(ListView) findViewById(android.R.id.list);
        Context context=getApplicationContext();
        CustomListView adapter=new CustomListView(this,context.getResources().getStringArray(R.array.new_profile_image_options),
                                                            imgid,false);
        list.setAdapter(adapter);

    }
}
