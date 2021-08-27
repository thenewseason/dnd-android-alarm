package com.example.webviewexample;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewInterface {

    private Context mContext;
    private WebView mWebView;

    public WebViewInterface(Context context, WebView webView) {
        mContext = context;
        mWebView = webView;
    }

    @JavascriptInterface
    public String showToastAndReturnString(String value) {
        Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
        return "my string~!";
    }
}
