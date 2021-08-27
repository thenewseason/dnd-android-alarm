package com.example.webviewexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class WebViewInterface {

    private Context mContext;
    private WebView mWebView;

    // notification
    public static final String CHANNEL_ID = "5902";
    public static final String channel_name = "notification channel name";
    public static final String channel_description = "notification channel description";

    public WebViewInterface(Context context, WebView webView) {
        mContext = context;
        mWebView = webView;
    }

    @JavascriptInterface
    public String showToastAndReturnString(String value) {
        Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
        return "my string~!";
    }

    @JavascriptInterface
    public void createNotification(String title, String desc) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channel_description);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
