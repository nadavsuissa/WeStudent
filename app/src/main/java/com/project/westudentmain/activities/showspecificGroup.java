package com.project.westudentmain.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject.R;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.classes.GroupData;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class showspecificGroup extends AppCompatActivity {
    private Toolbar mToolBar;
    private TextView txt_group_name, txt_group_manager_name, txt_group_description;
    private TextSwitcher txt_notifications;
    private ImageButton push_notifications;
    private Button btn_leave_group;
    private RecyclerView rv_group_members;
    private final Context context =this;
    private final FireBaseGroup fireBaseGroup = FireBaseGroup.getInstance();
    private GroupData group;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
        connect_items_by_id();
        configTextSwitcher();


        if(getIntent().hasExtra("from group_adapter")){
            Gson gson = new Gson();
            Type type = new TypeToken<GroupData>(){}.getType();
            String group_json = getIntent().getStringExtra("from group_adapter");
            group = gson.fromJson(group_json,type );
            txt_group_name.setText(group.getGroupName());
            txt_group_name.setTypeface(null, Typeface.BOLD_ITALIC);
            txt_group_description.setText(group.getDescription());
            push_notifications.setOnClickListener(view->{
                popUpDialog();
            });

            btn_leave_group.setOnClickListener(v -> {
                //TODO: add if i'm manager change text to delete group
                fireBaseGroup.leaveGroup(group.getGroupId(), (what, ok) -> {

                });
            });
        }


    }

    private void configTextSwitcher() {
        txt_notifications.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left));
        txt_notifications.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));

        txt_notifications.setFactory(() -> {
            TextView t = new TextView(context);
            t.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL);
            t.setTextSize(18);
            t.setTextColor(getColor(R.color.red));
            return t;
        });
    }

    private void popUpDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Write new notification:");

        EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Send", (dialog, whichButton) -> {


            FireBaseGroup.getGroupUsersFriends(group.getGroupId(), new CustomDataListener() {
                @Override
                public void onDataChange(@NonNull Object data) {
                    ArrayList<User> group_friends = (ArrayList<User>)data;

                    //TODO: here you send the notifications to the members group using group_friends
                }

                @Override
                public void onCancelled(@NonNull String error) {

                }
            });
            txt_notifications.setText(input.getText());
        }).setNegativeButton("Cancel", (dialog, whichButton) -> {
        }).show();
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
        txt_group_name = findViewById(R.id.groupnametv);
        txt_group_manager_name = findViewById(R.id.groupmanageroutput);
        txt_group_description = findViewById(R.id.groupdescriptionoutput);
        txt_notifications = (TextSwitcher)findViewById(R.id.textSwitcher);
        push_notifications = findViewById(R.id.pushnotificationbutton);
        btn_leave_group = findViewById(R.id.leavgroupbutton);
        rv_group_members = findViewById(R.id.groupmemberrv);
    }
}