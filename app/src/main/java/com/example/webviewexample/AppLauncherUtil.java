package com.example.webviewexample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class AppLauncherUtil {

    private static final String LOG_TAG = "AppLauncherUtil";

    Context context;

    public AppLauncherUtil(Context context) {
        this.context = context;
    }

    public void launchApp(String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packageName);

            if (intent != null) {
                context.startActivity(intent);
            } else {
                launchMarket(packageName);
            }
        } catch(Exception e) {
            launchMarket(packageName);
        }
    }

    private void launchMarket(String packageName) {
        launchUri("market://details?id=" + packageName);
    }

    public void launchUri(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

}
