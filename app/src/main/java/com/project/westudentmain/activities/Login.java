package com.project.westudentmain.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.westudentmain.Validation;
import com.project.westudentmain.util.CustomOkListener;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseLogin;
import com.project.westudentmain.util.FireBaseToken;

public class Login extends AppCompatActivity {
    private EditText user_name, pass_word;
    private Button btn_login, btn_sign;
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connect_items_by_id();

        btn_login.setOnClickListener(var -> {
            String email = user_name.getText().toString().trim();
            String password = pass_word.getText().toString().trim();

            // check for wrong input from user
            Validation validation = new Validation();
            boolean flag = validation.LoginPassword(pass_word, password);
            if (!flag) return;
            validation.LoginEmailOrUser(user_name, email, new CustomOkListener() {
                @Override
                public void onComplete(@NonNull String what, Boolean ok) {
                    if (ok) {
                        //TODO: check if user already logged in
                        //TODO: show progress bar
                        //TODO: close this page when login successful
                        //TODO: check network fail
                        FireBaseLogin.emailLogin(what, password, task -> {
                            // TODO: start loading fragment before the last line and close in here
                            if (task.isSuccessful()) {
                                FireBaseData.isUniversity((what1, ok1) -> {
                                    if (ok1){
                                        Toast.makeText(getBaseContext(), what1, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this, showUniversityAccount.class));
                                    }else {
                                        startActivity(new Intent(Login.this, showProfile.class));
                                        //send token
                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    private static final String TAG = "token";

                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                            return;
                                                        }
                                                        String token = task.getResult();
                                                        FireBaseToken.updateToken(token, (what, ok) -> Log.w(TAG, what+" "+ok));
                                                    }
                                                });
                                        //TODO:close this activity?
                                    }
                                });

                            } else {
                                Toast.makeText(Login.this,
                                        "email or password are not correct" + what,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else
                        Toast.makeText(Login.this, what, Toast.LENGTH_SHORT).show();
                }
            });

        });
        btn_sign.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        finishAffinity();
                        System.exit(0);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                    }
                })
                .show();

    }

    private void connect_items_by_id() {
        user_name = findViewById(R.id.loginemail);
        pass_word = findViewById(R.id.loginPassword);
        btn_login = findViewById(R.id.login1);
        btn_sign = findViewById(R.id.signup1);
    }
}