package com.project.westudentmain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidproject.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu,menu);
         return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mi_allpostedprojects:
                startActivity(new Intent(MainActivity.this, showProject.class));
                return true;
            case R.id.mi_yourgroups:
                startActivity(new Intent(MainActivity.this, showGroup.class));
                return true;
            case R.id.mi_settings:
                startActivity(new Intent(MainActivity.this, showSettings.class));
                return true;
            case R.id.mi_your_profile:
                startActivity(new Intent(MainActivity.this, showProfile.class));
                return true;
            case R.id.mi_home:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;
            case R.id.mi_chat:
                startActivity(new Intent(MainActivity.this, showChat.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}