package tamara.mosis.elfak.walkhike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        switchOrNot=switchornot;
    }
    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView,@NonNull ViewGroup parent)
    {
        View vw=convertView;
        ViewHolder vh=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
           /* if(switchOrNot)
              vw = layoutInflater.inflate(R.layout.list_member_switch, null, true);
            else*/
                vw = layoutInflater.inflate(R.layout.list_member, null, true);

            vh=new ViewHolder(vw);
            vw.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)vw.getTag();
        }
        vh.iw.setImageResource(images[pos]);
        vh.tw.setText(names[pos]);

        return vw;
    }
    class ViewHolder
    {
        TextView tw;
        ImageView iw;

        ViewHolder(View v)
        {
            tw=(TextView) v.findViewById(R.id.textViewPhotoInput);
            iw=(ImageView) v.findViewById(R.id.imageViewPhotoInput);

        }
    }
}
