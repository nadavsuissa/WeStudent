package com.project.westudentmain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.util.FcmNotificationsSender;
import com.project.westudentmain.util.FireBaseGroup;

public class createGroup extends AppCompatActivity {
    private Toolbar mToolBar;
    private EditText edittext_group_name, edittext_group_description, edittext_group_capacity, edittext_group_date;
    private Button btn_create_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        connect_items_by_id();
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        DatabaseReference myRef = database.getReference();



        btn_create_group.setOnClickListener(v ->
        {
            String name = edittext_group_name.getText().toString().trim();
            String description = edittext_group_description.getText().toString().trim();
            int maxcapacity = Integer.parseInt(edittext_group_capacity.getText().toString());

            Group group = new Group();

            group.setGroupName(name);
            group.setMaxCapacity(maxcapacity);
            group.setDescription(description);



            FireBaseGroup.getInstance().pushNewGroup(group,(what, ok) -> {

                Toast.makeText(getBaseContext(),what, Toast.LENGTH_SHORT).show();
                //TODO: switch to the group itself
                startActivity(new Intent(this, showProfile.class));
            });
        });

    }

    private void connect_items_by_id() {
        edittext_group_capacity = findViewById(R.id.maxcapacity);
        edittext_group_description = findViewById(R.id.groupdescription);
        edittext_group_name = findViewById(R.id.groupnameinput);
        btn_create_group = findViewById(R.id.creategroupbtn);
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
        if (item.getItemId() == R.id.setting_menu ) {
            startActivity(new Intent(this, showSettings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
