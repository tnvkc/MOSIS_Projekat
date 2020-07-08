package tamara.mosis.elfak.walkhike;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tamara.mosis.elfak.walkhike.Activities.LoginActivity;
import tamara.mosis.elfak.walkhike.Activities.MainActivity;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;

public class GroupItemsRecyclerAdapter extends RecyclerView.Adapter<tamara.mosis.elfak.walkhike.GroupItemsRecyclerAdapter.ViewHolder>{

    List<String> objectList;
    Context context;
    String groupName;
    // ItemFilter itemFilter;
    MapObjectData mapObjectData;



        public GroupItemsRecyclerAdapter(Context context, List<String> objectList, String groupName) {
            this.objectList=objectList;
            this.context = context;
            this.groupName = groupName;
            //itemFilter=new ItemFilter();

        }


        @Override
        public tamara.mosis.elfak.walkhike.GroupItemsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_view, parent, false);
            return new tamara.mosis.elfak.walkhike.GroupItemsRecyclerAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull final tamara.mosis.elfak.walkhike.GroupItemsRecyclerAdapter.ViewHolder holder, int position) {


            MapObject m = mapObjectData.getInstance().getObjectWithDatetimeUser(objectList.get(position)) ;
            String imeGrupe = m.desc;
            holder.groupname_view.setText(imeGrupe);

            holder.chooseButton.setOnClickListener
                    (new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {


                            //Toast.makeText(context, "ovde treba da se izbrise", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPref = context.getSharedPreferences(
                                    context.getString(R.string.SavedRoutesShared), Context.MODE_PRIVATE);
                            int numm = sharedPref.getInt(context.getString(R.string.NumberOfSavedGroup) + groupName, -1);
                            int pomeraj = -1;
                            SharedPreferences.Editor editor = sharedPref.edit();


                            if(numm != -1)
                            {
                                for(int j= 0; j< numm; j++) {
                                    String objectId = sharedPref.getString(context.getString(R.string.SavedRoute) + groupName + j, "EMPTY");
                                   if(objectId.compareTo(objectList.get(position)) == 0)
                                   {
                                        pomeraj = j;
                                   }

                                }

                                if(pomeraj!= -1) {
                                    for (int j = pomeraj + 1; j < numm; j++) {
                                        String objectId = sharedPref.getString(context.getString(R.string.SavedRoute) + groupName + j, "EMPTY");
                                        editor.putString(context.getString(R.string.SavedRoute) + groupName + (j - 1), objectId);
                                    }
                                    editor.remove(context.getString(R.string.SavedRoute) + groupName + (numm-1));

                                    numm--;
                                    editor.putInt(context.getString(R.string.NumberOfSavedGroup) + groupName, numm);
                                    editor.commit();

                                }
                            }
                             removeItem(holder.getAdapterPosition());
                              objectList.remove(position);
                        }
                    });
            holder.vie.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public  void onClick(View view)
                {
                    Intent intent =new Intent(context, MainActivity.class);
                    intent.putExtra("objekaat", m);
                    context.startActivity(intent);

                    //Toast.makeText(context, "Ovo treba da vodi na objekat na mapi " + m.desc + " by " +m.createdBy, Toast.LENGTH_SHORT).show();

                }
            });

        }

        @Override
        public int getItemCount() {
            //return usersList.size();
            return objectList.size();
        }

        private void removeItem(int position) {
            //usersList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, objectList.size());
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
                chooseButton.setText("Remove");

            }
        }

    }

