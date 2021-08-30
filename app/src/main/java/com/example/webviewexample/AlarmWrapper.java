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

    public static final String LOG_TAG = "AlarmWrapper";

    Context context;

    public AlarmWrapper(Context context) {
        this.context = context;
    }

    public void set(String requestCodeStr, String triggerAtMillisStr, String title, String text, String largeIconType) {
        Log.d(LOG_TAG, "set() requestCode: " + requestCodeStr + ", triggerAtMillis: " + triggerAtMillisStr);

        int requestCode = Integer.parseInt(requestCodeStr);
        long triggerAtMillis = Long.parseLong(triggerAtMillisStr);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("title", title);
        alarmIntent.putExtra("text", text);
        alarmIntent.putExtra("largeIconType", largeIconType);

        Log.d(LOG_TAG, title);
        Log.d(LOG_TAG, text);
        Log.d(LOG_TAG, largeIconType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // setExactAndAllowWhileIdle() 설정 후
        // adb 명령어로 doze 모드에 진입시키면 알람이 제시각에 울리지 않음.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // setAlarmClock() 으로 설정시
            // adb -s 192.168.219.110:5902 shell dumpsys deviceidle step
            // -> Stepped to deep: INACTIVE
            // 인접한 알람이 있을 경우 doze 모드 진입이 안되는 것 같다.
            AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(triggerAtMillis, pendingIntent);
            alarmManager.setAlarmClock(ac, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }

        enableReceiver();
    }

    public void cancel(String requestCodeStr) {
        Log.d(LOG_TAG, "cancel() requestCode: " + requestCodeStr);

        int requestCode = Integer.parseInt(requestCodeStr);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, alarmIntent,
                        PendingIntent.FLAG_NO_CREATE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public boolean isExist(String requestCodeStr) {
        Log.d(LOG_TAG, "isExist() requestCode: " + requestCodeStr);

        int requestCode = Integer.parseInt(requestCodeStr);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, alarmIntent,
                        PendingIntent.FLAG_NO_CREATE);

        return pendingIntent != null;
    }

    public void disableReceiver() {
        PackageManager pm = context.getPackageManager();
        ComponentName deviceBootReceiver = new ComponentName(context, DeviceBootReceiver.class);
        ComponentName alarmReceiver = new ComponentName(context, AlarmReceiver.class);

        pm.setComponentEnabledSetting(deviceBootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(alarmReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
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
