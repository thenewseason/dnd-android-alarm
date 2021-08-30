package com.example.webviewexample;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {

    public static final String PREFERENCES_NAME = "GONGMORER";
    public static final String KEY_ALL_ALARM = "all-alarm";
    private static final String DEFAULT_VALUE_STRING = "";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void saveAllAlarmData(Context context, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ALL_ALARM, value);
        editor.commit();
    }

    public static String getAllAlarmData(Context context) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(KEY_ALL_ALARM, DEFAULT_VALUE_STRING);
        return value;
    }
}
