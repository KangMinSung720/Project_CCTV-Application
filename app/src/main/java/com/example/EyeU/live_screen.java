package com.example.EyeU;

import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class live_screen extends AppCompatActivity {

    private final long finish_time = 2000;
    private long backPressedTime = 0;
    private boolean isFull;
    FrameLayout streamFrame;
    WebView web;
    Button full;
    View.OnClickListener cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_screen);

        isFull = false;

        web = (WebView) findViewById(R.id.StreamScreen);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(false);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("http://192.168.55.195:8081");

        streamFrame = (FrameLayout) findViewById(R.id.streamFrame);
        full = (Button) findViewById(R.id.fullscreen);

        cl= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFullScreen(!isFull);
            }
        };
        full.setOnClickListener(cl);
    }

    public void setFullScreen(boolean full) {
        isFull = full;
        ViewGroup.LayoutParams params = streamFrame.getLayoutParams();

        if (isFull) {
            isFull = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            isFull = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = (int) (metrics.density * 250);
            params.height = height;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void onBackPressed() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        long tempTime = System.currentTimeMillis();
        long intervalTime =  tempTime - backPressedTime;
        if ( 0<= intervalTime && finish_time >= intervalTime) {
            super.onBackPressed();
        }else if (web.canGoBack()){
            web.goBack();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 메인화면으로 가집니다.", Toast.LENGTH_LONG).show();
        }
    }

}

