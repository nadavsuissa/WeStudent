package com.project.westudentmain.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidproject.R;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseLogin;

public class showSettings extends AppCompatActivity {
    private User my_user;
    private Button btn_dissconnect, btn_delete_user, btn_edit_profile, btn_notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_settings);
        connect_items_by_id();

        FireBaseData.getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                my_user = (User)data;
            }

            @Override
            public void onCancelled(@NonNull String error) {

            }
        });

        btn_dissconnect.setOnClickListener(var -> {
            FireBaseLogin.signOut();
            finish();
            startActivity(new Intent(showSettings.this, Login.class));
        });

        btn_edit_profile.setOnClickListener(view -> {
            startActivity(new Intent(showSettings.this, EditProfileActivity.class));
        });
        btn_delete_user.setOnClickListener(v -> {
            popUpDialog();

        });
    }

    private void popUpDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set title
                .setTitle("Are you sure you want to delete your account?")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        FireBaseData.deleteUser(my_user.getUserName(),(what, ok) -> {
                            startActivity(new Intent(showSettings.this, Login.class));
                        });
                    }
                })
                //set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    private void connect_items_by_id() {
        btn_dissconnect = findViewById(R.id.btndissconect);
        btn_delete_user = findViewById(R.id.btndeleteuser);
        btn_edit_profile = findViewById(R.id.btneditprofile);
        btn_notifications = findViewById(R.id.btnnotifications);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(showSettings.this, showProfile.class)); // this is how to move between screens
    }
}