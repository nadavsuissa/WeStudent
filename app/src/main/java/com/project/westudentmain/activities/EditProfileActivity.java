package com.project.westudentmain.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.project.westudentmain.UserInformation;
import com.project.westudentmain.util.FireBase;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnsave;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhoneNo;
    private FireBase fire_base;

    public EditProfileActivity() {
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        fire_base = FireBase.getInstance();
        if (!fire_base.userIsLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        editTextName = findViewById(R.id.EditTextName);
        editTextSurname = findViewById(R.id.EditTextSurname);
        editTextPhoneNo = findViewById(R.id.EditTextPhoneNo);
        btnsave = findViewById(R.id.btnSaveButton);
        btnsave.setOnClickListener(this);
        TextView textViewemailname = findViewById(R.id.textViewEmailAddress);
        textViewemailname.setText(fire_base.getEmail());

        // TODO: check why it needs to be an array (probably because pointers)
        // get last data if data is empty it will stay null so add something to catch it in `onDataChange`
        final UserInformation[] t = {new UserInformation()};
        fire_base.getData(t[0], new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t[0] = snapshot.getValue(t[0].getClass());

//                UserInformation as = snapshot.getValue(UserInformation.class);
//                update screen (as)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               //error
            }
        });
    }

    private void userInformation() {
        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String phoneno = editTextPhoneNo.getText().toString().trim();
        UserInformation userinformation = new UserInformation(name, surname, phoneno);
        boolean res = fire_base.updateData(userinformation);
        if (res)
            Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "User information update ERROR", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {
        if (view == btnsave) {
            userInformation();
            sendUserData();
            finish();
            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));

        }
    }
    private void sendUserData() {
        // Get "User UID" from Firebase > Authentification > Users.
    }
}