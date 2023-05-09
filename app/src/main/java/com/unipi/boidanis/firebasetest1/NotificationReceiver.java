package com.unipi.boidanis.firebasetest1;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {



    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity3.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        Toast.makeText(context, "broadcast", Toast.LENGTH_SHORT).show();

        NotificationChannel channel = new NotificationChannel("134", "notificationchannel",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager2 =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager2.createNotificationChannel(channel);
        NotificationCompat.Builder builder2 =
                new NotificationCompat.Builder(context, "134");
        builder2.setContentTitle("How's your baby?")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("smile for Face A Day")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        notificationManager2.notify(4, builder2.build());
    }
}
