package com.example.subdua.broadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.subdua.MainActivity;
import com.example.subdua.R;


public class NotificationReceiver extends BroadcastReceiver {
    public static final int notif_id = 50;
    public static String channelid = "100";
    public static CharSequence notif_nama = "Notifikasi Manager";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent japan = new Intent(context, MainActivity.class);
        japan.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notif_id, japan, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder korea = new NotificationCompat.Builder(context, channelid)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(context.getResources().getString(R.string.name))
                .setContentText(context.getResources().getString(R.string.deskripsi))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelid, notif_nama, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(notif_nama.toString());
            korea.setChannelId(channelid);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        if (mNotificationManager != null) {
            mNotificationManager.notify(notif_id, korea.build());
        }
    }
}
