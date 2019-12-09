package com.dleague.lakeshoreimporters.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dleague.lakeshoreimporters.activities.MyOrdersActivity;

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                //LocalData localData = new LocalData(context);
               // NotificationScheduler.setReminder(context, AlarmReceiver.class, 10, 12);
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.showNotification(context, MyOrdersActivity.class,
                "LAKESHORE IMPORTERS", "Check Updation about your recent order");

    }
}
