package com.example.stepcounter.alarmReceiver;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.stepcounter.R;


public class DailyReminderReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "dailyReminderID";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("worked", "yes");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        mBuilder.setContentTitle("Step counter");
        mBuilder.setContentText("Time to work out !");
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mNotificationManager.notify(123, mBuilder.build());

        Toast.makeText(context, "It's time for your daily reminder!", Toast.LENGTH_SHORT).show();
    }
}
