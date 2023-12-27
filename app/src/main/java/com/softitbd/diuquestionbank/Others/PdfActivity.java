package com.softitbd.diuquestionbank.Others;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.softitbd.diuquestionbank.R;

public class PdfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        // Get the file URI from the intent
        String fileUri = getIntent().getStringExtra("fileUri");
        getSupportActionBar().setTitle(fileUri);
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (fileUri != null) {
            // Display the PDF file
            loadPdf(webView, fileUri);
        } else {
            // Handle the case when file URI is not provided
            // You may show an error message or take appropriate action
        }
    }
    private void loadPdf(WebView webView, String fileUrl) {
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + fileUrl);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
    }



}