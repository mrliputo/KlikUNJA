package id.ac.unja.klikunja;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView mTitle, mDate, noInet;
    ProgressBar mProgress;
    WebView webView;
    String url, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        toolbar = findViewById(R.id.notice_toolbar);
        mTitle = findViewById(R.id.notice_title);
        mDate = findViewById(R.id.notice_date);
        mProgress = findViewById(R.id.notice_bar);
        noInet = findViewById(R.id.notice_no_inet);
        webView = findViewById(R.id.notice_webView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Informasi");

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");

        mTitle.setText(title);
        mDate.setText(intent.getStringExtra("publishedAt"));

        if (isNetworkAvailable()) {
            initWebView(url);
        } else {
            mProgress.setVisibility(View.GONE);
            noInet.setVisibility(View.VISIBLE);
        }
    }

    private void initWebView(String url){
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setVisibility(View.GONE);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

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

                mProgress.setVisibility(View.GONE);
                noInet.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsshareandbrowser_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.view_web) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            return true;
        }else if(id == R.id.share) {
            try{
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                String body = title + "\n\n" + url + "\n\n" + "Shared from KlikUNJA";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share info"));
            }catch(Exception e) {
                Toast.makeText(this, "Hmm.. That's weird.", Toast.LENGTH_SHORT).show();
            }
        } else if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
