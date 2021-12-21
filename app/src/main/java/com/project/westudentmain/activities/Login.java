package com.project.westudentmain.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.westudentmain.Validation;

import com.project.westudentmain.classes.testing;
import com.project.westudentmain.util.FireBaseLogin;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText user_name, pass_word;
    private Button btn_login, btn_sign;
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connect_items_by_id();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        testing t = new testing();
//        t.add("qwertyuiop","123456789");
//        t.add("121","123456789");
//        database.getReference().child("qwerty").setValue(t).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });

//        database.getReference().child("qwerty").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                testing t = snapshot.getValue(testing.class);
//                if (snapshot == null){
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        btn_login.setOnClickListener(var -> {
            String email = user_name.getText().toString().trim();
            String password = pass_word.getText().toString().trim();

            // check for wrong input from user
            Validation validation =new Validation();
            boolean flag = validation.Login(user_name, pass_word,email,password);
            if(!flag) return;

            //TODO: check if user already logged in
            //TODO: show progress bar
            //TODO: close this page when login successful
            //TODO: check network fail
           FireBaseLogin.emailLogin(email,password, task -> {
               // TODO: start loading fragment before the last line and close in here
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, showProfile.class));
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