package com.project.westudentmain.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.androidproject.R;
import com.project.westudentmain.util.FireBaseLogin;

public class showSettings extends AppCompatActivity {
    private Toolbar mToolBar;

    private Button dissconnectbtn,changepassbtn,editprofilebtn,changepicbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_settings);
        connect_items_by_id();
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
        dissconnectbtn.setOnClickListener(var -> {
            FireBaseLogin.signOut();
            finish();
            startActivity(new Intent(showSettings.this, Login.class));
        });
    }
    private void connect_items_by_id() {
        dissconnectbtn = findViewById(R.id.btndissconect);
        changepassbtn = findViewById(R.id.btnchangepass);
        editprofilebtn = findViewById(R.id.btneditprofile);
        changepicbtn = findViewById(R.id.btnchangeprofpic);


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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}