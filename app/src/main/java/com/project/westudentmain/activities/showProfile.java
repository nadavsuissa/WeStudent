package com.project.westudentmain.activities;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.project.westudentmain.classes.Profile;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.FireBase;

public class showProfile extends AppCompatActivity {
    private ImageView profile_img;
    private TextView txt_university, txt_department, txt_degree, txt_year, txt_bio,txt_home_town;
    private Button settingsbtn,appostedbtn,ptilfpartnersbtn,chatbtn;
    private User user;
    private FireBase fire_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        connect_items_by_id();

        fire_base = FireBase.getInstance();
        final User[] t = {new User()};
        fire_base.getData(t[0], new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t[0] = snapshot.getValue(t[0].getClass());
                assert t[0] != null;
                user = new User(t[0]);
                RenderText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                user = new User();
            }
        });

    }

    private void RenderText() {
        Profile profile = user.getProfile();
        txt_degree.setText("Degree: "+profile.getDegree());
        txt_department.setText(profile.getDepartment());
        txt_university.setText(profile.getUniversity());
        txt_year.setText(profile.getYear());
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

        settingsbtn = findViewById(R.id.btn_settings);
        appostedbtn = findViewById(R.id.btn_apposted);
        ptilfpartnersbtn = findViewById(R.id.btn_ptilfpartners); // PTILF - Projects that i looked for partners
        chatbtn = findViewById(R.id.btn_chat);

    }
}