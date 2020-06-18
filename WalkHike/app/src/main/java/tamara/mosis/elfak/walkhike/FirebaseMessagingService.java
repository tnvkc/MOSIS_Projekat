package tamara.mosis.elfak.walkhike;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService
{
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle=remoteMessage.getNotification().getTitle();
        String messageBody=remoteMessage.getNotification().getBody();

        String click_action=remoteMessage.getNotification().getClickAction();
        String message =remoteMessage.getData().get("message");
        String dataFrom =remoteMessage.getData().get("from_user_id");

        if (Build.VERSION.SDK_INT >= 26) {//da li je context this?

            NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "Channel name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            notificationManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent resIntent=new Intent(click_action);
        resIntent.putExtra("message",message);
        resIntent.putExtra("from_user_id",dataFrom);

        PendingIntent resPendingIntent
                =PendingIntent.getActivity(this,0,resIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resPendingIntent);

        int notificationId=(int)System.currentTimeMillis();
        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, builder.build());



    }
}
