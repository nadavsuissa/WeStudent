package com.project.westudentmain.activities;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.R;
import com.project.westudentmain.classes.Profile;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseData;

public class showProfile extends AppCompatActivity {
    private ImageView profile_img;
    private TextView txt_university, txt_department, txt_degree, txt_year, txt_bio,txt_home_town;
    private Button btn_settings,appostedbtn,ptilfpartnersbtn,chatbtn;
//    private User user;
    private FireBaseData fire_base_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        connect_items_by_id();

        fire_base_data = FireBaseData.getInstance();
//
//        User user = new User("user_name123", "name", "last_name", FireBaseData.getEmail(), "2134568");
//        fire_base_data.updateData(user,null);

        fire_base_data.getUserData(User.class, new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user = (User) data;
                //TODO: else ask to add profile and check if working
                if(user.getProfile() != null)
                    RenderText(user);
            }

            @Override
            public void onCancelled(@NonNull String error) {
//                user = new User();
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
            }
        });

//        user deletion example:
//        FireBaseLogin.deleteUser("user_name", new CustomDataListener() {
//            @Override
//            public void onDataChange(@NonNull Object data) {
//                if ((int)data<0){
//
//                    Toast.makeText(getBaseContext(), "user deletion code "+ data, Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(showProfile.this,Login.class));
//                }else
//                    Toast.makeText(getBaseContext(), "data deletion times "+ data, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull String error) {
//                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void RenderText(User user) {
        Profile profile = user.getProfile();
        txt_degree.setText("Degree: "+profile.getDegree());
        txt_department.setText(profile.getDepartment());
        txt_university.setText(profile.getUniversity());
        txt_year.setText(""+profile.getYear());
        txt_bio.setText(profile.getBIO());
        txt_home_town.setText(profile.getHomeTown());
    }

    private void connect_items_by_id() {
        profile_img = findViewById(R.id.profilepicview);
        txt_university = findViewById(R.id.universitytextview);
        txt_department = findViewById(R.id.departmenttextview);
        txt_degree = findViewById(R.id.degreetextview);
        txt_year = findViewById(R.id.yeartextview);
        txt_bio = findViewById(R.id.biotextview);
        txt_home_town=findViewById(R.id.fwaitextview); // FWAI - From Where Am I

        btn_settings = findViewById(R.id.btn_settings);
        appostedbtn = findViewById(R.id.btn_apposted);
        ptilfpartnersbtn = findViewById(R.id.btn_ptilfpartners); // PTILF - Projects that i looked for partners
        chatbtn = findViewById(R.id.btn_chat);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(showProfile.this,showSettings.class)); // this is how to move between screens
    }
}