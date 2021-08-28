package com.example.webviewexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class AlarmWrapper {

    public static final String LOG_TAG = "[GONG][AlarmWrapper]";

    Context context;

    public AlarmWrapper(Context context) {
        this.context = context;
    }

    public void set(String requestCodeStr, String epochMillisecondsStr, String title, String text, String largeIconType) {
        Log.d(LOG_TAG, "set");

        int requestCode = Integer.parseInt(requestCodeStr);
        long triggerAtMillis = Long.parseLong(epochMillisecondsStr);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("title", title);
        alarmIntent.putExtra("text", text);
        alarmIntent.putExtra("largeIconType", largeIconType);

        Log.d(LOG_TAG, title);
        Log.d(LOG_TAG, text);
        Log.d(LOG_TAG, largeIconType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }

        enableReceiver();
    }

    public void cancel(String requestCodeStr) {
        int requestCode = Integer.parseInt(requestCodeStr);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, alarmIntent,
                        PendingIntent.FLAG_NO_CREATE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public boolean isExist(String requestCodeStr) {
        int requestCode = Integer.parseInt(requestCodeStr);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, alarmIntent,
                        PendingIntent.FLAG_NO_CREATE);

        return pendingIntent != null;
    }

    private void enableReceiver() {
        PackageManager pm = context.getPackageManager();
        ComponentName deviceBootReceiver = new ComponentName(context, DeviceBootReceiver.class);
        ComponentName alarmReceiver = new ComponentName(context, AlarmReceiver.class);

        pm.setComponentEnabledSetting(deviceBootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(alarmReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

}
