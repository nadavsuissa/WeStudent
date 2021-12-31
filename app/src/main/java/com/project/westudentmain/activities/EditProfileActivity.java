package com.project.westudentmain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidproject.R;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseLogin;
import com.project.westudentmain.util.FireBaseData;

public class EditProfileActivity extends AppCompatActivity {
    private Toolbar mToolBar;

    private Button btn_save;
    private EditText edt_name,edt_surname,edt_phone, edt_address,edt_bio; //TODO:
    private TextView user_name;
    private FireBaseData fire_base_data;

    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();

        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);

        // TODO: if fail then the user is still inserted
        fire_base_data = FireBaseData.getInstance();
        if (!FireBaseLogin.userIsLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        Log.d("edit profile","fire base connected"); // PRINT

        View.OnClickListener save_listener = new View.OnClickListener() { // init only after user data received
            @Override
            public void onClick(View view) {
                FillUser();
                UpdateUser();
                finish();
                startActivity(new Intent(EditProfileActivity.this, showProfile.class));
            }
        };

        FireBaseData.getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                user = (User) data;
                updateScreen(user);

                btn_save.setOnClickListener(save_listener);
            }

            @Override
            public void onCancelled(@NonNull String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
            }
        });



    }

    private void updateScreen(User user) {
        user_name.setText(user.getUserName());

        edt_name.setText(user.getName());
        edt_surname.setText(user.getLastName());
        edt_phone.setText(user.getPhone());
        edt_address.setText(user.getProfile().getHomeTown());
        edt_bio.setText(user.getProfile().getBIO());
    }

    private void FillUser() {

        String s_name = edt_name.getText().toString().trim();
        String s_surname = edt_surname.getText().toString().trim();
        String s_phone_number = edt_phone.getText().toString().trim();
        String s_address = edt_address.getText().toString().trim();
        String s_bio = edt_bio.getText().toString().trim();

        user.setName(s_name);
        user.setLastName(s_surname);
        user.setPhone(s_phone_number);
        user.getProfile().setHomeTown(s_address);
        user.getProfile().setBIO(s_bio);
    }

    private void initViews() {
        edt_name = findViewById(R.id.EditTextName);
        edt_surname = findViewById(R.id.EditTextSurname);
        edt_phone = findViewById(R.id.EditTextPhoneNo);
        btn_save = findViewById(R.id.btnSaveButton);
        edt_address = findViewById(R.id.editTextTextPersonName3);
        edt_bio = findViewById(R.id.editTextTextPersonName4);

        user_name = findViewById(R.id.textView6);
    }

    private void UpdateUser() {

        // TODO: add more fields
        boolean res = fire_base_data.updateUser(user,null);
        if (res)
            Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "User information update ERROR", Toast.LENGTH_LONG).show();

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
            case R.id.mi_create_group:
                startActivity(new Intent(this, createGroup.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}