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
    private String type;

    public CustomListView(Activity context, String[] itemName, Integer[] imageid,String type)
    {
        super(context, R.layout.list_member,itemName);
        this.context=context;
        this.names=itemName;
        this.images=imageid;
        this.type=type;
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

            switch(type) {
                case "addUser":
                    vw = layoutInflater.inflate(R.layout.list_member_add_user, null, true);
                    break;
                case "acceptDecline":
                    vw = layoutInflater.inflate(R.layout.list_member_accept_decline, null, true);
                    break;
                case "settings":
                    vw = layoutInflater.inflate(R.layout.list_member_switch, null, true);
                    break;
                default:
                    vw = layoutInflater.inflate(R.layout.list_member, null, true);
            }

            vh=new ViewHolder(vw);
            vw.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)vw.getTag();
        }
        if(images!=null)
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
