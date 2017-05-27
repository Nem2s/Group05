package it.polito.group05.group05.Utility.NotificationClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context.getApplicationContext(), MyService.class);
        context.startService(i);
    }
}
