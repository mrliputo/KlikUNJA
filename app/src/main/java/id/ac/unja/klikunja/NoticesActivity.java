package id.ac.unja.klikunja;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.ac.unja.klikunja.api.ApiClient;
import id.ac.unja.klikunja.api.ApiInterface;
import id.ac.unja.klikunja.models.Notices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<Notices.Items> allArticles = new ArrayList<>();
    private NoticesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar paginationProgress;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;

    // Pagination variables
    private int pageNumber = 1;
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        Toolbar newsToolbar = findViewById(R.id.news_toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recyclerView);
        paginationProgress =findViewById(R.id.pagination_progress);
        errorLayout = findViewById(R.id.errorLayout);
        errorImage = findViewById(R.id.errorImage);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        btnRetry = findViewById(R.id.btnRetry);

        layoutManager = new LinearLayoutManager(NoticesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setSupportActionBar(newsToolbar);
        getSupportActionBar().setTitle("Informasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        initContent();
    }

    private void resetPagination() {
        pastVisibleItems = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        previousTotal = 0;
        pageNumber = 1;
    }

    @Override
    public void onRefresh() {
        resetPagination();
        loadJson();
    }

    public void loadJson() {

        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Notices> call = apiInterface.getNotices(1);

        call.enqueue(new Callback<Notices>() {
            @Override
            public void onResponse(Call<Notices> call, Response<Notices> response) {
                if(response.isSuccessful() && response.body() != null) {

                    allArticles = response.body().getItems();
                    adapter = new NoticesAdapter(allArticles, NoticesActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();

                    if(String.valueOf(response.body()).equals("[]")) {
                        showNotFoundMessage(
                                R.drawable.no_result,
                                "I wish there was something, but there's not",
                                "We couldn't find what you're looking for. Sorry :(");
                    }

                }else{
                    Toast.makeText(NoticesActivity.this, "No response from server", Toast.LENGTH_SHORT).show();
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Notices> call, Throwable t) {
                showErrorMessage(
                        R.drawable.oops,
                        "Oops",
                        "It looks like you're offline.");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void initListener() {
        adapter.setOnItemClickListener(new NoticesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(NoticesActivity.this, NoticeDetailActivity.class);

                Notices.Items article = allArticles.get(position);

                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("publishedAt", Utils.DateFormat(article.getDate_published()));

                startActivity(intent);
            }
        });
    }

    private void initScrollRecycler() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if(dy > 0) {
                    if(isLoading) {
                        if(totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if(!isLoading && (totalItemCount-visibleItemCount) <= pastVisibleItems) {
                        pageNumber++;
                        performPagination();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void performPagination() {
        paginationProgress.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Notices> call = apiInterface.getNotices(pageNumber);

        call.enqueue(new Callback<Notices>() {
            @Override
            public void onResponse(Call<Notices> call, Response<Notices> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Notices.Items> articles = response.body().getItems();
                    adapter.addItem(articles);
                    initListener();
                }

                paginationProgress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<Notices> call, Throwable t) {
                paginationProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(NoticesActivity.this, "You're offline. Check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void onLoadingSwipeRefresh(final String keyword) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadJson();
            }
        });
    }


    private void showErrorMessage(int imageView, String title, String message) {
        if(errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            btnRetry.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initContent();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initContent() {
        if(isNetworkAvailable()) {
            if(errorLayout.getVisibility() == View.VISIBLE) {
                errorLayout.setVisibility(View.GONE);
            }

            onLoadingSwipeRefresh("");
            initScrollRecycler();
        }else{
            showErrorMessage(
                    R.drawable.oops,
                    "Network Error",
                    "Umm.. You need the Internet for this");
        }
    }

    private void showNotFoundMessage(int imageView, String title, String message) {
        if(errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            btnRetry.setVisibility(View.GONE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);
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
