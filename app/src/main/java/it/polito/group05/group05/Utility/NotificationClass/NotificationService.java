package it.polito.group05.group05.Utility.NotificationClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import it.polito.group05.group05.R;
import it.polito.group05.group05.SignUpActivity;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

/**
 * Created by antonino on 16/05/2017.
 */

public class NotificationService extends FirebaseMessagingService {


    int counter = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override

    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> map = remoteMessage.getData();

        buildNotification(map);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void buildNotification(Map<String, String> map) {
        String type = map.get("type");
        int notificationId;
        NotificationCompat.Builder nb = new NotificationCompat.Builder(getBaseContext());
        nb.setAutoCancel(true).setSmallIcon(R.drawable.logo)
                .setDefaults(Notification.DEFAULT_ALL)
        // .addAction(R.drawable.ic_mail_white_24dp, "Pay", null)
        //.addAction(R.drawable.ic_mail_white_24dp, "Another Pay", null)
        ;
        //Map<String,String> map = remoteMessage.getData();
        String body = "default";
        String title = "default";
        String ticker = "default";
        notificationId = 0;
        switch (type) {
            case "newGroup":
                body = "You have been added to " + map.get("groupName");
                title = map.get("groupName") + " is created";
                notificationId = 1;
                ticker = title + "\n" + body + "\n";
                break;
            case "newExpense":
                notificationId = 2;
                title = map.get("expense_name") + " is created by " + map.get("expense_owner");
                body = "Debit: " + map.get("expense_debit");
                ticker = title + "\n" + body + "\n";

                break;
            case "newMessage":
                notificationId = 3;
                body = map.get("messageUser") + ": " + map.get("message");
                title = map.get("groupName");
                ticker = "New message" + " @ " + title + "\n" + map.get("messageUser") + ":" + map.get("message");
                break;

        }
        Intent i = new Intent(this, SignUpActivity.class);
        for (String s : map.keySet()) {
            i.putExtra(s, map.get(s));
        }
        nb.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, i, FLAG_ONE_SHOT));
        nb.setContentText(body).setContentTitle(title).setTicker(ticker);
        NotificationManager nm = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        nm.notify(map.get("groupId"), notificationId, nb.build());

    }
}
