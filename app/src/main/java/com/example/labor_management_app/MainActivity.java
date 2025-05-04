package com.example.labor_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView loadingText;
    private Handler handler = new Handler();
    private Random random = new Random();
    private final int MIN_DURATION = 5000;
    private final int MAX_DURATION = 8000;
    private int progressStatus = 0;
    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

        final int totalDuration = MIN_DURATION + random.nextInt(MAX_DURATION - MIN_DURATION);

        final int pause1 = random.nextInt(30) + 10;
        final int pause2 = random.nextInt(30) + 50;
        final int pause3 = random.nextInt(10) + 90;

        final int interval1 = totalDuration * (pause1) / 100;
        final int interval2 = totalDuration * (pause2 - pause1) / 100;
        final int interval3 = totalDuration * (pause3 - pause2) / 100;
        final int interval4 = totalDuration * (100 - pause3) / 100;

        new Thread(() -> {
            updateProgress(0, pause1, interval1, "Initializing...");
            randomDelay(800, 1700);
            updateProgress(pause1, pause2, interval2, "Loading data...");
            randomDelay(900, 1800);
            updateProgress(pause2, pause3, interval3, "Processing...");
            randomDelay(1000, 1900);
            updateProgress(pause3, 100, interval4, "Almost there!");

            handler.post(() -> {
                loadingText.setText("Ready!");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                 finish();
            });
        }).start();
    }

    private void updateProgress(int start, int end, int duration, String message) {
        final int steps = end - start;
        final int stepDuration = duration / steps;

        for (int i = 0; i <= steps && isRunning; i++) {
            final int currentProgress = start + i;
            handler.post(() -> {
                progressBar.setProgress(currentProgress);
                loadingText.setText(message + " " + currentProgress + "%");
            });
            try {
                Thread.sleep(stepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void randomDelay(int min, int max) {
        try {
            Thread.sleep(min + random.nextInt(max - min));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}