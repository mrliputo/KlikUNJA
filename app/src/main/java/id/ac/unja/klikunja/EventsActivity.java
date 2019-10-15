package id.ac.unja.klikunja;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    LinearLayout wvLoading;
    Toolbar toolbar;
    WebView webView;
    RelativeLayout errorLayout;
    ImageView errorImage;
    TextView errorTitle, errorMessage;
    Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        progressBar = findViewById(R.id.event_progressbar);
        toolbar = findViewById(R.id.events_toolbar);
        webView = findViewById(R.id.events_wv);
        errorLayout = findViewById(R.id.errorLayout);
        errorImage = findViewById(R.id.errorImage);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        btnRetry = findViewById(R.id.btnRetry);
        wvLoading = findViewById(R.id.wv_loading);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init("https://www.unja.ac.id/events/hari-ini/");

    }

    private void webViewHander(String url) {
        wvLoading.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode((WebSettings.LOAD_NO_CACHE));
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setProgress(0);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressBar.setProgress(0);

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('top-bar')[0].style.display='none'; " +
                        "document.getElementsByClassName('container')[1].style.display='none'; " +
                        "document.getElementsByClassName('vertical-space2')[0].style.display='none'; " +
                        "document.getElementsByClassName('w-next-article')[0].style.display='none'; " +
                        "document.getElementsByClassName('w-prev-article')[0].style.display='none'; " +
                        "document.getElementsByClassName('sidebar')[0].style.display='none'; " +
                        "document.getElementsByClassName('rec-posts')[0].style.display='none'; " +
                        "document.getElementsByClassName('post-trait-w')[0].style.display='none'; " +
                        "document.getElementsByClassName('au-avatar-box')[0].style.display='none'; " +
                        "document.getElementsByClassName('postmetadata')[0].style.display='none'; " +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('header').style.display='none'; " +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('headline').style.display='none'; " +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('pre-footer').style.display='none'; " +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('footer').style.display='none'; " +
                        "})()");

                wvLoading.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                wvLoading.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }

    private void showErrorMessage(int imageView, String title, String message, final String url) {
        if(errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(url);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void init(String url) {
        if(isNetworkAvailable()) {

            if(errorLayout.getVisibility() == View.VISIBLE) {
                errorLayout.setVisibility(View.GONE);
            }

            webViewHander(url);
        }else{
            showErrorMessage(
                    R.drawable.oops,
                    "Network Error",
                    "Umm.. You need the Internet for this",
                    url);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
