package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

public class WebSiteActivity extends AppCompatActivity {

    WebView webView;
    String webName;
    EditText loadWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_site);

        webView = findViewById(R.id.webView);
        loadWeb = findViewById(R.id.loadWeb);

    }

    public void loadWebBtn(View view) {
        webName = loadWeb.getText().toString();
        webView.loadUrl("https://" + webName);
    }
}