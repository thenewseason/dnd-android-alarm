package com.example.webviewexample;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

//    private static final String url = "http://192.100.1.143:8080/";
//    private static final String url = "http://192.100.1.143:5500/publishing/android_alarm_test.html";
//    private static final String url = "http://192.168.219.109:8080/";
    private static final String url = "http://192.168.219.109:5500/publishing/android_alarm_test.html";
    private static final String USER_AGENT_HINT = " gongmorer_alarm";
    private static final String PREF_KEY_STOP_TIME = "pref-key-stop-time";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.addJavascriptInterface(new WebViewInterface(this, webView), "Native");

        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent + USER_AGENT_HINT);

        webView.loadUrl(url);
    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "onStop()");

        super.onStop();

        long currentMillis = Calendar.getInstance().getTimeInMillis();
        SharedPreferencesWrapper.setLong(this, PREF_KEY_STOP_TIME, currentMillis);
    }

    @Override
    protected void onRestart() {
        Log.d("MainActivity", "onRestart()");

        super.onRestart();

        long stoppedMillis = SharedPreferencesWrapper.getLong(this, PREF_KEY_STOP_TIME);
        long currentMillis = Calendar.getInstance().getTimeInMillis();

        if (currentMillis - stoppedMillis > 3000 * 1000) { // 50분
            Log.d("MainActivity", "> 50 min");
            webView.reload();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}