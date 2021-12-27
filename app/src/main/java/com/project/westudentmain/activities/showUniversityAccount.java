package com.project.westudentmain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.androidproject.R;
import com.project.westudentmain.classes.UniversityNotification;
import com.project.westudentmain.util.FireBaseUniversity;

import java.util.ArrayList;

public class showUniversityAccount extends AppCompatActivity {
    Button send_notification;
    EditText  date, head, body;
    Spinner department;
    String string_department;


//    String array_department[] = {"computers","math","physics","medical","engineering","psychology","grass"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_university_account);

        department = findViewById(R.id.notification_department);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.department, android.R.layout.select_dialog_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
//        adapter.addAll();
        department.setAdapter(adapter);

        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Register.this, parent.getItemAtPosition(position)+" Selected", Toast.LENGTH_SHORT).show();
                string_department = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                string_department ="computers";
            }
        });

        send_notification = findViewById(R.id.button_send_notification);
        department = findViewById(R.id.notification_department);
        date = findViewById(R.id.notification_date);
        head = findViewById(R.id.notification_head);
        body = findViewById(R.id.notification_body);

        send_notification.setOnClickListener(view -> {
            String  string_date, string_head, string_body;
//            string_department = department.getText().toString();
            string_date = date.getText().toString();
            string_head = head.getText().toString();
            string_body = body.getText().toString();
            if (string_head.isEmpty()){
                Toast.makeText(this, "head is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            UniversityNotification notification = new UniversityNotification(string_department,string_head,string_body,string_date);
            FireBaseUniversity.getInstance().pushNewNotification(notification,(what, ok) -> {
                Toast.makeText(this, what, Toast.LENGTH_SHORT).show();
            });
        });

    }
}