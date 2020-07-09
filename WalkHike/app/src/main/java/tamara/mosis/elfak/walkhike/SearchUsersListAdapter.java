package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Activities.MainActivity;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.Position;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class SearchUsersListAdapter extends ArrayAdapter<User> implements View.OnClickListener {

    MainActivity mainActivity;
    Context context;
    int resource;

    public SearchUsersListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public SearchUsersListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects, MainActivity activity) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.mainActivity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User obj = getItem(position);
        if (obj != null) {
            String searched_text = obj.username; //za user-e desc = username!
            String user_photo = obj.image;

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            ImageView search_result_iv = (ImageView) convertView.findViewById(R.id.search_result_icon);
            TextView search_result_tv = (TextView) convertView.findViewById(R.id.search_result_text);
            TextView search_result_distance_tv = (TextView) convertView.findViewById(R.id.search_result_distance);
            Button search_result_btn = (Button) convertView.findViewById(R.id.search_result_btn);

            if (user_photo != null && user_photo.compareTo("") != 0)
                Glide.with(context).load(user_photo).into(search_result_iv);

            search_result_tv.setText(searched_text);

            search_result_btn.setTag(getItem(position));
            search_result_btn.setOnClickListener(this);

            Location currentUserLocation = mainActivity.getLocation();

            Location objectLocation = new Location("");
            objectLocation.setLatitude(Double.parseDouble(obj.UserPosition.latitude));
            objectLocation.setLongitude(Double.parseDouble(obj.UserPosition.longitude));

            float distance = currentUserLocation.distanceTo(objectLocation);
            search_result_distance_tv.setText(String.format("distance: %.2fm", distance));

        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        return convertView;

    }

    @Override
    public void onClick(View v) {
        mainActivity.FindObjectOnMap((MapObject) v.getTag());
    }
}
