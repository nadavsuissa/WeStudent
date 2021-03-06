package com.project.westudentmain.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.project.westudentmain.util.FireBaseGroup;

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

        fire_base_data = FireBaseData.getInstance();
        CustomDataListener customDataListener = new CustomDataListener() {
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
        };
        if(getIntent().getBooleanExtra("from profile",false)){
            FireBaseGroup.getConnectedGroups(customDataListener);
        }
        else {
            fire_base_data.getAllGroups(customDataListener);
        }

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
        if (item.getItemId() == R.id.setting_menu ) {
            startActivity(new Intent(this, showSettings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(showGroup.this, showProfile.class)); // this is how to move between screens
    }
}