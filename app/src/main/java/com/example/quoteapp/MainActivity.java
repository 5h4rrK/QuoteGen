package com.example.quoteapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {
    TextView quoteDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get text view to display quote in
        quoteDisplay = (TextView) findViewById(R.id.textView);

        // Quote API Call
        // Using "https://favqs.com/api" to get the quotes

        // In android studio, cannot run network tasks on main thread as it can lead to slowing down the UI.
        // So create a new thread to get the quote from the API.

    }

    public void getQuoteClick(View view) {
        Callable<String> quoteApi  = new QuoteApiCallable();
        FutureTask<String> futureApiCall = new FutureTask<>(quoteApi);
        Thread networkThread = new Thread(futureApiCall);
        networkThread.start();
        try {
            String output = futureApiCall.get();
            quoteDisplay.setText(output);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Maybe put all this in the on create
    }
}