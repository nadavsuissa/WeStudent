package com.project.westudentmain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidproject.R;

public class showChat extends AppCompatActivity {

    private EditText input;
    private Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chat);
        TextView message = findViewById(R.id.textOutput); // TextOutput From XML
        connect_items_by_id();


    }
    private void connect_items_by_id() {
        input = findViewById(R.id.textInput);
        btn_send=findViewById(R.id.btnSend);

    }
}