package tamara.mosis.elfak.walkhike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

                        // removeItem(holder.getAdapterPosition());
                        //  groupslist.remove(position);
                    }
                });
        holder.vie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View view)
            {
              //  Toast.makeText(context, "treba da vodi na drugi fragment za objekte rute", Toast.LENGTH_SHORT).show();
                doWork(groupslist.get(position));

            }
        });
        holder.vie.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuInfo;
                String group = imeGrupe;
                menu.setHeaderTitle(imeGrupe);
                menu.add(0,1,1,"Delete group").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        SharedPreferences sharedPref = context.getSharedPreferences(
                                context.getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);

                        int numm= sharedPref.getInt( context.getString(R.string.NumberOfSavedTotal), 0);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        int pomeraj = -1;
                        for(int j = 0; j < numm; j++)
                        {
                            String s = sharedPref.getString(context.getString(R.string.SavedRoutesGroup) + j, "EMPTY");
                            if(s.compareTo(imeGrupe) == 0)
                            {
                                pomeraj = j;
                            }
                            if(pomeraj != -1)
                            {
                                s = sharedPref.getString(context.getString(R.string.SavedRoutesGroup) + (j+1), "EMPTY");
                                editor.putString(context.getString(R.string.SavedRoutesGroup) + j, s);
                            }

                        }
                        editor.remove(context.getString(R.string.SavedRoutesGroup) + (numm - 1));
                        numm --;
                        editor.putInt( context.getString(R.string.NumberOfSavedTotal), numm);


                        int num = sharedPref.getInt( context.getString(R.string.NumberOfSavedGroup), 0);
                        for(int i = 0; i<num; i++)
                        {
                            editor.remove( context.getString(R.string.SavedRoute) + imeGrupe + i);

                        }

                        editor.putInt( context.getString(R.string.NumberOfSavedGroup)+ imeGrupe, 0);


                        editor.commit();
                        Toast.makeText(context, "Group " + groupslist.get(position) + " deleted", Toast.LENGTH_SHORT).show();
                        removeItem(position);
                        return true;
                    }
                });
                menu.add(0,2,2,"Clear all items").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                context.getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);
                        int num = sharedPref.getInt( context.getString(R.string.NumberOfSavedGroup), 0);

                        SharedPreferences.Editor editor = sharedPref.edit();



                        for(int i = 0; i<num; i++)
                        {
                            editor.remove( context.getString(R.string.SavedRoute) + imeGrupe + i);

                        }

                        editor.putInt( context.getString(R.string.NumberOfSavedGroup)+ imeGrupe, 0);


                        editor.commit();
                        Toast.makeText(context, "Group " + groupslist.get(position) + " cleared", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        });



    }

    @Override
    public int getItemCount() {
        //return usersList.size();
        return groupslist.size();
    }

    private void removeItem(int position) {
        groupslist.remove(position);
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

    private static ListenerOnGroupClick  myListener;

    public static void setUpListenerOnGroupClick(ListenerOnGroupClick Listener) {
        myListener = Listener;
    }

    public void doWork(String grouup) { //View view
        if(myListener!= null)
            myListener.onGroupClick(grouup);
    }

    public interface ListenerOnGroupClick{
        void onGroupClick(String groupp);
    }




}


