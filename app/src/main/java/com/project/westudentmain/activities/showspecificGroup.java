package com.project.westudentmain.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.project.westudentmain.adapters.GroupAskingRecViewAdapter;
import com.project.westudentmain.adapters.GroupMembersRecViewAdapter;
import com.project.westudentmain.adapters.UserRecyclerViewAdapter;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.classes.GroupData;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FcmNotificationsSender;
import com.project.westudentmain.util.FireBaseGroup;
import com.project.westudentmain.util.FireBaseToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class showspecificGroup extends AppCompatActivity {
    private Toolbar mToolBar;
    private TextView txt_group_name, txt_group_manager_name, txt_group_description,txt_head_line_notif,txt_asking;
    private TextSwitcher txt_notifications;
    private ImageButton push_notifications;
    private Button btn_leave_group;
    private RecyclerView rv_group_members,rv_asking_users;
    private final Context context =this;
    private final FireBaseGroup fireBaseGroup = FireBaseGroup.getInstance();
    private GroupData group;
    private String sToken;



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

            fireBaseGroup.getGroupManager(group.getGroupId(), new CustomDataListener() {
                @Override
                public void onDataChange(@NonNull Object data) {
                    txt_group_manager_name.setText((String)data);
                    txt_group_description.setText(group.getDescription());
                }
                @Override
                public void onCancelled(@NonNull String error) {
                }
            });

            push_notifications.setOnClickListener(view->{
                popUpDialog();
            });
            btn_leave_group.setVisibility(View.GONE);
            push_notifications.setVisibility(View.GONE);
            txt_head_line_notif.setVisibility(View.GONE);
            txt_notifications.setVisibility(View.GONE);
            rv_asking_users.setVisibility(View.GONE);
            txt_asking.setVisibility(View.GONE);

            // TODO: fix problem with isManagerGroup
            FireBaseData.getUser(new CustomDataListener() {
                @Override
                public void onDataChange(@NonNull Object data) {
                    User my_user = (User) data;

                    FireBaseGroup.getGroupUsersFriends(group.getGroupId(), new CustomDataListener() {
                        @Override
                        public void onDataChange(@NonNull Object data) {
                            ArrayList<User> users = (ArrayList<User>) data;
                            users.forEach(user -> {
                                if (user.getUserName().equals(my_user.getUserName())) {
                                    push_notifications.setVisibility(View.GONE);
                                    btn_leave_group.setVisibility(View.VISIBLE);
                                    txt_head_line_notif.setVisibility(View.VISIBLE);
                                    txt_notifications.setVisibility(View.VISIBLE);
                                }
                            });
                            GroupMembersRecViewAdapter adapter = new GroupMembersRecViewAdapter(context,group);
                            adapter.setMembers(users);
                            rv_group_members.setAdapter(adapter);
                            rv_group_members.setLayoutManager(new GridLayoutManager(context, 1)); // splitting the contacts to 2 columns
                        }

                        @Override
                        public void onCancelled(@NonNull String error) {
                        }
                    });
                    FireBaseGroup.isGroupManager(my_user.getUserName(), group.getGroupId(), (what, ok) -> {
                        if(ok){
                            btn_leave_group.setVisibility(View.VISIBLE);
                            btn_leave_group.setText("delete group");
                            txt_head_line_notif.setVisibility(View.VISIBLE);
                            txt_notifications.setVisibility(View.VISIBLE);
                            push_notifications.setVisibility(View.VISIBLE);
                            rv_asking_users.setVisibility(View.VISIBLE);
                            txt_asking.setVisibility(View.VISIBLE);
                            FireBaseGroup.getGroupAskingUsers(group.getGroupId(), new CustomDataListener() {
                                @Override
                                public void onDataChange(@NonNull Object data) {
                                    ArrayList<User> users = (ArrayList<User>) data;
                                    GroupAskingRecViewAdapter adapter = new GroupAskingRecViewAdapter(context,group);
                                    adapter.setMembers(users);
                                    rv_asking_users.setAdapter(adapter);
                                    rv_asking_users.setLayoutManager(new GridLayoutManager(context, 1)); // splitting the contacts to 2 columns
                                }

                                @Override
                                public void onCancelled(@NonNull String error) {

                                }
                            });
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull String error) {
                }
            });


            btn_leave_group.setOnClickListener(v -> {
                FireBaseGroup.getGroupData(group.getGroupId(), new CustomDataListener() {
                    @Override
                    public void onDataChange(@NonNull Object data) {
                        Group group_functions = (Group) data;
                        FireBaseData.getUser(new CustomDataListener() {
                            @Override
                            public void onDataChange(@NonNull Object data) {
                                User my_user = (User) data;
                                if(group_functions.isOnManagerList(my_user.getUserName())){
                                    fireBaseGroup.deleteGroup(group.getGroupId(), (what, ok) -> {
                                    });
                                }
                                else {
                                    fireBaseGroup.leaveGroup(group.getGroupId(), (what, ok) -> {
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull String error) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull String error) {
                    }
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

            txt_notifications.setText(input.getText());
            FireBaseGroup.getGroupUsersFriends(group.getGroupId(), new CustomDataListener() {
                @Override
                public void onDataChange(@NonNull Object data) {
                    ArrayList<User> group_friends = (ArrayList<User>)data;
                    // For Each User, Get Token - Send Private Notification
                    for (User a:group_friends) {

                        FireBaseToken.getUserToken(a.getUserName(), new CustomDataListener() {
                            @Override
                            public void onDataChange(@NonNull Object data) {
                                if (true){
                                    sToken = (String) data;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull String error) {

                            }
                        });
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(sToken,
                                "Group Notification",
                                input.getText().toString(),
                                context.getApplicationContext(),
                               showspecificGroup.this);
                        notificationsSender.SendNotifications();
                    }

                    //TODO: here you send the notifications to the members group using group_friends
                }

                @Override
                public void onCancelled(@NonNull String error) {

                }
            });


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
        txt_head_line_notif = findViewById(R.id.groupnotificationtv);
        txt_asking = findViewById(R.id.asking_users);
        rv_asking_users = findViewById(R.id.rv_asking_users);
    }
}