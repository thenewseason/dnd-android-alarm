package com.example.webviewexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

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
        PendingIntent pendingIntent = createPendingIntent();

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.gong_white2_outline)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.gong_offer12))
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

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

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        return pendingIntent;
    }
}
