package com.project.westudentmain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.androidproject.R;

public class showProject extends AppCompatActivity {
    private TextView projecttypetview,subjecttview,participantstview,descriptiontview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project);
        connect_items_by_id();
    }
    private void connect_items_by_id() {
        projecttypetview = findViewById(R.id.projecttextview);
        subjecttview = findViewById(R.id.subjecttextview);
        participantstview = findViewById(R.id.participantstextview);
        descriptiontview = findViewById(R.id.descriptiontextview);


    }
}