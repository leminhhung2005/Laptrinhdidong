package com.example.btap08.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btap08.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // LƯU Ý: tên layout phải đúng với file em tạo
        // nếu file là activity_webview.xml thì sửa lại cho khớp:
        setContentView(R.layout.activity_web_view);
        // hoặc R.layout.activity_web_view nếu em đặt tên như vậy

        webView = findViewById(R.id.webview);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // Trang responsive theo yêu cầu
        webView.loadUrl("http://iotstar.vn");
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
