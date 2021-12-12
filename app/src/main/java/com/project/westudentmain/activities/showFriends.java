package com.project.westudentmain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.androidproject.R;

public class showFriends extends AppCompatActivity {
    private Button addfriendbtn,delfriendbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);
        connect_items_by_id();
    }
    private void connect_items_by_id() {
        addfriendbtn = findViewById(R.id.btnaddfriend);
        delfriendbtn = findViewById(R.id.btndeletefriend);
    }
}