package com.project.westudentmain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.FireBase;
import com.project.westudentmain.util.FireBaseData;

public class EditProfileActivity extends AppCompatActivity {

    private Button btn_save;
    private EditText edt_name,edt_surname,edt_phone; //TODO:
    private FireBase fire_base;
    private TextView txt_email;
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();
        fire_base = FireBase.getInstance();
        if (!fire_base.userIsLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        Log.d("edit profile","fire base connected");


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser();
                finish();
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            }
        });

        txt_email.setText(fire_base.getEmail());

        // TODO: check why it needs to be an array (probably because pointers)
        // get last data if data is empty it will stay null so add something to catch it in `onDataChange`
        final User[] t = {new User()};
        fire_base.getData(t[0], new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t[0] = snapshot.getValue(t[0].getClass());
                FillUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               user = new User();
            }
        });

        fire_base.updateData(user);


    }

    private void FillUser() {

        String name = edt_name.getText().toString().trim();
        String surname = edt_surname.getText().toString().trim();
        String phone_number = edt_phone.getText().toString().trim();
        user = new User("user name",name, surname, "mail",phone_number);
    }

    private void initViews() {
        edt_name = findViewById(R.id.EditTextName);
        edt_surname = findViewById(R.id.EditTextSurname);
        edt_phone = findViewById(R.id.EditTextPhoneNo);
        btn_save = findViewById(R.id.btnSaveButton);
        txt_email = findViewById(R.id.textViewEmailAddress);
    }

    private void UpdateUser() {

        // TODO: add more fields
        boolean res = fire_base.updateData(user);
        if (res)
            Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "User information update ERROR", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO: need to know where did i came from? (register or profile)
    }

    //    private void sendUserData() {
//        // Get "User UID" from Firebase > Authentification > Users.
//    }
}