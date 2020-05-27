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

public class NotificationsListAdapter extends ArrayAdapter<Notification> {

    Context context;
    int resource;

    public NotificationsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Notification> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        int icon_id = getItem(position).getIcon();
        String notification_text = getItem(position).getNotification_text();
        String notification_time = getItem(position).getNotification_time();

        Notification notification = new Notification(icon_id, notification_text, notification_time);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        ImageView icon_iv = (ImageView) convertView.findViewById(R.id.notification_icon);
        TextView notification_text_tv = (TextView) convertView.findViewById(R.id.notification_text);
        TextView notification_time_tv = (TextView) convertView.findViewById(R.id.notification_time);

        switch (icon_id) {
            case 1: icon_iv.setImageResource(R.drawable.ic_trophy); break;
            case 2: icon_iv.setImageResource(R.drawable.ic_marker); break;
        }

        notification_text_tv.setText(notification_text);
        notification_time_tv.setText(notification_time);

        return convertView;
    }
}
