package com.example.webviewexample;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewInterface {

    private Context mContext;
    private WebView mWebView;
    private NotificationWrapper notificationWrapper;
    private AlarmWrapper alarmWrapper;

    public WebViewInterface(Context context, WebView webView) {
        mContext = context;
        mWebView = webView;
        notificationWrapper = new NotificationWrapper(context);
        alarmWrapper = new AlarmWrapper(context);
    }

    @JavascriptInterface
    public String showToastAndReturnString(String value) {
        Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
        return "my string~!";
    }

    @JavascriptInterface
    public void createNotification(String title, String description, String largeIconType) {
        notificationWrapper.notify(title, description, largeIconType);
    }

    @JavascriptInterface
    public void createAlarm(String requestCodeStr, String epochMillisecondsStr, String title, String text, String largeIconType) {
        alarmWrapper.set(requestCodeStr, epochMillisecondsStr, title, text, largeIconType);
    }

}
