package com.example.webviewexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DeviceBootReceiver", "DeviceBootReceiver.onReceive");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.

            Log.d("DeviceBootReceiver", "intent.getAction().equals()");
        }
    }
}
