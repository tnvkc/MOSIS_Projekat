package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;


public class OtherUsersRecyclerAdapter extends RecyclerView.Adapter<OtherUsersRecyclerAdapter.ViewHolderOther>
{
    private List<User> usersList;
    private Context context;

    private String currentId;//ours
    private FirebaseFirestore mFirestore;

    FriendshipData friendshipData;
    UserData userData;
    User current;
    public OtherUsersRecyclerAdapter(Context context,List<User> usersList, User u)
    {
        this.usersList=usersList;
        this.current = u;
        this.context=context;
    }
    @Override
    public ViewHolderOther onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member_add_user,parent,false);

        return new ViewHolderOther(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderOther holder, int position)
    {
        final String username=usersList.get(position).username;
        final String email=usersList.get(position).email;
        holder.username_view.setText(username + ", "+email);
        //final String user_id=usersList.get(position).userId;

        holder.addFriendButton.setOnClickListener
                (new View.OnClickListener()
        {
            @Override
            public  void onClick(final View view)
            {
                User other = usersList.get(position);
                Friendship f = new Friendship();
                f.fromUser = current;
                f.toUser = other;
                f.accepted = false;

                friendshipData.getInstance().AddFriendship(f);

                Toast.makeText(view.getContext(),"Request sent",Toast.LENGTH_LONG);
                usersList.remove(position);
                //view.setActivated(false);
                removeItem(holder.getAdapterPosition());
                //friendshipData.getInstance().AddFriendship();

            }
        });
    }
    private void removeItem(int position) {
        //usersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, usersList.size());
    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }


    public class ViewHolderOther extends RecyclerView.ViewHolder
    {
        private View vie;
        private TextView username_view;
        private Button addFriendButton;

        public ViewHolderOther(@NonNull View itemView) {
            super(itemView);
            vie=itemView;
            username_view=(TextView)vie.findViewById(R.id.textViewAddUser);
            addFriendButton=vie.findViewById(R.id.addFriendButton);

        }
    }
}


