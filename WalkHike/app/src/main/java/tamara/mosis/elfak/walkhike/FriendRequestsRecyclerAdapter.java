package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendRequestsRecyclerAdapter extends RecyclerView.Adapter<FriendRequestsRecyclerAdapter.ViewHolder>{
    private List<Users> usersList;
    private Context context;

    private String currentId;//ours
    private FirebaseFirestore mFirestore;

    public FriendRequestsRecyclerAdapter(Context context,List<Users> usersList)
    {
        this.usersList=usersList;
        this.context=context;
    }
    @Override
    public FriendRequestsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member_accept_decline,parent,false);
        return new FriendRequestsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendRequestsRecyclerAdapter.ViewHolder holder, int position)
    {
        final String username=usersList.get(position).getName();
        holder.username_view.setText(username);
        final String user_id=usersList.get(position).userId;

        holder.acceptButton.setOnClickListener
                (new View.OnClickListener()
                {
                    @Override
                    public  void onClick(final View view)
                    {
                        removeItem(holder.getAdapterPosition());
                    }
                });

        holder.declineButton.setOnClickListener
                (new View.OnClickListener()
                {
                    @Override
                    public  void onClick(final View view)
                    {
                        removeItem(holder.getAdapterPosition());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    private void removeItem(int position) {
        usersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, usersList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View vie;
        private TextView username_view;
        private Button acceptButton;
        private Button declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vie=itemView;
            username_view=(TextView)vie.findViewById(R.id.textViewRequest);
            acceptButton=vie.findViewById(R.id.button_accept);
            declineButton=vie.findViewById(R.id.button_decline);


        }
    }
}
