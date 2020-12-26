package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import io.sentry.Sentry;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Info", "Schedule Trigger " + intent.getStringExtra("id") );

        Sentry.captureMessage(intent.getStringExtra("title"));

        String CHANNEL_ID = "CH1";
        String CHANNEL_NAME = "CHANNEL";
        NotificationManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            manager = MyApplication.getAppContext().getSystemService(NotificationManager.class);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MAX);
            manager.createNotificationChannel(channel);
        }

        // Build notification based on Intent
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getAppContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(DateFormat.format("dd/MM/yyyy hh:mm:ss", System.currentTimeMillis()).toString() + intent.getStringExtra("text"))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(intent.getStringExtra("text")))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);

        // Show notification
        manager.notify(Integer.parseInt(intent.getStringExtra("id")), builder.build());

    }
}
