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

import tamara.mosis.elfak.walkhike.Activities.MainActivity;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;

public class MapObjectRecyclerAdapter extends RecyclerView.Adapter<tamara.mosis.elfak.walkhike.MapObjectRecyclerAdapter.ViewHolder>{

        List<MapObject> objectList;
        Context context;
        String groupName;
        // ItemFilter itemFilter;
        MapObjectData mapObjectData;



        public MapObjectRecyclerAdapter(Context context, List<MapObject> objectList) {
            this.objectList=objectList;
            this.context = context;
            //itemFilter=new ItemFilter();

        }


        @Override
        public tamara.mosis.elfak.walkhike.MapObjectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_view, parent, false);
            return new tamara.mosis.elfak.walkhike.MapObjectRecyclerAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull final tamara.mosis.elfak.walkhike.MapObjectRecyclerAdapter.ViewHolder holder, int position) {


            MapObject m =objectList.get(position);
            String imeGrupe = m.desc;
            holder.groupname_view.setText(imeGrupe);

            holder.chooseButton.setOnClickListener
                    (new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {

                            Intent intent =new Intent(context, MainActivity.class);
                            intent.putExtra("objekaat", m);
                            context.startActivity(intent);
                        }
                    });
            holder.vie.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public  void onClick(View view)
                {
                    Toast.makeText(context, "Ovo treba da vodi na objekat na mapi " + m.desc + " by " +m.createdBy, Toast.LENGTH_SHORT).show();

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
                chooseButton.setText("GO!");

            }
        }




}
