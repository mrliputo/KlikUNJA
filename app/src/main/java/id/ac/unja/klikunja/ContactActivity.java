package id.ac.unja.klikunja;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    Toolbar mToolbar;
    TextInputLayout textInputSubject, textInputMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mToolbar = findViewById(R.id.msg_toolbar);
        textInputSubject = findViewById(R.id.text_input_subject);
        textInputMessage = findViewById(R.id.text_input_message);
        btnSend = findViewById(R.id.btn_send);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Kontak");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateSubject() | !validateMessage()) {
                    return;
                }

                String subject = textInputSubject.getEditText().getText().toString();
                String message = textInputMessage.getEditText().getText().toString();

                if(isNetworkAvailable()) {
                    sendEmail(subject, message);
                } else {
                    Toast.makeText(ContactActivity.this, "You're offline. Check your connection.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean validateSubject() {
        String subjectInput = textInputSubject.getEditText().getText().toString().trim();

        if (subjectInput.isEmpty()) {
            textInputSubject.setError("Harus diisi");
            return false;
        } else {
            textInputSubject.setError(null);
            return true;
        }
    }

    private boolean validateMessage() {
        String messageInput = textInputMessage.getEditText().getText().toString().trim();

        if (messageInput.isEmpty()) {
            textInputMessage.setError("Harus diisi");
            return false;
        } else {
            textInputMessage.setError(null);
            return true;
        }
    }

    private void sendEmail(String subject, String text) {
        String uriText =
                "mailto:humas@unja.ac.id" +
                        "?subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(text);

        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);

        try {
            startActivity(Intent.createChooser(sendIntent, "Kirim email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this, "Tidak ada email client terinstal.",
                    Toast.LENGTH_SHORT).show();
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
