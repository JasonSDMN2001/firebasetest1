package com.unipi.boidanis.firebasetest1;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

        }
   }

    private void sendNotification(String messageTitle, String messageBody){
       Intent intent = new Intent(this, MainActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

       String channelId = "fcm_default_channel";
       //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       NotificationCompat.Builder notificationBuilder =
       new NotificationCompat.Builder(this, channelId)
               .setSmallIcon(R.mipmap.ic_launcher)
               .setContentTitle(messageTitle)
               .setAutoCancel(true)
               .setStyle(new NotificationCompat.BigTextStyle()
                       .bigText(messageBody));
       //.setSound(defaultSoundUri)

       NotificationManager notificationManager =
               (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

       // Since android Oreo notification channel is needed.
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           NotificationChannel channel = new NotificationChannel(channelId,
                   "Channel human readable title",
                   NotificationManager.IMPORTANCE_DEFAULT);
           notificationManager.createNotificationChannel(channel);
       }

       notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
   }

}
