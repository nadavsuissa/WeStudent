package com.project.westudentmain.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject.R;

public class showspecificGroup extends AppCompatActivity {
    private Toolbar mToolBar;
    private TextView txt_groupname, txt_groupmanagername, txt_groupdiscription, txt_groupnotifications;
    private EditText edtxt_enternotifications;
    private ImageButton imgbtn_pushnotifications;
    private Button btn_leavegroup;
    private RecyclerView rv_groupmembers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
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
            case R.id.mi_create_group:
                startActivity(new Intent(this, createGroup.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void connect_items_by_id() {
        txt_groupname = findViewById(R.id.groupnametv);
        txt_groupmanagername = findViewById(R.id.groupmanageroutput);
        txt_groupdiscription = findViewById(R.id.groupdescriptionoutput);
        txt_groupnotifications = findViewById(R.id.groupnotificationview);
        edtxt_enternotifications = findViewById((R.id.edtxt_enternotification));
        imgbtn_pushnotifications = findViewById(R.id.pushnotificationbutton);
        btn_leavegroup = findViewById(R.id.leavgroupbutton);
        rv_groupmembers= findViewById(R.id.groupmemberrv);
    }
}