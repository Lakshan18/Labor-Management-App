package com.example.labor_management_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

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

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private BroadcastReceiver networkReciever;
    private AlertDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("SupervisorSession", Context.MODE_PRIVATE);

        networkReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkNetworkConnection();
            }
        };
        checkNetworkConnection();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (noInternetDialog != null && noInternetDialog.isShowing()) {
                finishAffinity();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            showNoInternetDialog();
        } else {
            if (noInternetDialog != null && noInternetDialog.isShowing()) {
                noInternetDialog.dismiss();
            }
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

                    long lastLoggedTime = sharedPreferences.getLong("lastLoggedTime", -1);
                    String supId = sharedPreferences.getString("supId",null);

                    if(supId == null){
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }else{
                       if(lastLoggedTime != -1){
                           long currentTime = System.currentTimeMillis();
                           long timeDifference = currentTime - lastLoggedTime;

                           if (timeDifference <= 48 * 60 * 60 * 1000) {
                               Log.d("MainActivity", "Session valid, redirecting to HomeActivity");
                               Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                               startActivity(intent);
                               finish();
                           }
                       }
                    }
                });
            }).start();

        }
    }

    private void showNoInternetDialog() {
        if (noInternetDialog == null || !noInternetDialog.isShowing()) {
            View dialogView = getLayoutInflater().inflate(R.layout.custom_no_internet_dialog, null);

            Button retryButton = dialogView.findViewById(R.id.dialog_retry_button);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            builder.setCancelable(false);

            noInternetDialog = builder.create();
            noInternetDialog.show();

            retryButton.setOnClickListener(v -> {
                checkNetworkConnection();
            });
        }
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