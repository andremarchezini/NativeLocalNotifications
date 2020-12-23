package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

        boolean scheduled = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Info", "Schedule clicked");
                String CHANNEL_ID = "CH1";
                String CHANNEL_NAME = "CHANNEL";
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MAX);
                        NotificationManager notificationManager = MyApplication.getAppContext().getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getAppContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentTitle("My notification")
                            .setContentText("This is a notification")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("This is a notification. Much longer text that cannot fit one line..."))
                            .setPriority(NotificationCompat.PRIORITY_MAX);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyApplication.getAppContext());
                    notificationManager.notify(1, builder.build());
                } catch (Error e){
                    Log.i("Error", e.toString());
                }
            }
        });

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                try{
                for(int i=1; i<= 10; i++){
                    long millis = System.currentTimeMillis() + (300000 * i);
                    Intent intent = new Intent(MyApplication.getAppContext(), NotificationReceiver.class);
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    String title = "Scheduled Notification " + DateFormat.format("dd/MM/yyyy hh:mm:ss", millis).toString();
                    String text = "This is a scheduled notification";
                    intent.putExtra("title", title);
                    intent.putExtra("text", text);
                    intent.putExtra("id", String.valueOf(i));
                    PendingIntent pending = PendingIntent.getBroadcast(MyApplication.getAppContext(), i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // Schedule notification
                    AlarmManager manager = (AlarmManager) MyApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pending);
                    }

                    TextView textView = view.findViewById(R.id.textView2);

                    textView.setVisibility(View.VISIBLE);

                }
                } catch (Error e){
                    Log.i("Error", e.toString());
                }
            }
        });

    }
}