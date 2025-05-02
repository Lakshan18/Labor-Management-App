package com.example.labor_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField, passwordField; // Changed from TextView to EditText
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameField = findViewById(R.id.Edit_Username_inp1); // Match your XML ID
        passwordField = findViewById(R.id.editTextTextPassword); // Match your XML ID
        loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(v -> validateLoginUser());
    }

    private void validateLoginUser() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameField.setError("Please Enter Your Username!");
            usernameField.requestFocus();
        } else if (TextUtils.isEmpty(password) || password.length() < 10) {
            passwordField.setError("Password must be at least 10 characters!");
            passwordField.requestFocus();
        } else {
            checkUserInDB(username, password);
        }
    }

    private void checkUserInDB(String username, String password) {
        // Replace with your actual authentication logic
        if (isValidUser(username, password)) {
            navigateToHome();
        } else {
            showLoginError();
        }
    }

    private boolean isValidUser(String username, String password) {
        // Add your real authentication logic here
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showLoginError() {
        CustomToast.showToast(this, "Invalid credentials", false);
    }
}