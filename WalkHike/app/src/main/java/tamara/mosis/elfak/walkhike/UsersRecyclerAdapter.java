package tamara.mosis.elfak.walkhike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Collator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import tamara.mosis.elfak.walkhike.Activities.FriendProfileActivity;
import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;


public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>
{
    //private List<Users> usersList;
    private List<User> usersList;
    private List<User> fullUsersList;

    private Context context;
    private String currentemail;
    FriendshipData friendshipData;
    ItemFilter itemFilter;

    public UsersRecyclerAdapter(Context context,List<User> usersList,String mail)
    {
        this.usersList=usersList;
        this.fullUsersList=usersList;
        this.context=context;
        this.currentemail=mail;
        itemFilter=new ItemFilter();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member_add_user,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String username=usersList.get(position).username;
        final String email=usersList.get(position).email;
        holder.username_view.setText(username +", " + email);

        String imgUrl = usersList.get(position).image;
        RequestOptions placeholderOpt = new RequestOptions();
        placeholderOpt.placeholder(R.drawable.girl_1);
        Glide.with(context).setDefaultRequestOptions(placeholderOpt).load(imgUrl).into(holder.image_view);

        holder.removeFriendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View view)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete this person from your friend list?")
                .setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        User other = usersList.get(position);

                        friendshipData.getInstance().deleteFriendship(currentemail,other.email);

                        usersList.remove(position);
                        removeItem(holder.getAdapterPosition());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

        holder.vie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View view)
            {
                Intent intent=new Intent(context, FriendProfileActivity.class);
                intent.putExtra("username",usersList.get(position).username);//proslediti odgovarajuceg prijatelja
                context.startActivity(intent);
            }
        });
    }

    private void removeItem(int adapterPosition) {
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, usersList.size());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


    public void filterUsersList(CharSequence s)
    {
        if(s.equals(""))
        {
            usersList=fullUsersList;
            notifyDataSetChanged();
        }
        else
            itemFilter.publishResults(s,itemFilter.performFiltering(s));
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View vie;
        private ImageView image_view;
        private TextView username_view;
        private Button removeFriendButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vie=itemView;
            image_view=vie.findViewById(R.id.imageViewFriendPhoto);
            username_view=(TextView)vie.findViewById(R.id.textViewAddUser);
            removeFriendButton=(Button)vie.findViewById(R.id.addFriendButton);
            removeFriendButton.setText("remove");
        }
    }

    private class ItemFilter extends Filter {

        public ItemFilter()
        {}
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            int count = usersList.size();
            final List<User> list = usersList;
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).username;
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filteredData = (List<String>) results.values;
            List<User> temp = new ArrayList<User>(results.count);
            for (String username:filteredData
                 ) {
                for (User u:usersList
                ) {
                    if(username.equals(u.username))
                        temp.add(u);
                }

            }
            usersList=temp;
            notifyDataSetChanged();
        }

    }
}



