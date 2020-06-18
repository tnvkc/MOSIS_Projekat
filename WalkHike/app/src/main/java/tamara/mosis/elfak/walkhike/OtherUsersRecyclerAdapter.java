package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.content.Intent;
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


public class OtherUsersRecyclerAdapter extends RecyclerView.Adapter<OtherUsersRecyclerAdapter.ViewHolderOther>
{
    private List<Users> usersList;
    private Context context;

    private String currentId;//ours
    private FirebaseFirestore mFirestore;

    public OtherUsersRecyclerAdapter(Context context,List<Users> usersList)
    {
        this.usersList=usersList;
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
        final String username=usersList.get(position).getName();
        holder.username_view.setText(username);
        final String user_id=usersList.get(position).userId;

        holder.addFriendButton.setOnClickListener
                (new View.OnClickListener()
        {
            @Override
            public  void onClick(final View view)
            {
                mFirestore= FirebaseFirestore.getInstance();
                currentId= FirebaseAuth.getInstance().getUid();

                Map<String,Object> notificationMessage=new HashMap<>();
                notificationMessage.put("message", "New friend request");
                notificationMessage.put("from", currentId);

                mFirestore.collection("Users/" +user_id+"/Notifications").add(notificationMessage);/*.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(view.getContext(),"message sent",Toast.LENGTH_LONG);
                    }
                });*/
            }
        });
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


