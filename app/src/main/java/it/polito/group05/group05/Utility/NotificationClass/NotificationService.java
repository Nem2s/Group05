package it.polito.group05.group05.Utility.NotificationClass;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import it.polito.group05.group05.R;

/**
 * Created by antonino on 16/05/2017.
 */

public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
            nb.setContentTitle(remoteMessage.getNotification().getTitle()).setContentText(remoteMessage.getNotification().getBody()).setSmallIcon(R.drawable.com_facebook_button_like_icon_selected);
            nm.notify(0, nb.build());


        }


    }
}
