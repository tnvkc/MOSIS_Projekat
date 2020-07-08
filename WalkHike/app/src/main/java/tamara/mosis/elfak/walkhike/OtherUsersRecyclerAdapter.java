package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
    private List<User> fullUsersList;
    ItemFilter itemFilter;

    private Context context;

    private String currentId;//ours
    private FirebaseFirestore mFirestore;

    FriendshipData friendshipData;
    UserData userData;
    User current;
    public OtherUsersRecyclerAdapter(Context context,List<User> usersList, User u)
    {
        this.usersList=usersList;
        fullUsersList=usersList;
        this.current = u;
        this.context=context;
        itemFilter=new ItemFilter();
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
        holder.username_view.setText(username );
        //final String user_id=usersList.get(position).userId;

        String imgUrl = usersList.get(position).image;
        RequestOptions placeholderOpt = new RequestOptions();
        placeholderOpt.placeholder(R.drawable.girl_1);
        Glide.with(context).setDefaultRequestOptions(placeholderOpt).load(imgUrl).into(holder.image_view);

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

    public class ViewHolderOther extends RecyclerView.ViewHolder
    {
        private View vie;
        private TextView username_view;
        private Button addFriendButton;
        private ImageView image_view;
        public ViewHolderOther(@NonNull View itemView) {
            super(itemView);
            vie=itemView;
            username_view=(TextView)vie.findViewById(R.id.textViewAddUser);
            addFriendButton=vie.findViewById(R.id.addFriendButton);
            image_view=vie.findViewById(R.id.imageViewFriendPhoto);

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


