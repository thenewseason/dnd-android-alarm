package com.example.webviewexample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String LOG_TAG = "[GONG][AlarmReceiver]";
    public static final String CHANNEL_ID = "10001";
    public static final String CHANNEL_NAME = "Gongmorer";
    public static final String CHANNEL_DESCRIPTION = "Gongmorer Notification Channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive()");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(context);

        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        int largeIcon = toDrawable(intent.getStringExtra("largeIconType"));
        int notificationId = (int) ((System.currentTimeMillis() / 1000L) % Integer.MAX_VALUE);

        Log.d(LOG_TAG, title);
        Log.d(LOG_TAG, text);
        Log.d(LOG_TAG, notificationId + "");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.gong_white2_outline)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(createNotificationPendingIntent(context));

        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private PendingIntent createNotificationPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        return pendingIntent;
    }

    private int toDrawable(String type) {
        if ("offer".equalsIgnoreCase(type)) {
            return R.drawable.gong_offer12;
        }
        return R.drawable.gong_white2_outline;
    }

}
