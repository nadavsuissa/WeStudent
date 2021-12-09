package com.project.westudentmain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.androidproject.R;

public class showSettings extends AppCompatActivity {
    private Button dissconnectbtn,changepassbtn,editprofilebtn,changepicbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_settings);
        connect_items_by_id();
    }
    private void connect_items_by_id() {
        dissconnectbtn = findViewById(R.id.btndissconect);
        changepassbtn = findViewById(R.id.btnchangepass);
        editprofilebtn = findViewById(R.id.btneditprofile);
        changepicbtn = findViewById(R.id.btnchangeprofpic);


    }
}