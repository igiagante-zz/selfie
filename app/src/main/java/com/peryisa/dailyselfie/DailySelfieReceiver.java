package com.peryisa.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ignaciogiagante on 8/18/15.
 */
public class DailySelfieReceiver extends BroadcastReceiver{

    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mNotificationIntent = new Intent(context, MainActivity.class);

        PendingIntent mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, PendingIntent.FLAG_ONE_SHOT);


        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.camera)
                .setAutoCancel(true)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie")
                .setContentIntent(mContentIntent);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());
    }
}
