package tamara.mosis.elfak.walkhike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
    String objectTag;
   // ItemFilter itemFilter;



    public GroupsRecyclerAdapter(Context context, List<String> groupslist, String objectTag) {
        this.groupslist=groupslist;
        this.context = context;
        this.objectTag = objectTag;
        //itemFilter=new ItemFilter();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final GroupsRecyclerAdapter.ViewHolder holder, int position) {


        String imeGrupe = groupslist.get(position);
        holder.groupname_view.setText(groupslist.get(position));

        holder.chooseButton.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                context.getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();

                        int num = sharedPref.getInt(context.getString(R.string.NumberOfSavedGroup) + imeGrupe, 0);

                        String id = objectTag;
                        editor.putString(context.getString(R.string.SavedRoute) + imeGrupe + num, id);
                        num++;
                        editor.putInt(context.getString(R.string.NumberOfSavedGroup) + imeGrupe, num);

                        editor.commit();
                        Toast.makeText(context, "Object added to group " + groupslist.get(position), Toast.LENGTH_SHORT).show();
                       // removeItem(holder.getAdapterPosition());
                      //  groupslist.remove(position);
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
        private TextView chooseButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vie = itemView;
            groupname_view = (TextView) vie.findViewById(R.id.textview_groupname);
            chooseButton = (TextView) vie.findViewById(R.id.add_group_btn);


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


