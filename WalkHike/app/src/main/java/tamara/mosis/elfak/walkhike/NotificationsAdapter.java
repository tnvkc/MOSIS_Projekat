package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<Notifications> notificationsList;
    private FirebaseFirestore firestore;
    private Context context;

    public NotificationsAdapter(Context context, List<Notifications> notList)
    {
        this.notificationsList=notList;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notification,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        firestore=FirebaseFirestore.getInstance();
        String from_id=notificationsList.get(position).getFrom();
        //holder.notifMessage.setText(notificationsList.get(position).getMessage());

        firestore.collection("Users").document(from_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name=documentSnapshot.getString("name");
               // String image=documentSnapshot.getString("image");

                holder.notifName.setText(name);

               // RequestOptions reqOptions= new RequestOptions();
               // reqOptions.placeholder(R.mipmap.default_image);

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;

        public TextView notifName;
        public TextView notifMessage;
        public  ViewHolder (View v)
        {
            super(v);
            view=v;

           // notifMessage=(TextView)view.findViewById(R.id.notif_message);
            notifName=(TextView)view.findViewById(R.id.notif_name);

        }
    }
}
