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

    private EditText usernameField, passwordField;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.Edit_Username_inp1);
        passwordField = findViewById(R.id.editTextTextPassword);
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
        if (isValidUser(username, password)) {
            navigateToHome(username,password);
        } else {
            showLoginError();
        }
    }

    private boolean isValidUser(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }

    private void navigateToHome(String uname,String pwd) {
        CustomToast.showToast(this,uname+" & "+ pwd, true);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showLoginError() {
        CustomToast.showToast(this, "Invalid credentials", false);
    }
}