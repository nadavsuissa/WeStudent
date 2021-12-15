package com.project.westudentmain.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidproject.R;
import com.project.westudentmain.classes.Profile;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseData;

public class showProfile extends AppCompatActivity {
    private Toolbar mToolBar; // WTF is that

    private ImageView img_profile; //TODO: show the pic
    private TextView txt_user_name, txt_name, txt_university, txt_department, txt_degree, txt_year, txt_bio;
    private Button btn_all_groups, btn_my_groups, btn_friends, btn_users, btn_update_user_data;

    private FireBaseData fire_base_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        connect_items_by_id();

        //??
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);

        fire_base_data = FireBaseData.getInstance();


        fire_base_data.getUserData(User.class, new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user = (User) data;
                RenderText(user);
            }

            @Override
            public void onCancelled(@NonNull String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
                //TODO: jump back to login
            }
        });

        //TODO: give them what to look for
        btn_all_groups.setOnClickListener(view -> {
            startActivity(new Intent(this, showGroup.class));
        });
        btn_my_groups.setOnClickListener(view -> {
            startActivity(new Intent(this, showGroup.class));
        });
        btn_friends.setOnClickListener(view -> {
            startActivity(new Intent(this, showFriends.class));
        });
        btn_users.setOnClickListener(view -> {
            startActivity(new Intent(this, showFriends.class));
        });
        btn_update_user_data.setOnClickListener(view -> {
            startActivity(new Intent(this, showSettings.class));
        });
    }

    private void RenderText(@NonNull User user) {
        txt_name.setText(user.getName());
        txt_user_name.setText(user.getUserName());

        Profile profile = user.getProfile();
        if (profile != null) {
            txt_university.setText(String.format("University: %s", profile.getUniversity()));
            txt_department.setText(String.format("Department: %s", profile.getDepartment()));
            txt_degree.setText(String.format("Degree: %s", profile.getDegree()));
            txt_year.setText(String.format("Year: %s", profile.getYear()));
            txt_bio.setText(profile.getBIO());
        } // TODO: add else

    }

    private void connect_items_by_id() {
        img_profile = findViewById(R.id.profilepicview);

        txt_name = findViewById(R.id.textViewName);
        txt_user_name = findViewById(R.id.textViewUserName);
        txt_university = findViewById(R.id.textViewUniversity);
        txt_department = findViewById(R.id.textViewDepartment);
        txt_degree = findViewById(R.id.textViewDegree);
        txt_year = findViewById(R.id.textViewYear);

        txt_bio = findViewById(R.id.multiLineTextBio);

        btn_all_groups = findViewById(R.id.buttonAllGroup);
        btn_my_groups = findViewById(R.id.buttonMyGroup);
        btn_friends = findViewById(R.id.buttonFriends);
        btn_users = findViewById(R.id.buttonUsers);
        btn_update_user_data = findViewById(R.id.buttonUpdateUserData);

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}