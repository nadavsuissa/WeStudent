package com.project.westudentmain.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.project.westudentmain.Validation;

import com.project.westudentmain.util.FireBaseLogin;

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
            Validation validation =new Validation();
            boolean flag = validation.RegisterLogin(user_name, pass_word,email,password);
            if(!flag) return;

            //TODO: check if user already logged in
            //TODO: show progress bar
            //TODO: close this page when login successful
            //TODO: check network fail
           FireBaseLogin.emailLogin(email,password, task -> {
               // TODO: start loading fragment before the last line and close in here
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, MainActivity.class));
                } else {
                    Toast.makeText(Login.this,
                            "email or password are not correct",
                            Toast.LENGTH_LONG).show();
                }
            });
        });
        btn_sign.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set title
                .setTitle("Are you sure to Exit?")
//set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        finishAffinity();
                        System.exit(0);
                    }
                })
//set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
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