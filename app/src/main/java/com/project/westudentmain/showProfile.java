package com.project.westudentmain;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidproject.R;

public class showProfile extends AppCompatActivity {
    private ImageView profpic;
    private TextView universitytview,departmenttview,degreetview,yeartview,biotview,fwaitv;
    private Button settingsbtn,appostedbtn,ptilfpartnersbtn,chatbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        connect_items_by_id();
    }
    private void connect_items_by_id() {
        profpic = findViewById(R.id.profilepicview);
        universitytview = findViewById(R.id.universitytextview);
        departmenttview = findViewById(R.id.departmenttextview);
        degreetview = findViewById(R.id.degreetextview);
        yeartview = findViewById(R.id.yeartextview);
        biotview = findViewById(R.id.biotextview);
        fwaitv=findViewById(R.id.fwaitextview); // FWAI - From Where Am I
        settingsbtn = findViewById(R.id.btn_settings);
        appostedbtn = findViewById(R.id.btn_apposted);
        ptilfpartnersbtn = findViewById(R.id.btn_ptilfpartners); // PTILF - Projects that i looked for partners
        chatbtn = findViewById(R.id.btn_chat);

    }
}