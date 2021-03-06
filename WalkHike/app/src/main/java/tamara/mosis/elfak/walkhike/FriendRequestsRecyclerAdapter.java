package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;

public class FriendRequestsRecyclerAdapter extends RecyclerView.Adapter<FriendRequestsRecyclerAdapter.ViewHolder>{
    //private List<Users> usersList;
    private List<Friendship> users;
    private Context context;

    private String currentId;//ours
    private FirebaseFirestore mFirestore;

    FriendshipData friendshipData;

    public FriendRequestsRecyclerAdapter(Context context,List<Users> usersList)
    {
        //this.usersList=usersList;
        this.context=context;
    }
    public FriendRequestsRecyclerAdapter(List<Friendship> users, Context context)
    {
        this.users=users;
        this.context=context;
        friendshipData.getInstance().getFriendships();
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
        final String username;
        if(users == null)
        {// username=usersList.get(position).getName();
       // holder.username_view.setText(username);
            //final String user_id=usersList.get(position).userId;
        }
        else
        {   username=users.get(position).fromUser.username;
            holder.username_view.setText(username);

            String imgUrl = users.get(position).fromUser.image;
            RequestOptions placeholderOpt = new RequestOptions();
            placeholderOpt.placeholder(R.drawable.girl_1);
            Glide.with(context).setDefaultRequestOptions(placeholderOpt).load(imgUrl).into(holder.image_view);
        }



        holder.acceptButton.setOnClickListener
                (new View.OnClickListener()
                {
                    @Override
                    public  void onClick(final View view)
                    {
                        removeItem(holder.getAdapterPosition());
                        friendshipData.getInstance().updateFriendshipAccept(users.get(position).fromUser.email, users.get(position).toUser.email, true);
                        users.remove(position);
                    }
                });

        holder.declineButton.setOnClickListener
                (new View.OnClickListener()
                {
                    @Override
                    public  void onClick(final View view)
                    {
                        //removeItem(holder.getAdapterPosition());
                        removeItem(holder.getAdapterPosition());
                        friendshipData.getInstance().deleteFriendship(users.get(position).fromUser.email, users.get(position).toUser.email);
                        users.remove(position);
                    }
                });
    }

    @Override
    public int getItemCount() {
        //return usersList.size();
        return users.size();
    }

    private void removeItem(int position) {
       //usersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, users.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View vie;
        private TextView username_view;
        private ImageButton acceptButton;
        private ImageButton declineButton;
        private ImageView image_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vie=itemView;
            username_view=(TextView)vie.findViewById(R.id.textViewRequest);
            acceptButton= (ImageButton) vie.findViewById(R.id.button_accept);
            declineButton=  (ImageButton)vie.findViewById(R.id.button_decline);
            image_view=vie.findViewById(R.id.imageViewRequestPhoto);


        }
    }
}
