package com.project.westudentmain.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidproject.R;
import com.project.westudentmain.classes.Profile;

public class showProfile extends AppCompatActivity {
    private ImageView profile_img;
    private TextView txt_university, txt_department, txt_degree, txt_year,biotview,fwaitv;
    private Button settingsbtn,appostedbtn,ptilfpartnersbtn,chatbtn;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        connect_items_by_id();
        RenderText();
    }

    private void RenderText() {
        txt_degree
    }

    private void connect_items_by_id() {
        profile_img = findViewById(R.id.profilepicview);
        txt_university = findViewById(R.id.universitytextview);
        txt_department = findViewById(R.id.departmenttextview);
        txt_degree = findViewById(R.id.degreetextview);
        txt_year = findViewById(R.id.yeartextview);
        biotview = findViewById(R.id.biotextview);
        fwaitv=findViewById(R.id.fwaitextview); // FWAI - From Where Am I
        settingsbtn = findViewById(R.id.btn_settings);
        appostedbtn = findViewById(R.id.btn_apposted);
        ptilfpartnersbtn = findViewById(R.id.btn_ptilfpartners); // PTILF - Projects that i looked for partners
        chatbtn = findViewById(R.id.btn_chat);

    }
}