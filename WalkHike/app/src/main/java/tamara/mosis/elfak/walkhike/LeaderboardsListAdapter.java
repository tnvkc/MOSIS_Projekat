package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class LeaderboardsListAdapter extends ArrayAdapter<Leaderboard_Entry> {

    Context context;
    int resource;

    public LeaderboardsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Leaderboard_Entry> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String username = getItem(position).getUsername();
        String points = getItem(position).getPoints();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        ImageView user_image_tv = (ImageView) convertView.findViewById(R.id.leaderboard_user_image);
        TextView user_name_tv = (TextView) convertView.findViewById(R.id.leaderboard_user_name);
        TextView points_tv = (TextView) convertView.findViewById(R.id.leaderboard_entry_points);

        user_image_tv.setImageResource(R.drawable.boy); //svima ovo za sada
        user_name_tv.setText(username);
        points_tv.setText(points);

        return convertView;
    }
}