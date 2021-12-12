package com.project.westudentmain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.project.westudentmain.util.FireBase;

public class Register extends AppCompatActivity {
    private Button btn2_signup;
    private EditText user_name, pass_word;
//    FirebaseAuth mAuth;
    private FireBase fire_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        connect_items_by_id();

        fire_base = FireBase.getInstance();
//        mAuth = FirebaseAuth.getInstance();

        btn2_signup.setOnClickListener(v -> {
            String email = user_name.getText().toString().trim();
            String password = pass_word.getText().toString().trim();

            if (email.isEmpty()) {
                user_name.setError("Email is empty");
                user_name.requestFocus();
                return;
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user_name.setError("Enter the valid email address");
                user_name.requestFocus();
                return;
            }
            else if (password.isEmpty()) {
                pass_word.setError("Enter a password");
                pass_word.requestFocus();
                return;
            }
            else if (password.length() < 6) {
                pass_word.setError("Password length needs to be at least 6");
                pass_word.requestFocus();
                return;
            }
//            User user = new User("user_name", "name", "last_name", email, "0563214798");

            // TODO: add on fail listener
            fire_base.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

//                    FireBase fireBase = FireBase.getInstance();
//                    fireBase.updateData(user);

                    Toast.makeText(Register.this, "You are successfully Registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this, MainActivity.class));
                } else {
                    Toast.makeText(Register.this, "Error in  Registration! Try again", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this, Login.class));
                }
            });

        });

    }

    private void connect_items_by_id() {
        user_name = findViewById(R.id.registeremail);
        pass_word = findViewById(R.id.registerpassword);
        btn2_signup = findViewById(R.id.signup2);
    }

}