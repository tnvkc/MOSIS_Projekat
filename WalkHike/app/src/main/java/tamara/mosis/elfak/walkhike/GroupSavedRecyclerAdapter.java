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

public class GroupSavedRecyclerAdapter extends RecyclerView.Adapter<GroupSavedRecyclerAdapter.ViewHolder>{

    List<String> groupslist;
    Context context;
    String objectTag;
    // ItemFilter itemFilter;



    public GroupSavedRecyclerAdapter(Context context, List<String> groupslist) {
        this.groupslist=groupslist;
        this.context = context;
        //itemFilter=new ItemFilter();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_saved, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final GroupSavedRecyclerAdapter.ViewHolder holder, int position) {


        String imeGrupe = groupslist.get(position);
        holder.groupname_view.setText(groupslist.get(position));

        holder.chooseButton.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        /*SharedPreferences sharedPref = context.getSharedPreferences(
                                context.getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();

                        int num = sharedPref.getInt(context.getString(R.string.NumberOfSavedGroup) + imeGrupe, 0);

                        String id = objectTag;
                        editor.putString(context.getString(R.string.SavedRoute) + imeGrupe + num, id);
                        num++;
                        editor.putInt(context.getString(R.string.NumberOfSavedGroup) + imeGrupe, num);

                        editor.commit();*/
                        Toast.makeText(context, "Group " + groupslist.get(position), Toast.LENGTH_SHORT).show();
                        // removeItem(holder.getAdapterPosition());
                        //  groupslist.remove(position);
                    }
                });
        holder.vie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View view)
            {
                Toast.makeText(context, "treba da vodi na drugi fragment za objekte rute", Toast.LENGTH_SHORT).show();
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
            groupname_view = (TextView) vie.findViewById(R.id.saved_textview_groupname);
            chooseButton = (TextView) vie.findViewById(R.id.saved_textview_number);


        }
    }


}


