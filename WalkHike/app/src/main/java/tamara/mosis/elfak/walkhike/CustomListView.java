
package tamara.mosis.elfak.walkhike;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import tamara.mosis.elfak.walkhike.Activities.NewProfilePictureActivity;

public class CustomListView extends ArrayAdapter<String> implements  ActivityCompat.OnRequestPermissionsResultCallback{

    private Activity context;
    private String[] names;
    private Integer[] images;
    private boolean switchOrNot;
    private MediaPlayer mp;
    private boolean permission;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    static final int REQUEST_MODIFY_AUDIO = 5;

    public CustomListView(Activity context, String[] itemName, Integer[] imageid,boolean switchornot)
    {
        super(context, R.layout.list_member,itemName);
        this.context=context;
        this.names=itemName;
        this.images=imageid;
        this.switchOrNot=switchornot;
        sharedPreferences= context.getSharedPreferences(
                "Userdata", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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

                switch (pos)
                {
                    case(0):
                    {
                        if (sharedPreferences.getBoolean("userSound", true)) {
                            vh.aSwitch.setChecked(true);
                        }
                        else
                            vh.aSwitch.setChecked(false);
                        break;
                    }
                    case(1):
                    {
                        if (sharedPreferences.getBoolean("userNotifications", true)) {
                            vh.aSwitch.setChecked(true);
                        }
                        else
                            vh.aSwitch.setChecked(false);
                        break;
                    }
                    case(2):
                    {
                        if (sharedPreferences.getBoolean("userLocationSharing", true)) {
                            vh.aSwitch.setChecked(true);
                        }
                        else
                            vh.aSwitch.setChecked(false);
                        break;
                    }
                    default: {//3, 4 po uslovu nije
                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                            vh.aSwitch.setChecked(true);
                        else
                            vh.aSwitch.setChecked(false);
                        break;

                    }
                }
            }
            else {
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
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onClick(View v) {
                    customSwitchListener.onSwitchClickListner(pos,(Switch)v);
                }
            });
        }
        return vw;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MODIFY_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permission=true;
                } else {
                    permission=false;
                }
                return;
            }

        }
    }


    private void checkModifyAudioPermission()
    {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.MODIFY_AUDIO_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS)) {

                new AlertDialog.Builder(context)
                        .setTitle("Permission Required")
                        .setMessage("Storage permission is required to save image")
                        .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ActivityCompat.requestPermissions(context,
                                        new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},
                                        REQUEST_MODIFY_AUDIO);
                            }
                        }).setNegativeButton("DENIED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},
                        REQUEST_MODIFY_AUDIO);

            }
        } else {

//            Toast.makeText(context, "permission already grated", Toast.LENGTH_SHORT).show();

            permission=true;

        }
    }

    class ViewHolder {
        TextView tw;
        ImageView iw;
        Switch aSwitch = null;

        ViewHolder(View v, boolean switchornot) {
            if (switchornot) {
                tw = (TextView) v.findViewById(R.id.textViewPhotoInput);
                iw = (ImageView) v.findViewById(R.id.imageViewPhotoInput);
                aSwitch = (Switch) v.findViewById(R.id.switch_settings);
            } else//list_member
            {
                tw = (TextView) v.findViewById(R.id.textViewUsername);
                iw = (ImageView) v.findViewById(R.id.imageViewPhoto);
            }

        }

    }

    public class CustomSwitchListener
    {
        public CustomSwitchListener() {};

        @RequiresApi(api = Build.VERSION_CODES.P)
        public void onSwitchClickListner(int position, Switch v) {

            switch (position) {
                case (0):
                {
                    checkModifyAudioPermission();
                    while(permission==false){}
                    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    if (v.isChecked())
                    {
                        editor.putBoolean("userSound", true);

                        editor.commit();
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                    }
                    else
                    {
                        editor.putBoolean("userSound", false);

                        editor.commit();
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMinVolume(AudioManager.STREAM_MUSIC), 0);
                    }
                    mp=new MediaPlayer();
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    break;
                }
                case (1): {
                    if (v.isChecked()) {
                        //Toast.makeText(context, "Start service", Toast.LENGTH_SHORT).show();
//                        editor.putBoolean("userNotifications", true);

                        editor.commit();

                        Intent i = new Intent(context, NotificationService.class);
                        i.putExtra("timer", 10);
                        context.startService(i);
                    } else {
                        //Toast.makeText(context, "Stop service", Toast.LENGTH_SHORT).show();
                        v.setActivated(false);
                        editor.putBoolean("userNotifications", false);

                        editor.commit();

                        Intent i = new Intent(context, NotificationService.class);

                        context.stopService(i);
                    }

                    break;
                }
                case (2): {
                    if (v.isChecked()) {
                        editor.putBoolean("userLocationSharing", true);

                        editor.commit();


//                            Toast.makeText(context, "Start service", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(context, NotificationService.class);
                                i.putExtra("timer",10);
                                context.startService(i);
                            }

                            //startedService = true;
                        }, 200);


                    } else {
                        editor.putBoolean("userLocationSharing", false);

                        editor.commit();


                        //Toast.makeText(context, "Stop service", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                                                      @Override
                                                      public void run() {

                                                          v.setActivated(false);

                                                          Intent i = new Intent(context, NotificationService.class);

                                                          context.stopService(i);
                                                      }
                                                  }, 200);
                       // startedService = false;

                    }
                    break;
                }
                case (3): {
                    if (v.isChecked())//pri inflate dodata provera da li je 3. switch dark mode i cekiranje, posto se pri promeni moda resetuje
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        };
    }


}



