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

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Activities.MainActivity;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;

public class SearchResultsListAdapter extends ArrayAdapter<MapObject> implements View.OnClickListener {

    MainActivity mainActivity;
    Context context;
    int resource;

    public SearchResultsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MapObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public SearchResultsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MapObject> objects, MainActivity activity) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.mainActivity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MapObject obj = getItem(position);
        if (obj != null) {
            int object_type = obj.objectType;
            String searched_text = obj.desc; //za user-e desc = username!
            if (object_type == 5) {
                String user_photo = obj.photo; //zapravo bi trebalo iz UserData!
            }

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            ImageView search_result_iv = (ImageView) convertView.findViewById(R.id.search_result_icon);
            TextView search_result_tv = (TextView) convertView.findViewById(R.id.search_result_text);
            TextView search_result_distance_tv = (TextView) convertView.findViewById(R.id.search_result_distance);
            Button search_result_btn = (Button) convertView.findViewById(R.id.search_result_btn);

            switch (object_type) {
                case 1:
                    search_result_iv.setImageResource(R.drawable.ic_message);
                    break;
                case 2:
                    search_result_iv.setImageResource(R.drawable.ic_photo);
                    break;
                case 3:
                    search_result_iv.setImageResource(R.drawable.ic_marker);
                    break;
                case 4:
                    search_result_iv.setImageResource(R.drawable.ic_heart);
                    break;
                case 5:
                    break; //ovde treba user image!
            }

            search_result_tv.setText(searched_text);

            search_result_btn.setTag(getItem(position));
            search_result_btn.setOnClickListener(this);

            Location currentUserLocation = mainActivity.getLocation();

            Location objectLocation = new Location("");
            objectLocation.setLatitude(Double.parseDouble(obj.position.latitude));
            objectLocation.setLongitude(Double.parseDouble(obj.position.longitude));

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
