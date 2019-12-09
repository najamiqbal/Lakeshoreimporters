package com.dleague.lakeshoreimporters.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.dleague.lakeshoreimporters.R;

import java.util.Calendar;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class NotificationScheduler {
    public static Random ran2 = new Random();
    public static final int endclear = ran2.nextInt(100 - 97) + 97;
    public static final int DAILY_REMINDER_REQUEST_CODE=100;
    public static final String TAG="NotificationScheduler";
    public static final String channelId = "channel-01";
    public static final int importance = NotificationManager.IMPORTANCE_HIGH;
    public static void setReminder(Context context, Class<?> cls, int hour, int min)
    {
        Calendar calendar = Calendar.getInstance();

        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);

        // cancel already scheduled reminders
        //cancelReminder(context,cls);

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, endclear, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

  /*  public static void cancelReminder(Context context,Class<?> cls)
    {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }*/

    public static void showNotification(Context context,Class<?> cls,String title,String content)
    {
        String channelName = "Channel Name";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("NOTIFICATION","IAM HERE BOY");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

/*
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);
*/

        // PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, DAILY_REMINDER_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId);

        Notification notification = builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        // NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);

    }

}