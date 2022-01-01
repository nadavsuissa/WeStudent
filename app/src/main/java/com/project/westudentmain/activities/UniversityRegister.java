package com.project.westudentmain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.androidproject.R;

public class UniversityRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_register);
        findViewById(R.id.button_university_register).setOnClickListener(view -> startActivity(new Intent(UniversityRegister.this,Login.class)));

    }
}