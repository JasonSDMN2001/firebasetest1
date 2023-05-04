package com.unipi.boidanis.firebasetest1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FirebaseCloudMessagingNotificationsService extends Service {
    public FirebaseCloudMessagingNotificationsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}