package com.project.westudentmain.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.androidproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.classes.GroupData;

import java.lang.reflect.Type;

public class showspecificGroup extends AppCompatActivity {
    private Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);



        if(getIntent().hasExtra("from group_adapter")){
            Gson gson = new Gson();
            Type type = new TypeToken<GroupData>(){}.getType();
            String group_json = getIntent().getStringExtra("from group_adapter");
            GroupData group = gson.fromJson(group_json,type );
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mi_allpostedprojects:
                startActivity(new Intent(this, showProject.class));
                return true;
            case R.id.mi_yourgroups:
                startActivity(new Intent(this, showGroup.class));
                return true;
            case R.id.mi_settings:
                startActivity(new Intent(this, showSettings.class));
                return true;
            case R.id.mi_your_profile:
                startActivity(new Intent(this, showProfile.class));
                return true;
            case R.id.mi_chat:
                startActivity(new Intent(this, showChat.class));
                return true;
            case R.id.mi_create_group:
                startActivity(new Intent(this, createGroup.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}