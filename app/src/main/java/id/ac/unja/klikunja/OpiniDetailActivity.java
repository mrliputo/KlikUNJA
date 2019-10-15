package id.ac.unja.klikunja;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class OpiniDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView mTitle, mAuthor, mContent;
    String title, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opini_detail);

        toolbar = findViewById(R.id.opini_toolbar);
        mTitle = findViewById(R.id.opini_title);
        mAuthor = findViewById(R.id.opini_author);
        mContent = findViewById(R.id.opini_body);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Opini");

        setText();
    }

    private void setText() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");

        mTitle.setText(title);
        mAuthor.setText(intent.getStringExtra("author"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mContent.setText(Html.fromHtml(intent.getStringExtra("content"), Html.FROM_HTML_MODE_COMPACT));
        } else {
            mContent.setText(Html.fromHtml(intent.getStringExtra("content")));
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
                startActivity(Intent.createChooser(intent, "Share article"));
            }catch(Exception e) {
                Toast.makeText(this, "Hmm.. That's weird.", Toast.LENGTH_SHORT).show();
            }
        } else if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
