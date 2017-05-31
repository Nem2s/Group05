package it.polito.group05.group05.Utility.NotificationClass;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;


public class MyService extends IntentService {
    String b, eid, gid, uid, debit, userId;

    public MyService() {
        super("Myservice");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        b = intent.getStringExtra("action");
        eid = intent.getStringExtra("expenseId");
        uid = intent.getStringExtra("requestFromId");
        gid = intent.getStringExtra("groupId");
        debit = intent.getStringExtra("expenseDebit");
        userId = intent.getStringExtra("userId");
        int notification = intent.getIntExtra("notification", 0);
        if (b.equals("true"))
            DB_Manager.getInstance().payDone(gid, eid, uid, Double.parseDouble(debit), userId);
        else
            DB_Manager.getInstance().payUnDone(gid, eid, uid);

        NotificationManager nm = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        nm.cancel(gid, notification);

    }
}