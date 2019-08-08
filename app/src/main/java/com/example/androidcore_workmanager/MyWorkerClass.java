package com.example.androidcore_workmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorkerClass extends Worker {

    private static final String CHANNEL_ID = "My Channel ID";
    public static final String RECEIVER_KEY = "RECEIVER KEY";
    NotificationManager nManager;
    private int my_ID = 21;

    public MyWorkerClass(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String input_data = data.getString(MainActivity.SENDER_KEY);
        displayNotification(input_data);

        Data data1 = new Data.Builder()
                .putString(RECEIVER_KEY, "Data Received Successfully")
                .build();

        return Result.success(data1);
    }

    public void displayNotification(String input){
        nManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), my_ID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                CHANNEL_ID).setAutoCancel(true)
                            .setContentTitle("Onclick Notification")
                            .setContentText(input)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentIntent(pendingIntent);

        nManager.notify(my_ID, builder.build());
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "My Channel 1", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Notification channel");
        channel.setLightColor(Color.RED);
        channel.setName("Use to provide onclick notification");

        nManager.createNotificationChannel(channel);
    }
}
