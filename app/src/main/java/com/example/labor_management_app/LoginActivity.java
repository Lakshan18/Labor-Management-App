package com.example.labor_management_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private Button loginBtn;

    private FirebaseFirestore db;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("SupervisorSession", Context.MODE_PRIVATE);

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

        db.collection("supervisor")
                .whereEqualTo("username",username)
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       QuerySnapshot querySnapshot = task.getResult();
                       if(querySnapshot != null && !querySnapshot.isEmpty()){
                          for(QueryDocumentSnapshot documentSnapshot : querySnapshot){
                              Log.d("Firestore Data :", documentSnapshot.getData().toString());

                              String supervisorId = documentSnapshot.getId();
                              String dbPassword = documentSnapshot.getString("password");
                              String dbMobile = documentSnapshot.getString("mobile");


                              if(password.equals(dbPassword)){
                                  SharedPreferences.Editor editor = sharedPreferences.edit();
                                  editor.putString("supId",supervisorId);
                                  editor.putString("supMobile", dbMobile);
                                  editor.putLong("lastLoggedTime",System.currentTimeMillis());
                                  editor.apply();

                                  navigateToHome();
                              }else{
                                  CustomToast.showToast(LoginActivity.this,"Invalid Credentials!",false);
                              }
                          }
                       }
                   }
                });
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}