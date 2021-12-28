package com.project.westudentmain.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.westudentmain.classes.UniversityNotification;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FcmNotificationsSender;
import com.project.westudentmain.util.FireBaseUniversity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class showUniversityAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final Calendar myCalendar = Calendar.getInstance();

    Button send_notification;
    EditText date, head, body;
    Spinner department;
    String string_department;


    String array_department[] = {"computers", "math", "physics", "medical", "engineering", "psychology", "grass"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_university_account);
        FirebaseMessaging.getInstance().subscribeToTopic("all");


        department = findViewById(R.id.notification_department);
        department.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_department);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(ad);

        send_notification = findViewById(R.id.button_send_notification);
        date = findViewById(R.id.notification_date);
        head = findViewById(R.id.notification_head);
        body = findViewById(R.id.notification_body);

        updateLabel();
        TimePickerDialog.OnTimeSetListener time_picker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }
        };
        DatePickerDialog.OnDateSetListener date_picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                new TimePickerDialog(showUniversityAccount.this, time_picker, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(showUniversityAccount.this, date_picker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        send_notification.setOnClickListener(view ->
        {
            String string_date, string_head, string_body;
            string_date = date.getText().toString();
            string_head = head.getText().toString();
            string_body = body.getText().toString();
            if (string_head.isEmpty()) {
                Toast.makeText(this, "head is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            UniversityNotification notification = new UniversityNotification(string_department, string_head, string_body, string_date);

            FireBaseUniversity.getInstance().pushNewNotification(notification, (what, ok) -> {
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        "/topics/all",
                        string_head,
                        string_body,
                        getApplicationContext(),
                        showUniversityAccount.this
                );
                notificationsSender.SendNotifications();
                Toast.makeText(this, what, Toast.LENGTH_SHORT).show();
            });
        });

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(dateFormat.format(myCalendar.getTime()));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long id) {
        string_department = array_department[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}