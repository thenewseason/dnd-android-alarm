package com.example.webviewexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {

    public static final String LOG_TAG = "DeviceBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive()");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            String allAlarmDataJsonStr = SharedPreferencesWrapper.getAllAlarmData(context);

            try {
                JSONArray alarmDataList = new JSONArray(allAlarmDataJsonStr);
                long currentMillis = Calendar.getInstance().getTimeInMillis();
                AlarmWrapper alarmWrapper = new AlarmWrapper(context);

                for (int i = 0; i < alarmDataList.length(); i++) {
                    JSONObject alarmData = alarmDataList.getJSONObject(i);

                    String triggerAtMillisStr = alarmData.getString("triggerAtMillis");
                    long triggerAtMillis = Long.parseLong(triggerAtMillisStr);
                    String requestCodeStr = alarmData.getString("requestCode");
                    String datetime = alarmData.getString("datetime");
                    String title = alarmData.getString("title");
                    String text = alarmData.getString("text");
                    String largeIconType = alarmData.getString("largeIconType");

                    if (currentMillis < triggerAtMillis) { // 미래 알람만 등록해야 함
                        Log.d(LOG_TAG, "Future time -> set " + "requestCode: " + requestCodeStr + ", datetime: " + datetime);
                        alarmWrapper.set(requestCodeStr, triggerAtMillisStr, title, text, largeIconType);
                    } else {
                        Log.d(LOG_TAG, "Past time -> ignore " + "requestCode: " + requestCodeStr + ", datetime: " + datetime);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
