package com.example.webviewexample;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String url = "http://192.168.219.104:8080";
//    private static final String url = "https://gongmorer.kr";
//    private static final String url = "http://192.100.1.143:5500/publishing/android_alarm_test.html";
//    private static final String url = "http://192.168.219.104:5500/publishing/android_alarm_test.html";
//    private static final String url = "http://192.168.219.104:5500/publishing/screencapture_home.html";
//    private static final String USER_AGENT_HINT = " ";
    private static final String USER_AGENT_HINT = " gongmorer_alarm gongmorer_launch";
    private static final String PREF_KEY_STOP_TIME = "pref-key-stop-time";
    private WebView webView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
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

        if (currentMillis - stoppedMillis > 3000 * 1000) { // 50???
            Log.d("MainActivity", "> 50 min");
            webView.reload();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("MainActivity", "onKeyDown()");
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            if (isModal(webView.getUrl())) {
                webView.goBack();
                return true;
            }

            if (isCustomNetworkErrorPage(webView.getUrl())) {
                return super.onKeyDown(keyCode, event);
            }

            if (!isHome(webView.getUrl())) {
                webView.loadUrl(url);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("MainActivity", "shouldOverrideUrlLoading()");
            Log.d("MainActivity", url);

            if (URLUtil.isNetworkUrl(url)) {
                webView.loadUrl(url);
                return true;
            }

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            if (url.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            } catch(Exception e) {
                Toast.makeText(context, "??? ????????? ???????????????..", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d("MainActivity", "onReceivedError");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (request.isForMainFrame()) { // isForMainFrame() ??? LOLLIPOP ?????? ??????
                    view.loadUrl("file:///android_asset/error_network.html");
                }
            }
        }

    }

    private boolean isHome(String url) {
        return url.endsWith("8080/") || url.endsWith("8080");
    }

    private boolean isCustomNetworkErrorPage(String url) {
        return url.endsWith("error_network.html");
    }

    private boolean isModal(String url) {
        return webView.getUrl().contains("#modal-");
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}