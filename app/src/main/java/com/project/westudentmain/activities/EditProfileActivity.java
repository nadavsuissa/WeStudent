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
    private EditText edt_name,edt_surname,edt_phone; //TODO:
    private FireBaseData fire_base_data;
    private TextView txt_email;
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


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser();
                finish();
                startActivity(new Intent(EditProfileActivity.this, showProfile.class));
            }
        });

        txt_email.setText(FireBaseData.getEmail());

        // TODO: check why it needs to be an array (probably because pointers)
        // get last data if data is empty it will stay null so add something to catch it in `onDataChange`
//        final User[] t = {new User()};
        fire_base_data.getUserData(User.class, new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data){
                user = (User) data;
                FillUser();
            }

            @Override
            public void onCancelled(@NonNull String error) {
                user = new User();
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
            }

        });

//        fire_base.updateData(user); its null!!!!!!!!!!


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
        boolean res = fire_base_data.updateData(user,null);
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

    //    private void sendUserData() {
//        // Get "User UID" from Firebase > Authentification > Users.
//    }
}