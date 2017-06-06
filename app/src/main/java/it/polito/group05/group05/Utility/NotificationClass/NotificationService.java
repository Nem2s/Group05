package it.polito.group05.group05.Utility.NotificationClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;
import java.util.Random;

import it.polito.group05.group05.R;
import it.polito.group05.group05.SignUpActivity;
import it.polito.group05.group05.Utility.HelperClasses.NotificationId;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

/**
 * Created by antonino on 16/05/2017.
 */

public class NotificationService extends FirebaseMessagingService {


    int counter = 0;


    @Override

    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> map = remoteMessage.getData();


            buildNotification(map);

    }


    private void buildNotification(Map<String, String> map) {
        String type = map.get("type");
        int notificationId;

        NotificationCompat.Builder nb = new NotificationCompat.Builder(getBaseContext());
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(this)
                .using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReference().child(map.get("icon")))
                .asBitmap()
                    .centerCrop()
                    .into(128, 128)
                .get();
            nb.setLargeIcon(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                notificationId = NotificationId.getID();
                title = map.get("expense_name") + " is created by " + map.get("expense_owner") + " @ " + map.get("groupName");
                body = "You have to pay € " + String.format("%.2f", Double.parseDouble(map.get("expense_debit").substring(1)));
                ticker = title + "\n" + body + "\n";

                break;
            case "newMessage":
                notificationId = 3;
                body = map.get("messageUser") + ": " + map.get("message");
                title = map.get("groupName");
                ticker = "New message" + " @ " + title + "\n" + map.get("messageUser") + ":" + map.get("message");
                break;
            case "paymentRequest":
                notificationId = NotificationId.getID();
                double d = Double.parseDouble(map.get("expenseDebit").substring(1));
                body = "Have you received € " + String.format("%.2f", d) + " for " + map.get("expenseName") + "by " + map.get("requestFrom") + " ?";
                title = map.get("groupName");
                ticker = "paymentRequest" + " from " + map.get("requestFrom") + "\n";
                Intent intent = new Intent(this, MyService.class);
                Intent intent2 = new Intent(this, MyService.class);
                for (String s : map.keySet()) {
                    intent.putExtra(s, map.get(s));
                    intent2.putExtra(s, map.get(s));
                }
                int int1 = NotificationId.getID();
                intent.putExtra("action", "true");
                intent.putExtra("notification", notificationId);

                nb.setPriority(Notification.PRIORITY_MAX);
                nb.addAction(R.drawable.ic_action_tick_white, "Yes", PendingIntent.getService(this, int1, intent, PendingIntent.FLAG_ONE_SHOT));
                int1 = NotificationId.getID();
                intent2.putExtra("notification", notificationId);
                intent2.putExtra("action", "false");
                nb.addAction(R.drawable.ic_action_decline_white, "No", PendingIntent.getService(this, int1, intent2, PendingIntent.FLAG_ONE_SHOT));
                break;
            case "rememberPayment":
                body = "reminds you to pay € " + String.format("%.2f", Double.parseDouble(map.get("expenseDebit").substring(1))) + " for " + map.get("expenseName");
                title = map.get("requestFrom") + "@" + map.get("groupName");

                ticker = title + '\n' + body;

                break;
            case "rememberPaymentToOne":
                body = "reminds you to pay € " + String.format("%.2f", Double.parseDouble(map.get("debit"))) + " to be balanced";
                title = map.get("requestFrom") + "@" + map.get("groupName");
                ticker = title + '\n' + body;
                break;
        }
        Intent i = new Intent(this, SignUpActivity.class);
        for (String s : map.keySet()) {
            i.putExtra(s, map.get(s));
        }
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        nb.setContentIntent(PendingIntent.getActivity(getApplicationContext(), NotificationId.getID(), i, FLAG_ONE_SHOT));
        nb.setContentText(body).setContentTitle(title).setTicker(ticker).setAutoCancel(true);
        NotificationManager nm = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));

        nm.notify(map.get("groupId"), notificationId, nb.build());

    }

}
