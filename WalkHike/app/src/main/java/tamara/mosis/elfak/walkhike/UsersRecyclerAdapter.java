package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tamara.mosis.elfak.walkhike.modeldata.User;


public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>
{
    //private List<Users> usersList;
    private List<User> usersList;
    private Context context;

    public UsersRecyclerAdapter(Context context,List<User> usersList)
    {
        this.usersList=usersList;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String username=usersList.get(position).username;
        final String email=usersList.get(position).email;
        holder.username_view.setText(username +", " + email);
        //final String user_id=usersList.get(position).userId;

       /* holder.vie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View view)
            {
                Intent sendIntent=new Intent(context,SendActivity.class);
                sendIntent.putExtra("user_id",user_id);
                sendIntent.putExtra("username",username);

                context.startActivity(sendIntent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View vie;
        private TextView username_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vie=itemView;
            username_view=(TextView)vie.findViewById(R.id.textViewUsername);
        }
    }
}


