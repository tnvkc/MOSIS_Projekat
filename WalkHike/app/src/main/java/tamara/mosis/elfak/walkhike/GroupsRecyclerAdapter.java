package tamara.mosis.elfak.walkhike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import tamara.mosis.elfak.walkhike.modeldata.Friendship;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;

public class GroupsRecyclerAdapter extends RecyclerView.Adapter<GroupsRecyclerAdapter.ViewHolder>{

    List<String> groupslist;
    Context context;
   // ItemFilter itemFilter;



    public GroupsRecyclerAdapter(Context context, List<String> groupslist) {
        this.groupslist=groupslist;
        this.context = context;

        //itemFilter=new ItemFilter();

    }


    @Override
    public GroupsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member_add_user, parent, false);
        return new GroupsRecyclerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final GroupsRecyclerAdapter.ViewHolder holder, int position) {

        holder.groupname_view.setText(groupslist.get(position));

        holder.chooseButton.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Toast.makeText(context, "Object added to group " + groupslist.get(position), Toast.LENGTH_SHORT).show();
                        removeItem(holder.getAdapterPosition());
                        groupslist.remove(position);
                    }
                });

    }

    @Override
    public int getItemCount() {
        //return usersList.size();
        return groupslist.size();
    }

    private void removeItem(int position) {
        //usersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, groupslist.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        private View vie;
        private TextView groupname_view;
        private Button chooseButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vie = itemView;
            groupname_view = (TextView) vie.findViewById(R.id.textViewAddUser);
            chooseButton = (Button) vie.findViewById(R.id.addFriendButton);


        }
    }
    /*private class ItemFilter extends Filter {

        public ItemFilter()
        {}
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            int count = groupslist.size();
            final List<> list = groupslist;
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

    }*/

}


