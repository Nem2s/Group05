package it.polito.group05.group05.Utility.Notification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import it.polito.group05.group05.R;

/**
 * Created by user on 17/05/2017.
 */

public class messageNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);

        nb.setContentText("New group");
        nb.setContentTitle("New Group");
        nb.setSmallIcon(R.drawable.ic_action_help);
        nm.notify(0,nb.build());

    }
}
