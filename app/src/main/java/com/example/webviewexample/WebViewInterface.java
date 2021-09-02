package com.example.webviewexample;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewInterface {

    private Context context;
    private WebView webView;
    private NotificationWrapper notificationWrapper;
    private AlarmWrapper alarmWrapper;

    public WebViewInterface(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
        this.notificationWrapper = new NotificationWrapper(context);
        this.alarmWrapper = new AlarmWrapper(context);
    }

    @JavascriptInterface
    public String showToastAndReturnString(String value) {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
        return "my string~!";
    }

    @JavascriptInterface
    public void createNotification(String title, String text, String largeIconType) {
        notificationWrapper.notify(title, text, largeIconType);
    }

    @JavascriptInterface
    public void setAlarm(String requestCodeStr, String triggerAtMillisStr, String title, String text, String largeIconType) {
        alarmWrapper.set(requestCodeStr, triggerAtMillisStr, title, text, largeIconType);
    }

    @JavascriptInterface
    public void cancelAlarm(String requestCodeStr) {
        alarmWrapper.cancel(requestCodeStr);
    }

    @JavascriptInterface
    public boolean isAlarmExist(String requestCodeStr) {
        return alarmWrapper.isExist(requestCodeStr);
    }

    @JavascriptInterface
    public void saveAllAlarmData(String alarmDataJsonStr) {
        SharedPreferencesWrapper.saveAllAlarmData(context, alarmDataJsonStr);
    }

    @JavascriptInterface
    public String getAllAlarmData() {
        return SharedPreferencesWrapper.getAllAlarmData(context);
    }

    @JavascriptInterface
    public void disableReceiver() {
        alarmWrapper.disableReceiver();
    }

}
