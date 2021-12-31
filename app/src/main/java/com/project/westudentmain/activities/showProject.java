package com.project.westudentmain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidproject.R;

public class showProject extends AppCompatActivity {
    private Toolbar mToolBar;

    private TextView projecttypetview, subjecttview, participantstview, descriptiontview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project);
        connect_items_by_id();
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
    }

    private void connect_items_by_id() {



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.drawable.ic_action_setting, menu);
        return true;
    }


}