package id.ac.unja.klikunja;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private boolean isHideToolbarView = false;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private String mUrl;
    private String mTitle;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar = findViewById(R.id.newsdetail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        date_behavior = findViewById(R.id.date_behavior);
        titleAppbar = findViewById(R.id.title_appbar);
        ImageView imageView = findViewById(R.id.backdrop);
        TextView appbar_title = findViewById(R.id.title_on_appbar);
        TextView appbar_subtitle = findViewById(R.id.subtitle_on_appbar);
        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time);
        TextView title = findViewById(R.id.title);
        TextView contentError = findViewById(R.id.cant_load_content);
        progressBar = findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        String mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        String mDate = intent.getStringExtra("date");
        String mAuthor = intent.getStringExtra("author");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        appbar_title.setText("News");
        appbar_subtitle.setText(mTitle);
        date.setText(Utils.DateFormat(mDate));
        title.setText(mTitle);

        String author;
        if (mAuthor != null){
            author = mAuthor;
        } else {
            author = "";
        }

        time.setText(author + " \u2022 " + Utils.DateToTimeFormat(mDate));

        if(isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            initWebView(mUrl);
        }else{
            progressBar.setVisibility(View.GONE);
            contentError.setVisibility(View.VISIBLE);
        }

    }

    private void initWebView(String url){
        final WebView webView = findViewById(R.id.webView);
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

                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            date_behavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            date_behavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
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
            intent.setData(Uri.parse(mUrl));
            startActivity(intent);
            return true;
        }else if(id == R.id.share) {
            try{
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, mTitle);
                String body = mTitle + "\n\n" + mUrl + "\n\n" + "Shared from KlikUNJA";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share news"));
            }catch(Exception e) {
                Toast.makeText(this, "Hmm.. That's weird.", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
