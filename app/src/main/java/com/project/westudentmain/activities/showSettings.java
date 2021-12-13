package com.project.westudentmain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidproject.R;
import com.project.westudentmain.util.FireBase;

public class showSettings extends AppCompatActivity {
    private Button dissconnectbtn,changepassbtn,editprofilebtn,changepicbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_settings);
        connect_items_by_id();
        dissconnectbtn.setOnClickListener(var -> {
            FireBase.signOut();
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
}