package com.project.westudentmain.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.project.westudentmain.adapters.GroupRecycleViewAdapter;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseData;

import java.util.ArrayList;

public class showGroup extends AppCompatActivity {
    private Toolbar mToolBar;
    private ImageButton image_button;
    private RecyclerView groups_rec_view;
    private ArrayList<Group> groups;
    private FireBaseData fire_base_data;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group);

        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);

        groups_rec_view = findViewById(R.id.groupRV);

        image_button = findViewById(R.id.createGroup);
        image_button.setOnClickListener(view -> {
            startActivity(new Intent(this, createGroup.class));
        });
//        groups = new ArrayList<>();
//        groups.add(new Group());
//        groups.add(new Group("user_name2","name2", 3, "mail2"));
        fire_base_data = FireBaseData.getInstance();
        fire_base_data.getAllGroups(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                groups = (ArrayList<Group>) data;
                GroupRecycleViewAdapter adapter = new GroupRecycleViewAdapter(context);
                adapter.setGroups(groups);

                groups_rec_view.setAdapter(adapter);

                groups_rec_view.setLayoutManager(new GridLayoutManager(context, 1)); // splitting the contacts to 2 columns
            }

            @Override
            public void onCancelled(@NonNull String error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(showGroup.this, showProfile.class)); // this is how to move between screens
    }
}