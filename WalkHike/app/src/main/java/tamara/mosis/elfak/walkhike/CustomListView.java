
package tamara.mosis.elfak.walkhike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

public class CustomListView extends ArrayAdapter<String> {

    private Activity context;
    private String[] names;
    private Integer[] images;
    private boolean switchOrNot;


    public CustomListView(Activity context, String[] itemName, Integer[] imageid,boolean switchornot)
    {
        super(context, R.layout.list_member,itemName);
        this.context=context;
        this.names=itemName;
        this.images=imageid;
        this.switchOrNot=switchornot;
    }
    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView,@NonNull ViewGroup parent)
    {
        CustomSwitchListener customSwitchListener=new CustomSwitchListener();
        View vw=convertView;
        ViewHolder vh=null;
        if(vw==null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();

            if (switchOrNot && pos!=4)
            {
                vw = layoutInflater.inflate(R.layout.list_member_switch, null, true);
                vh = new ViewHolder(vw, true);

            } else {
                vw = layoutInflater.inflate(R.layout.list_member, null, true);
                vh=new ViewHolder(vw,false);

            }

            vw.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)vw.getTag();
        }
        if(images!=null)
            vh.iw.setImageResource(images[pos]);
        vh.tw.setText(names[pos]);

        if(vh.aSwitch!=null) {
            vh.aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customSwitchListener.onSwitchClickListner(pos,(Switch)v);
                }
            });
        }
        return vw;
    }


    class ViewHolder
    {
        TextView tw;
        ImageView iw;
        Switch aSwitch=null;

        ViewHolder(View v,boolean switchornot)
        {
            if(switchornot) {
                tw = (TextView) v.findViewById(R.id.textViewPhotoInput);
                iw = (ImageView) v.findViewById(R.id.imageViewPhotoInput);
                aSwitch=(Switch)v.findViewById(R.id.switch_settings);
            }

            else//list_member
            {
                tw = (TextView) v.findViewById(R.id.textViewUsername);
                iw = (ImageView) v.findViewById(R.id.imageViewPhoto);
            }

        }

    }

    public class CustomSwitchListener {
        public CustomSwitchListener(){};
        public void onSwitchClickListner(int position, Switch v)
        {
            switch (position) {
                case (0):
                {
                    if (v.isChecked()) {

                    } else {

                    }
                    break;
                }
                case (1):
                {
                    if (v.isChecked()) {

                    } else {

                    }
                    break;
                }
                case (2):
                {
                    if (v.isChecked()) {

                    } else {

                    }
                    break;
                }
                case (3):
                {
                    if (v.isChecked())
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    else
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    break;
                }
                default:
                {}
            }
        };
    }
}
