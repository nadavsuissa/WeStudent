package com.project.westudentmain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText user_name, pass_word;
    private Button btn_login, btn_sign;
    FirebaseAuth mAuth; // private?
    private static final String TAG = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connect_items_by_id();

        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(var -> {
            String email = user_name.getText().toString().trim();
            String password = pass_word.getText().toString().trim();

            // or just email or password are not correct
            if (email.isEmpty()) {
                Log.d(TAG,"test");
                user_name.setError("Email is empty");
                user_name.requestFocus();
                return;
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user_name.setError("Enter valid email");
                user_name.requestFocus();
                return;
            }
            else if (password.isEmpty()) {
                pass_word.setError("Password is empty");
                pass_word.requestFocus();
                return;
            }
            else if (password.length() < 6) {
                pass_word.setError("Password length needs to be at least 6");
                pass_word.requestFocus();
                return;
            }
            //TODO: check if user already logged in
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, MainActivity.class));
                } else {
                    Toast.makeText(Login.this,
                            "email or password are not correct",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
        btn_sign.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));
    }

    private void connect_items_by_id() {
        user_name = findViewById(R.id.loginemail);
        pass_word = findViewById(R.id.loginPassword);
        btn_login = findViewById(R.id.login1);
        btn_sign = findViewById(R.id.signup1);
    }
}